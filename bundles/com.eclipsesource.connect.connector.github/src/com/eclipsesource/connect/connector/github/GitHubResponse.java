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
package com.eclipsesource.connect.connector.github;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Splitter.on;
import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.MultivaluedMap;

import com.eclipsesource.connect.api.html.Pagination;
import com.eclipsesource.connect.connector.github.internal.GitHubPaginationBuilder;
import com.eclipsesource.connect.connector.github.model.GHScope;


public class GitHubResponse<T> {

  private final T entity;
  private final Pagination pagination;
  private final long timestamp;
  private final List<GHScope> authorizedScopes;
  private final List<GHScope> usedScopes;
  private final int rateLimit;
  private final int rateRemaining;
  private final int rateReset;

  public GitHubResponse( T entity, MultivaluedMap<String, Object> headers ) {
    checkArgument( entity != null, "Entity must not be null" );
    checkArgument( headers != null, "Headers must not be null" );
    this.entity = entity;
    this.pagination = new GitHubPaginationBuilder( readLinks( headers ) ).build();
    this.timestamp = new Date().getTime();
    this.authorizedScopes = readScopes( "X-OAuth-Scopes", headers );
    this.usedScopes = readScopes( "X-Accepted-OAuth-Scopes", headers );
    this.rateLimit = readInt( "X-RateLimit-Limit", headers );
    this.rateRemaining = readInt( "X-RateLimit-Remaining", headers );
    this.rateReset = readInt( "X-RateLimit-Reset", headers );
  }

  private List<Link> readLinks( MultivaluedMap<String, Object> headers ) {
    List<Link> links = new ArrayList<>();
    List<Object> list = headers.get( "Link" );
    if( list != null ) {
      list.forEach( element -> {
        List<String> rawLinks = newArrayList( on( "," ).trimResults().split( ( String )element ) );
        rawLinks.forEach( link -> links.add( Link.valueOf( link ) ) );
      } );
    }
    return links;
  }

  private List<GHScope> readScopes( String headerName, MultivaluedMap<String, Object> headers ) {
    List<GHScope> scopes = new ArrayList<>();
    List<Object> scopeHeader = headers.get( headerName );
    if( scopeHeader != null ) {
      scopeHeader.forEach( scope -> {
        newArrayList( on( "," ).trimResults().split( ( String )scope ) )
          .forEach( singleScope -> scopes.add( GHScope.fromString( singleScope ) ) );
      } );
    }
    return scopes;
  }

  private int readInt( String headerName, MultivaluedMap<String, Object> headers ) {
    List<Object> headerValue = headers.get( headerName );
    if( headerValue != null ) {
      for( Object header : headerValue ) {
        return Integer.parseInt( ( String )header );
      }
    }
    return -1;
  }

  public T getEntity() {
    return entity;
  }

  public Pagination getPagination() {
    return pagination;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public List<GHScope> getAuthorizedScopes() {
    return authorizedScopes;
  }

  public List<GHScope> getUsedScope() {
    return usedScopes;
  }

  public int getRateLimit() {
    return rateLimit;
  }

  public int getRateRemaining() {
    return rateRemaining;
  }

  public int getRateResetTimeSeconds() {
    return rateReset;
  }

}
