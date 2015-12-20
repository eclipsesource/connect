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
import static com.mongodb.client.model.Filters.eq;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.eclipsesource.connect.api.persistence.Query;
import com.eclipsesource.connect.api.persistence.Query.SortDirection;
import com.eclipsesource.connect.api.persistence.Storage;
import com.eclipsesource.connect.serialization.GsonFactory;
import com.github.fakemongo.Fongo;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;


public class InMemoryStorage implements Storage {

  private static final String KEY_ID = "_id";
  private static final UpdateOptions UPDATE_OPTIONS = new UpdateOptions().upsert( true );

  private MongoDatabase db;
  private Gson gson;

  public InMemoryStorage() {
    Fongo fongo = new Fongo("mongo server 1");
    db = fongo.getDatabase( "test-db" );
    gson = GsonFactory.createBuilder( false ).registerTypeAdapter( InputStream.class, new InputStreamTypeAdapter( db ) ).create();
  }

  @Override
  public void store( String place, Object object ) {
    validateArguments( place, object );
    store( place, object, new Object[]{} );
  }

  @Override
  public void store( String place, Object object, Object... objects ) {
    validateArguments( place, object );
    MongoCollection<Document> collection = db.getCollection( place );
    List<Document> documents = createDocuments( object, objects );
    documents.forEach( document -> {
      collection.updateOne( eq( KEY_ID, document.get( KEY_ID ) ), new Document( "$set", document ), UPDATE_OPTIONS );
    } );
  }

  @Override
  public void delete( String place, Object object ) {
    validateArguments( place, object );
    delete( place, object, new Object[]{} );
  }

  @Override
  public void delete( String place, Object object, Object... objects ) {
    validateArguments( place, object );
    MongoCollection<Document> collection = db.getCollection( place );
    List<Document> documents = createDocuments( object, objects );
    documents.forEach( document -> collection.deleteOne( new Document( KEY_ID, document.get( KEY_ID ) ) ) );
  }

  @Override
  public void delete( Query<?> query ) {
    checkArgument( query != null, "Query must not be null" );
    MongoCollection<Document> collection = db.getCollection( query.getPlace() );
    Document document = createDocument( query );
    collection.deleteMany( document );
  }

  private void validateArguments( String place, Object object ) {
    checkArgument( place != null, "Place must not be null" );
    checkArgument( !place.trim().isEmpty(), "Place must not be empty" );
    checkArgument( object != null, "Object must not be null" );
  }

  private List<Document> createDocuments( Object object, Object[] objects ) {
    List<Document> result = new ArrayList<>();
    addDocument( result, object );
    if( objects != null ) {
      Stream.of( objects ).forEach( otherObject -> addDocument( result, otherObject ) );
    }
    return result;
  }

  private void addDocument( List<Document> documents, Object object ) {
    String serializedObject = gson.toJson( object );
    Document document = Document.parse( serializedObject );
    ensureId( document );
    documents.add( document );
  }

  private void ensureId( Document document ) {
    Object id = document.get( KEY_ID );
    if( id == null ) {
      document.put( KEY_ID, ObjectId.get().toString() );
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
    MongoCollection<Document> collection = db.getCollection( query.getPlace() );
    List<T> result = new ArrayList<>();
    for( Document document : prepareIterable( query, collection.find( createDocument( query ) ) ) ) {
      result.add( gson.fromJson( document.toJson(), query.getType() ) );
    }
    return result;
  }

  @Override
  public long count( Query<?> query ) {
    checkArgument( query != null, "Query must not be null" );
    MongoCollection<Document> collection = db.getCollection( query.getPlace() );
    return collection.count( createDocument( query ) );
  }

  private FindIterable<Document> prepareIterable( Query<?> query, FindIterable<Document> iterable ) {
    if( query.getLimit() != -1 ) {
      iterable.limit( query.getLimit() );
    }
    if( query.getSortField() != null ) {
      iterable.sort( new Document( query.getSortField(), query.getSortDirection() == SortDirection.ASC ? 1 : -1 ) );
    }
    if( query.getSkip() != Query.UNDEFINED ) {
      iterable.skip( query.getSkip() );
    }
    return iterable;
  }

  private Document createDocument( Query<?> query ) {
    Map<String, Object> conditions = query.getConditions();
    Document document = new Document();
    conditions.entrySet().forEach( entry -> document.append( entry.getKey(), entry.getValue() ) );
    return document;
  }

}
