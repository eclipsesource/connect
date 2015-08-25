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
package com.eclipsesource.connect.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;

import com.eclipsesource.connect.api.serialization.Name;
import com.eclipsesource.connect.model.Id;


public class GsonSerializationTest {

  private GsonSerialization serialization;

  @Before
  public void setUp() throws ConfigurationException {
    serialization = new GsonSerialization();
    serialization.updated( new Hashtable<String, Object>() );
  }

  @Test
  public void testSerializesObjectWithId() {
    TestType testType = new TestType( new Id( "bar" ), "foo", "bar" );

    String serializedObject = serialization.serialize( testType );
    TestType deserialized = serialization.deserialize( serializedObject, TestType.class );

    assertThat( deserialized.getId() ).isEqualTo( new Id( "bar" ) );
    assertThat( deserialized.getFoo() ).isEqualTo( "foo" );
  }

  @Test
  public void testSerializesIdAs_id() {
    TestType testType = new TestType( new Id( "bar" ), "foo", "bar" );

    String serializedObject = serialization.serialize( testType );

    assertThat( serializedObject ).contains( "_id" );
  }

  @Test
  public void testSerializesFieldWithNameAnnotation() {
    TestType testType = new TestType( new Id( "bar" ), "foo", "bar" );

    String serializedObject = serialization.serialize( testType );

    assertThat( serializedObject ).contains( "foobar" );
    assertThat( serialization.deserialize( serializedObject, TestType.class ).getBar() ).isEqualTo( "bar" );
  }

  @Test
  public void testCanSerializeJava8Dates() {
    ZonedDateTime date = ZonedDateTime.now();

    String json = serialization.serialize( date );
    ZonedDateTime actualDate = serialization.deserialize( json, ZonedDateTime.class );

    assertThat( actualDate ).isEqualTo( date );
  }

  private static class TestType {

    private Id id;
    private String foo;
    @Name( "foobar" )
    private String bar;

    public TestType( Id id, String foo, String bar ) {
      this.id = id;
      this.foo = foo;
      this.bar = bar;
    }

    public Id getId() {
      return id;
    }

    public String getFoo() {
      return foo;
    }

    public String getBar() {
      return bar;
    }

  }

}
