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

import com.eclipsesource.connect.api.serialization.Name;
import com.eclipsesource.connect.model.Id;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;


public class GsonFactory {

  static final String KEY_ID = "_id";

  public static Gson create( boolean prettyPrinting ) {
    GsonBuilder builder = new GsonBuilder();
    if( prettyPrinting ) {
      builder.setPrettyPrinting();
    }
    builder.registerTypeAdapter( Id.class, new IdTypeAdapter() );
    builder.setLongSerializationPolicy( LongSerializationPolicy.STRING );
    Converters.registerAll( builder );
    addFieldNamingStrategy( builder );
    return builder.create();
  }

  private static void addFieldNamingStrategy( GsonBuilder builder ) {
    builder.setFieldNamingStrategy( field -> {
      if( field.getType() == Id.class ) {
        return KEY_ID;
      }
      if( field.isAnnotationPresent( Name.class ) ) {
        return field.getAnnotation( Name.class ).value();
      }
      return field.getName();
    } );
  }

}
