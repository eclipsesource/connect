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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.eclipsesource.connect.api.persistence.Query;
import com.eclipsesource.connect.api.persistence.StorageObserver;
import com.eclipsesource.connect.api.serialization.Deserializer;
import com.eclipsesource.connect.api.serialization.Serializer;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;


public class MongoStorageTest {

  private MongoStorage mongoStorage;
  private MongoCollection<Document> collection;

  @Before
  public void setUp() {
    mongoStorage = new MongoStorage( createSynchronousExecutor() );
    createDBFactory();
    createSerializer();
    createDeserializer();
  }

  private void createDeserializer() {
    Deserializer deserializer = mock( Deserializer.class );
    when( deserializer.deserialize( anyString(), eq( Object.class ) ) ).thenReturn( "foo" );
    mongoStorage.setDeserializer( deserializer );
  }

  private void createSerializer() {
    Serializer serializer = mock( Serializer.class );
    when( serializer.serialize( any( Object.class ) ) ).thenReturn( "{\"foo\":\"bar\"}" );
    mongoStorage.setSerializer( serializer );
  }

  private void createDBFactory() {
    MongoDBFactory factory = mock( MongoDBFactory.class );
    MongoDatabase db = mock( MongoDatabase.class );
    when( factory.getDB() ).thenReturn( db );
    collection = createCollection( db );
    when( db.getCollection( anyString() ) ).thenReturn( collection );
    mongoStorage.setDBFactory( factory );
  }

  @SuppressWarnings("unchecked")
  private MongoCollection<Document> createCollection( MongoDatabase db ) {
    MongoCollection<Document> collection = mock( MongoCollection.class );
    FindIterable<Document> iterable = mock( FindIterable.class );
    when( iterable.iterator() ).thenReturn( mock( MongoCursor.class ) );
    when( collection.find( any( Document.class ) ) ).thenReturn( iterable );
    when( db.getCollection( anyString() ) ).thenReturn( collection );
    return collection;
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullPlace() {
    mongoStorage.store( null, new Object() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddNullObservers() {
    mongoStorage.addObserver( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToRemoveNullObservers() {
    mongoStorage.removeObserver( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyPlace() {
    mongoStorage.store( "", new Object() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhiteSpacePlace() {
    mongoStorage.store( " ", new Object() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNulObject() {
    mongoStorage.store( "foo", null );
  }

  @Test
  public void testStoresObjectInCollection() {
    mongoStorage.store( "foo", new Object() );

    verify( collection ).updateOne( any( Document.class ), any( Document.class ), any( UpdateOptions.class ) );
  }

  @Test
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void testNotifiesObserverAfterStore() {
    StorageObserver observer = mock( StorageObserver.class );
    mongoStorage.addObserver( observer );
    Object object = new Object();
    mongoStorage.store( "foo", object );

    ArgumentCaptor<List> captor = ArgumentCaptor.forClass( List.class );
    verify( observer ).objectsStored( eq( "foo" ), captor.capture() );
    assertThat( captor.getValue().get( 0 ) ).isSameAs( object );
  }

  @Test
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void testNotifiesObserverAfterStoreWithAllObjects() {
    StorageObserver observer = mock( StorageObserver.class );
    mongoStorage.addObserver( observer );
    Object object = new Object();
    Object object1 = new Object();
    Object object2 = new Object();
    mongoStorage.store( "foo", object, object1, object2 );

    ArgumentCaptor<List> captor = ArgumentCaptor.forClass( List.class );
    verify( observer ).objectsStored( eq( "foo" ), captor.capture() );
    assertThat( captor.getValue().get( 0 ) ).isSameAs( object );
    assertThat( captor.getValue().get( 1 ) ).isSameAs( object1 );
    assertThat( captor.getValue().get( 2 ) ).isSameAs( object2 );
  }

  @Test
  public void testDeletesObjectsFromCollection() {
    mongoStorage.delete( "foo", new Object() );

    verify( collection ).deleteOne( any( Document.class ) );
  }

  @Test
  public void testDeletesObjectsFromCollectionWithQuery() {
    mongoStorage.delete( new Query<>( "foo" ).where( "id", "bar" ) );

    ArgumentCaptor<Document> captor = ArgumentCaptor.forClass( Document.class );
    verify( collection ).deleteMany( captor.capture() );
    assertThat( captor.getValue().get( "id" ) ).isEqualTo( "bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToDeleteObjectsFromCollectionWithNullQuery() {
    mongoStorage.delete( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToFindSingleWithNullQuery() {
    mongoStorage.find( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullQuery() {
    mongoStorage.findAll( null );
  }

  @Test
  public void testUsesQueryToFindElements() {
    mongoStorage.findAll( new Query<>( "foo", Object.class ) );

    ArgumentCaptor<Document> captor = ArgumentCaptor.forClass( Document.class );
    verify( collection ).find( captor.capture() );
  }

  @Test
  public void testReturnsNullWhenNoResults() {
    Object object = mongoStorage.find( new Query<>( "foo", Object.class ) );

    ArgumentCaptor<Document> captor = ArgumentCaptor.forClass( Document.class );
    verify( collection ).find( captor.capture() );
    assertThat( object ).isNull();
  }

  private ExecutorService createSynchronousExecutor() {
    ExecutorService executorService = mock( ExecutorService.class );
    doAnswer( new Answer<Object>() {

      @Override
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        Runnable runnable = ( Runnable )invocation.getArguments()[ 0 ];
        runnable.run();
        return null;
      }
    } ).when( executorService ).submit( any( Runnable.class ) );
    return executorService;
  }
}
