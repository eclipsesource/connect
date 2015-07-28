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

import org.junit.Test;

import com.eclipsesource.connect.model.Id;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;


public class IdTypeAdapterTest {

  @Test
  public void testSerializesId() {
    IdTypeAdapter typeAdapter = new IdTypeAdapter();

    JsonElement element = typeAdapter.serialize( new Id( "foo" ), Id.class, null );

    assertThat( element ).isInstanceOf( JsonPrimitive.class );
    assertThat( ( ( JsonPrimitive )element ).getAsString() ).isEqualTo( "foo" );
  }

  @Test
  public void testDeserializesId() {
    IdTypeAdapter typeAdapter = new IdTypeAdapter();

    Id id = typeAdapter.deserialize( new JsonPrimitive( "foo" ), Id.class, null );

    assertThat( id ).isEqualTo( new Id( "foo" ) );
  }

}
