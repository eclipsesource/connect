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

import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class GsonFactoryTest {

  @Test
  public void testCreatesGson() {
    Gson gson = GsonFactory.create( true );

    assertThat( gson ).isNotNull();
  }

  @Test
  public void testSerializesLongAsStrings() {
    Gson gson = GsonFactory.create( true );
    TestObject obj = new TestObject();
    obj.foo = 23L;

    String json = gson.toJson( obj );
    Map<String, Object> parsedMap = gson.fromJson( json, new TypeToken<Map<String, Object>>(){}.getType() );

    assertThat( parsedMap.get( "foo" ) ).isEqualTo( "23" );
  }

  @Test
  public void testDesserializesStringsAsLong() {
    Gson gson = GsonFactory.create( true );

    TestObject obj = gson.fromJson( "{ \"foo\" : \"23\" }", TestObject.class );

    assertThat( obj.foo ).isEqualTo( 23L );
  }

  private static class TestObject {
    Long foo;
  }

}
