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
package com.eclipsesource.connect.connector.stackexchange.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;

import com.eclipsesource.connect.connector.stackexchange.StackExchangeTest;
import com.eclipsesource.connect.connector.stackexchange.model.SEUser;
import com.google.gson.Gson;


public class GzipGsonProviderTest {

  @Test
  public void testGetSize() {
    GzipGsonProvider<TestObject> testObjectProvider = new GzipGsonProvider<TestObject>();

    long size = testObjectProvider.getSize( mock( TestObject.class ), null, null, null, null );

    assertThat( size ).isEqualTo( -1 );
  }

  @Test
  public void testIsWritableWithTestObject() {
    GzipGsonProvider<TestObject> testObjectProvider = new GzipGsonProvider<TestObject>();

    boolean writeable = testObjectProvider.isWriteable( TestObject.class, null, null, null );

    assertTrue( writeable );
  }

  @Test
  public void testIsReadableWithTestObject() {
    GzipGsonProvider<TestObject> testObjectProvider = new GzipGsonProvider<TestObject>();

    boolean readable = testObjectProvider.isReadable( TestObject.class, null, null, null );

    assertTrue( readable );
  }

  @Test
  public void testWritesTestObject() throws WebApplicationException, IOException {
    GzipGsonProvider<TestObject> testObjectProvider = new GzipGsonProvider<TestObject>();
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    TestObject testObject = new TestObject( "foo" );

    testObjectProvider.writeTo( testObject, TestObject.class, null, null, null, null, stream );

    TestObject actualTestObject = new Gson().fromJson( convertToReader( stream ), TestObject.class );
    assertThat( actualTestObject ).isEqualTo( testObject );
  }

  @Test
  public void testWritesTestObjectClosesStream() throws WebApplicationException, IOException {
    GzipGsonProvider<TestObject> testObjectProvider = new GzipGsonProvider<TestObject>();
    OutputStream stream = mock( OutputStream.class );
    TestObject testObject = new TestObject( "foo" );

    testObjectProvider.writeTo( testObject, TestObject.class, null, null, null, null, stream );

    verify( stream ).close();
  }

  @Test
  public void testReadsTestObject() throws WebApplicationException, IOException {
    GzipGsonProvider<SEUser> testObjectProvider = new GzipGsonProvider<SEUser>();
    InputStream inputStream = spy( StackExchangeTest.class.getResourceAsStream( "/test-user.json.gz" ) );

    SEUser user = testObjectProvider.readFrom( SEUser.class, null, null, null, null, inputStream );

    assertThat( user.getDisplayName() ).isEqualTo( "FooBar" );
    assertThat( user.getUserId() ).isEqualTo( "2131212" );
  }

  @Test
  public void testReadTestObjectClosesStream() throws WebApplicationException, IOException {
    GzipGsonProvider<SEUser> testObjectProvider = new GzipGsonProvider<SEUser>();
    InputStream inputStream = spy( StackExchangeTest.class.getResourceAsStream( "/test-user.json.gz" ) );

    testObjectProvider.readFrom( SEUser.class, null, null, null, null, inputStream );

    verify( inputStream ).close();
  }

  private BufferedReader convertToReader( ByteArrayOutputStream stream ) {
    InputStream input = new ByteArrayInputStream(stream.toByteArray());
    return new BufferedReader( new InputStreamReader( input ) );
  }

  private static class TestObject {

    private final String id;

    public TestObject( String id ) {
      this.id = id;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
      return result;
    }

    @Override
    public boolean equals( Object obj ) {
      if( this == obj )
        return true;
      if( obj == null )
        return false;
      if( getClass() != obj.getClass() )
        return false;
      TestObject other = ( TestObject )obj;
      if( id == null ) {
        if( other.id != null )
          return false;
      } else if( !id.equals( other.id ) )
        return false;
      return true;
    }

  }
}
