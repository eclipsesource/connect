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

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import com.eclipsesource.connect.api.serialization.Deserializer;
import com.eclipsesource.connect.api.serialization.Serializer;
import com.google.gson.Gson;


public class GsonSerialization implements ManagedService, Serializer, Deserializer {

  static final String PROPERTY_PRETTY_PRINTING = "pretty.printing";

  private Gson gson;

  @Override
  public void updated( Dictionary<String, ?> properties ) throws ConfigurationException {
    gson = GsonFactory.create( usePrettyPrinting( properties ) );
  }

  private boolean usePrettyPrinting( Dictionary<String, ?> properties ) {
    Object prettyPrinting = properties.get( PROPERTY_PRETTY_PRINTING );
    if( prettyPrinting != null ) {
      return Boolean.parseBoolean( ( String )prettyPrinting );
    }
    return false;
  }

  @Override
  public String serialize( Object object ) {
    return gson.toJson( object );
  }

  @Override
  public <T> T deserialize( String serializedObject, Class<T> type ) {
    return gson.fromJson( serializedObject, type );
  }
}
