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
package com.eclipsesource.connect.markdown;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;


public class GitHubMarkdownParserConfiguration implements ManagedService {

  static final String PROPERTY_ACCESS_TOKEN = "access.token";

  private String accessToken;

  @Override
  public void updated( Dictionary<String, ?> properties ) throws ConfigurationException {
    validateProperties( properties );
    this.accessToken = ( String )properties.get( PROPERTY_ACCESS_TOKEN );
  }

  private void validateProperties( Dictionary<String, ?> properties ) throws ConfigurationException {
    if( properties.get( PROPERTY_ACCESS_TOKEN ) == null ) {
      throw new ConfigurationException( PROPERTY_ACCESS_TOKEN, "Access Token must be set" );
    }
  }

  public String getAccessToken() {
    return accessToken;
  }
}
