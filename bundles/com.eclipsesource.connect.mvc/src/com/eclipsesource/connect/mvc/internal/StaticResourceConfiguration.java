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
package com.eclipsesource.connect.mvc.internal;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;


public class StaticResourceConfiguration implements ManagedService {

  static final String PROPERTY_USE_CACHE = "use.cache";
  static final String PROPERTY_USE_COMPRESS = "use.compress";

  private boolean useCache;
  private boolean useCompress;

  @Override
  public void updated( Dictionary<String, ?> properties ) throws ConfigurationException {
    verifyPropertiesAreComplete( properties );
    useCache = Boolean.valueOf( ( String )properties.get( PROPERTY_USE_CACHE ) );
    useCompress = Boolean.valueOf( ( String )properties.get( PROPERTY_USE_COMPRESS ) );
  }

  private void verifyPropertiesAreComplete( Dictionary<String, ?> properties ) throws ConfigurationException {
    if( properties.get( PROPERTY_USE_CACHE ) == null ) {
      throw new ConfigurationException( PROPERTY_USE_CACHE, "Use Cache must be set" );
    }
    if( properties.get( PROPERTY_USE_COMPRESS ) == null ) {
      throw new ConfigurationException( PROPERTY_USE_COMPRESS, "Use Compress must be set" );
    }
  }

  public boolean useCache() {
    return useCache;
  }

  public boolean useCompress() {
    return useCompress;
  }
}
