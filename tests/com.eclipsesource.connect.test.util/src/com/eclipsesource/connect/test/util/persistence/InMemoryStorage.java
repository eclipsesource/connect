/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.test.util.persistence;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.osgi.service.cm.ConfigurationException;

import com.eclipsesource.connect.api.persistence.Query;
import com.eclipsesource.connect.api.persistence.Query.SortDirection;
import com.eclipsesource.connect.api.persistence.Storage;
import com.eclipsesource.connect.serialization.GsonSerialization;
import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;


public class InMemoryStorage implements Storage {

  private static final String KEY_ID = "_id";

  private DB db;
  private GsonSerialization serialization;

  public InMemoryStorage() {
    Fongo fongo = new Fongo("mongo server 1");
    db = fongo.getDB( "test-db" );
    serialization = new GsonSerialization();
    try {
      Hashtable<String, Object> properties = new Hashtable<>();
      properties.put( "pretty.printing", "true" );
      serialization.updated( properties );
    } catch( ConfigurationException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  @Override
  public void store( String place, Object object ) {
    validateArguments( place, object );
    store( place, object, new Object[]{} );
  }

  @Override
  public void store( String place, Object object, Object... objects ) {
    validateArguments( place, object );
    DBCollection collection = db.getCollection( place );
    List<DBObject> dbObjects = createDBObjects( object, objects );
    dbObjects.forEach( dbObject -> collection.save( dbObject ) );
  }

  @Override
  public void delete( String place, Object object ) {
    validateArguments( place, object );
    delete( place, object, new Object[]{} );
  }

  @Override
  public void delete( String place, Object object, Object... objects ) {
    validateArguments( place, object );
    DBCollection collection = db.getCollection( place );
    List<DBObject> dbObjects = createDBObjects( object, objects );
    dbObjects.forEach( dbObject -> collection.remove( new BasicDBObject( KEY_ID, dbObject.get( KEY_ID ) ) ) );
  }

  @Override
  public void delete( Query<?> query ) {
    checkArgument( query != null, "Query must not be null" );
    DBCollection collection = db.getCollection( query.getPlace() );
    DBObject dbObject = createDBObject( query );
    collection.remove( dbObject );
  }

  private void validateArguments( String place, Object object ) {
    checkArgument( place != null, "Place must not be null" );
    checkArgument( !place.trim().isEmpty(), "Place must not be empty" );
    checkArgument( object != null, "Object must not be null" );
  }

  private List<DBObject> createDBObjects( Object object, Object[] objects ) {
    List<DBObject> result = new ArrayList<>();
    addDbObject( result, object );
    if( objects != null ) {
      Stream.of( objects ).forEach( otherObject -> addDbObject( result, otherObject ) );
    }
    return result;
  }

  private void addDbObject( List<DBObject> result, Object object ) {
    String serializedObject = serialization.serialize( object );
    DBObject dbObject = ( DBObject )JSON.parse( serializedObject );
    ensureId( dbObject );
    result.add( dbObject );
  }

  private void ensureId( DBObject dbObject ) {
    Object id = dbObject.get( KEY_ID );
    if( id == null ) {
      dbObject.put( KEY_ID, ObjectId.get().toString() );
    }
  }

  @Override
  public <T> T find( Query<T> query ) {
    List<T> result = findAll( query );
    if( !result.isEmpty() ) {
      return result.get( 0 );
    }
    return null;
  }

  @Override
  public <T> List<T> findAll( Query<T> query ) {
    checkArgument( query != null, "Query must not be null" );
    DBCollection collection = db.getCollection( query.getPlace() );
    List<T> result = new ArrayList<>();
    try( DBCursor cursor = collection.find( createDBObject( query ) ) ) {
      prepareCursor( query, cursor );
      while(cursor.hasNext()) {
        DBObject next = cursor.next();
        result.add( serialization.deserialize( JSON.serialize( next ), query.getType() ) );
      }
    }
    return result;
  }

  @Override
  public long count( Query<?> query ) {
    checkArgument( query != null, "Query must not be null" );
    DBCollection collection = db.getCollection( query.getPlace() );
    try( DBCursor cursor = collection.find( createDBObject( query ) ) ) {
      prepareCursor( query, cursor );
      return cursor.count();
    }
  }

  private void prepareCursor( Query<?> query, DBCursor cursor ) {
    if( query.getLimit() != -1 ) {
      cursor.limit( query.getLimit() );
    }
    if( query.getSortField() != null ) {
      createSorting( query, cursor );
    }
    if( query.getSkip() != Query.UNDEFINED ) {
      cursor.skip( query.getSkip() );
    }
  }

  private DBObject createDBObject( Query<?> query ) {
    Map<String, Object> conditions = query.getConditions();
    BasicDBObject dbObject = new BasicDBObject();
    conditions.entrySet().forEach( entry -> dbObject.append( entry.getKey(), entry.getValue() ) );
    return dbObject;
  }

  private void createSorting( Query<?> query, DBCursor cursor ) {
    BasicDBObject sortObject = new BasicDBObject( query.getSortField(), query.getSortDirection() == SortDirection.ASC ? 1 : -1 );
    cursor.sort( sortObject );
  }

}
