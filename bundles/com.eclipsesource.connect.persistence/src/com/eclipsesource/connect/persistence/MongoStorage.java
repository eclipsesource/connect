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
package com.eclipsesource.connect.persistence;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.bson.types.ObjectId;

import com.eclipsesource.connect.api.persistence.Query;
import com.eclipsesource.connect.api.persistence.Query.SortDirection;
import com.eclipsesource.connect.api.persistence.Storage;
import com.eclipsesource.connect.api.persistence.StorageObserver;
import com.eclipsesource.connect.api.serialization.Deserializer;
import com.eclipsesource.connect.api.serialization.Serializer;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;


public class MongoStorage implements Storage {

  private static final String KEY_ID = "_id";

  private final List<StorageObserver> observers;
  private final ExecutorService executor;
  private MongoDBFactory factory;
  private Serializer serializer;
  private Deserializer deserializer;

  public MongoStorage() {
    this( Executors.newSingleThreadExecutor( new ThreadFactoryBuilder().setNameFormat( "storage-observer-notifier-%d" ).build() ) );
  }

  MongoStorage( ExecutorService executor ) {
    this.executor = executor;
    this.observers = new ArrayList<>();
  }

  @Override
  public void store( String place, Object object ) {
    validateArguments( place, object );
    store( place, object, new Object[]{} );
  }

  @Override
  public void store( String place, Object object, Object... objects ) {
    validateArguments( place, object );
    DB db = factory.getDB();
    DBCollection collection = db.getCollection( place );
    List<DBObject> dbObjects = createDBObjects( object, objects );
    dbObjects.forEach( dbObject -> collection.save( dbObject ) );
    notifyObservers( place, object, objects );
  }

  @Override
  public void delete( String place, Object object ) {
    validateArguments( place, object );
    delete( place, object, new Object[]{} );
  }

  @Override
  public void delete( String place, Object object, Object... objects ) {
    validateArguments( place, object );
    DB db = factory.getDB();
    DBCollection collection = db.getCollection( place );
    List<DBObject> dbObjects = createDBObjects( object, objects );
    dbObjects.forEach( dbObject -> collection.remove( new BasicDBObject( KEY_ID, dbObject.get( KEY_ID ) ) ) );
  }

  @Override
  public void delete( Query<?> query ) {
    checkArgument( query != null, "Query must not be null" );
    DB db = factory.getDB();
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
    String serializedObject = serializer.serialize( object );
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

  private void notifyObservers( String place, Object object, Object[] objects ) {
    List<Object> storedObjects = Lists.newArrayList( object );
    storedObjects.addAll( Arrays.asList( objects ) );
    executor.submit( new Runnable() {

      @Override
      public void run() {
        observers.forEach( observer -> observer.objectsStored( place, storedObjects ) );
      }
    } );
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
    DB db = factory.getDB();
    DBCollection collection = db.getCollection( query.getPlace() );
    List<T> result = new ArrayList<>();
    try( DBCursor cursor = collection.find( createDBObject( query ) ) ) {
      prepareCursor( query, cursor );
      while(cursor.hasNext()) {
        DBObject next = cursor.next();
        result.add( deserializer.deserialize( JSON.serialize( next ), query.getType() ) );
      }
    }
    return result;
  }

  @Override
  public long count( Query<?> query ) {
    checkArgument( query != null, "Query must not be null" );
    DB db = factory.getDB();
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

  public void setDBFactory( MongoDBFactory factory ) {
    this.factory = factory;
  }

  public void unsetDBFactory( MongoDBFactory factory ) {
    this.factory = null;
  }

  public void setSerializer( Serializer serializer ) {
    this.serializer = serializer;
  }

  public void unsetSerializer( Serializer serializer ) {
    this.serializer = null;
  }

  public void setDeserializer( Deserializer deserializer ) {
    this.deserializer = deserializer;
  }

  public void unsetDeserializer( Deserializer deserializer ) {
    this.deserializer = null;
  }

  void addObserver( StorageObserver observer ) {
    checkArgument( observer != null, "StorageObserver must not be null" );
    observers.add( observer );
  }

  void removeObserver( StorageObserver observer ) {
    checkArgument( observer != null, "StorageObserver must not be null" );
    observers.remove( observer );
  }
}
