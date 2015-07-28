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
package com.eclipsesource.connect.connector.github.internal;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import com.eclipsesource.connect.connector.github.GitHubException;
import com.eclipsesource.connect.connector.github.GitHubParticipant;
import com.eclipsesource.connect.connector.github.GitHubResponse;


public class GitHubFactory {

  private static final String GITHUB_MEDIA_TYPE = "application/vnd.github.v3+json";
  private static final String PARAM_ACCESS_TOKEN = "access_token";

  private final String baseUrl;
  private final Client client;
  private final String accessToken;
  private final List<GitHubParticipant> particpants;

  public GitHubFactory( String baseUrl, String accessToken, GitHubParticipant... participants  ) {
    checkArgument( baseUrl != null, "Base URL must not be null" );
    checkArgument( !baseUrl.trim().isEmpty(), "Base URL must not be empty" );
    checkArgument( accessToken != null, "Token must not be null" );
    this.baseUrl = baseUrl;
    this.accessToken = accessToken;
    this.client = ClientBuilder.newClient().register( new GitHubProvider<>() );
    this.particpants = Arrays.asList( participants );
  }

  public <T> GitHubResponse<T> get( String path, Class<T> type ) {
    Response response = createTarget( baseUrl + path )
                              .request( GITHUB_MEDIA_TYPE ).get();
    verifyResponse( response );
    GitHubResponse<T> gitHubResponse = new GitHubResponse<T>( response.readEntity( type ), response.getHeaders() );
    notifyParticipants( gitHubResponse );
    return gitHubResponse;
  }

  public <T> GitHubResponse<T> get( String path, Map<String, String> queryParameter, Class<T> type ) {
    WebTarget target = createTarget( baseUrl + path );
    Set<Entry<String, String>> entrySet = queryParameter.entrySet();
    for( Entry<String, String> entry : entrySet ) {
      target = target.queryParam( entry.getKey(), entry.getValue() );
    }
    Response response = target.request( GITHUB_MEDIA_TYPE ).get();
    verifyResponse( response );
    GitHubResponse<T> gitHubResponse = new GitHubResponse<T>( response.readEntity( type ), response.getHeaders() );
    notifyParticipants( gitHubResponse );
    return gitHubResponse;
  }

  public <T> GitHubResponse<T> post( String path, Object entity, Class<T> type ) {
    Response response = createTarget( baseUrl + path )
                              .request( GITHUB_MEDIA_TYPE )
                              .post( Entity.entity( entity, MediaType.APPLICATION_JSON ) );
    verifyResponse( response );
    T readEntity = response.readEntity( type );
    GitHubResponse<T> gitHubResponse = new GitHubResponse<T>( readEntity, response.getHeaders() );
    notifyParticipants( gitHubResponse );
    return gitHubResponse;
  }

  public void delete( String path ) {
    Response response = createTarget( baseUrl + path )
                              .request( GITHUB_MEDIA_TYPE )
                              .delete();
    verifyResponse( response );
    GitHubResponse<Object> gitHubResponse = new GitHubResponse<>( "", response.getHeaders() );
    notifyParticipants( gitHubResponse );
  }

  public <T> GitHubResponse<T> patch( String path, Object entity, Class<T> type  ) {
    Response response = createTarget( baseUrl + path )
                              .request( GITHUB_MEDIA_TYPE )
                              .method( "POST", Entity.entity( entity, MediaType.APPLICATION_JSON ) );
    verifyResponse( response );
    T readEntity = response.readEntity( type );
    GitHubResponse<T> gitHubResponse = new GitHubResponse<T>( readEntity, response.getHeaders() );
    notifyParticipants( gitHubResponse );
    return gitHubResponse;
  }

  private WebTarget createTarget( String url ) {
    if( accessToken.isEmpty() ) {
      return client.target( url );
    }
    return client.target( url ).queryParam( PARAM_ACCESS_TOKEN, accessToken );
  }

  private void verifyResponse( Response response ) {
    if( Family.familyOf( response.getStatus() ) != Family.SUCCESSFUL ) {
      throw new GitHubException( response.getStatus() );
    }
  }

  private void notifyParticipants( GitHubResponse<?> response ) {
    particpants.forEach( participant -> participant.participate( response ) );
  }
}
