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
package com.eclipsesource.connect.persistence.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.eclipsesource.connect.api.persistence.Query;
import com.google.common.io.CharSource;


public class ReadWriteTest extends PersistenceTest {

  public ReadWriteTest( StorageProvider storageProvider ) {
    super( storageProvider );
  }

  @Test
  public void testStoresObject() {
    TestType object = new TestType( "foo" );

    getStorage().store( "foo-store", object );

    List<TestType> all = getStorage().findAll( new Query<>( "foo-store", TestType.class ) );
    assertThat( all ).containsExactly( object );
  }

  @Test
  public void testStoresObjectWithId() {
    TestTypeWithId object = new TestTypeWithId( "foo" );
    getStorage().store( "foo-id", object );
    getStorage().store( "foo-id", new TestTypeWithId( "bar" ) );

    List<TestTypeWithId> all = getStorage().findAll( new Query<>( "foo-id", TestTypeWithId.class ).where( "_id", object.getId().toString() ) );

    assertThat( all.get( 0 ).getName() ).isEqualTo( object.getName() );
  }

  @Test
  public void testStoresObjectWithStream() throws IOException {
    InputStream stream = new ByteArrayInputStream( "foo".getBytes( "utf-8" ) );
    TestTypeWithStream object = new TestTypeWithStream( stream );
    getStorage().store( "foo-stream", object );

    List<TestTypeWithStream> all = getStorage().findAll( new Query<>( "foo-stream", TestTypeWithStream.class ).where( "_id", object.getId().toString() ) );

    String streamContent = read( all.get( 0 ).getStream() );
    assertThat( streamContent ).isEqualTo( "foo" );
  }

  private String read( InputStream stream ) throws IOException {
    return new CharSource() {

      @Override
      public Reader openStream() throws IOException {
        return new InputStreamReader( stream );
      }
    }.read();
  }

  @Test
  public void testUpdatesObject() {
    TestTypeWithId object = new TestTypeWithId( "foo" );

    getStorage().store( "foo-store", object );
    object.setName( "bar" );
    getStorage().store( "foo-store", object );

    List<TestTypeWithId> all = getStorage().findAll( new Query<>( "foo-store", TestTypeWithId.class ) );
    assertThat( all ).hasSize( 1 );
    assertThat( all.get( 0 ).getName() ).isEqualTo( "bar" );
  }

  @Test
  public void testCountsObjects() {
    for( int i = 0; i < 5; i++ ) {
      getStorage().store( "foo-count", new TestTypeWithId( "bar" + 1 ) );
    }
    for( int i = 0; i < 10; i++ ) {
      getStorage().store( "foo-count", new TestTypeWithId( "foo" + 1 ) );
    }

    long fooCount = getStorage().count( new Query<>( "foo-count", TestTypeWithId.class ).where( "name", Pattern.compile( "foo." ) ) );
    long barCount = getStorage().count( new Query<>( "foo-count", TestTypeWithId.class ).where( "name", Pattern.compile( "bar." ) ) );

    assertThat( fooCount ).isEqualTo( 10 );
    assertThat( barCount ).isEqualTo( 5 );
  }

  @Test
  public void testDeletesObject() {
    TestTypeWithId object = new TestTypeWithId( "foo" );
    getStorage().store( "foo-delete", object );

    getStorage().delete( "foo-delete", object );

    List<TestTypeWithId> all = getStorage().findAll( new Query<>( "foo-delete", TestTypeWithId.class ) );
    assertThat( all ).isEmpty();
  }

  @Test
  public void testDeletesMultipleObjects() {
    TestTypeWithId object = new TestTypeWithId( "foo" );
    TestTypeWithId object2 = new TestTypeWithId( "foo2" );
    TestTypeWithId object3 = new TestTypeWithId( "foo3" );
    getStorage().store( "foo-multi-delete", object );
    getStorage().store( "foo-multi-delete", object2 );
    getStorage().store( "foo-multi-delete", object3 );

    getStorage().delete( "foo-multi-delete", object, object2 );

    List<TestTypeWithId> all = getStorage().findAll( new Query<>( "foo-multi-delete", TestTypeWithId.class ) );
    assertThat( all.get( 0 ).getName() ).isEqualTo( object3.getName() );
  }

  @Test
  public void testDeletesAllObjects() {
    getStorage().store( "foo-all-delete", new TestType( "foo" ) );
    getStorage().store( "foo-all-delete", new TestType( "foo2" ) );

    getStorage().delete( new Query<>( "foo-all-delete" ) );

    List<TestType> all = getStorage().findAll( new Query<>( "foo-all-delete", TestType.class ) );
    assertThat( all ).isEmpty();
  }

  @Test
  public void testDeletesObjectWithQuery() {
    getStorage().store( "foo-delete-query", new TestType( "foo" ) );
    getStorage().store( "foo-delete-query", new TestType( "foo2" ) );

    getStorage().delete( new Query<>( "foo-delete-query" ).where( "name", "foo" ) );

    List<TestType> all = getStorage().findAll( new Query<>( "foo-delete-query", TestType.class ) );
    assertThat( all ).hasSize( 1 ).containsExactly( new TestType( "foo2" ) );
  }
}
