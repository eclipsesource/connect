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


import static com.google.common.base.Preconditions.checkArgument;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.eclipsesource.connect.connector.stackexchange.Site;


public class StackExchangeFactory {

  private final String baseUrl;
  private final Client client;
  private final String key;
  private final String accessToken;

  public StackExchangeFactory( String baseUrl, String key, String accessToken ) {
    validateArguments( baseUrl, key, accessToken );
    this.baseUrl = baseUrl;
    this.key = key;
    this.accessToken = accessToken;
    this.client = ClientBuilder.newClient().register( new GzipGsonProvider<>() );
  }

  private void validateArguments( String baseUrl, String key, String accessToken ) {
    checkArgument( baseUrl != null, "Base URL must not be null" );
    checkArgument( !baseUrl.trim().isEmpty(), "Base URL must not be empty" );
    checkArgument( key != null, "Key must not be null" );
    checkArgument( !key.trim().isEmpty(), "Key must not be empty" );
    checkArgument( accessToken != null, "Token must not be null" );
    checkArgument( !accessToken.trim().isEmpty(), "Token must not be empty" );
  }

  public <T> T get( String path, Class<T> type ) {
    return client.target( baseUrl + path )
                 .queryParam( "access_token", accessToken )
                 .queryParam( "key", key )
                 .request( "application/json" )
                 .header( "Content-Encoding", "gzip" )
                 .get( type );
  }

  public <T> T get( String path, Site site, Class<T> type ) {
    return client.target( baseUrl + path )
        .queryParam( "access_token", accessToken )
        .queryParam( "site", site.getName() )
        .queryParam( "key", key )
        .request( "application/json" )
        .header( "Content-Encoding", "gzip" )
        .get( type );
  }
}
