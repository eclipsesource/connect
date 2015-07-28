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
package com.eclipsesource.connect.model.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.gson.JsonElement;


public class JsonUtilTest {

  @Test
  public void testToJson() {
    TestUserData data = new TestUserData( "foo" );

    JsonElement json = JsonUtil.toJson( data );

    assertThat( json.getAsJsonObject().get( "name" ).getAsString() ).isEqualTo( "foo" );
  }

  @Test
  public void testFromJson() {
    TestUserData data = new TestUserData( "foo" );
    JsonElement json = JsonUtil.toJson( data );

    TestUserData actualData = JsonUtil.fromJson( json, TestUserData.class );

    assertThat( actualData.getName() ).isEqualTo( "foo" );
  }

  private class TestUserData {

    private String name;

    public TestUserData( String name ) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

  }
}
