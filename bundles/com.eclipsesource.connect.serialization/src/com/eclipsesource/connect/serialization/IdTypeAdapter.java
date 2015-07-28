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

import java.lang.reflect.Type;

import com.eclipsesource.connect.model.Id;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class IdTypeAdapter implements JsonSerializer<Id>, JsonDeserializer<Id> {

  @Override
  public JsonElement serialize( Id src, Type typeOfSrc, JsonSerializationContext context ) {
    return new JsonPrimitive( src.toString() );
  }

  @Override
  public Id deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context ) throws JsonParseException {
    return new Id( json.getAsString() );
  }
}
