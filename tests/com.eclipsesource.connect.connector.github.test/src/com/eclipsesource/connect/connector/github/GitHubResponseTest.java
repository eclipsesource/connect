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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedHashMap;

import org.junit.Test;

import com.eclipsesource.connect.api.html.Pagination;
import com.eclipsesource.connect.connector.github.model.GHScope;
import com.google.common.collect.Lists;


public class GitHubResponseTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullEntity() {
    new GitHubResponse<>( null, new MultivaluedHashMap<>() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullHeaders() {
    new GitHubResponse<>( new Object(), null );
  }

  @Test
  public void testHasEntity() {
    Object entity = new Object();
    GitHubResponse<Object> response = new GitHubResponse<>( entity, new MultivaluedHashMap<>() );

    Object actualEntity = response.getEntity();

    assertThat( actualEntity ).isSameAs( entity );
  }

  @Test
  public void testHasTimestamp() throws InterruptedException {
    GitHubResponse<Object> response = new GitHubResponse<>( new Object(), new MultivaluedHashMap<>() );
    Thread.sleep( 5 );
    GitHubResponse<Object> response2 = new GitHubResponse<>( new Object(), new MultivaluedHashMap<>() );

    long timestamp = response.getTimestamp();
    long timestamp2 = response2.getTimestamp();

    assertThat( timestamp ).isPositive();
    assertThat( timestamp2 ).isPositive();
    assertThat( timestamp2 ).isNotEqualTo( timestamp );
  }

  @Test
  public void testCreatesPagination() {
    MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
    List<Object> links = new ArrayList<>();
    links.add( "<https://api.github.com/user/repos?page=3&per_page=100>; rel=\"next\", "
             + "<https://api.github.com/user/repos?page=50&per_page=100>; rel=\"last\"");
    headers.put( "Link", links );
    GitHubResponse<Object> response = new GitHubResponse<>( new Object(), headers );

    Pagination pagination = response.getPagination();

    assertThat( pagination.getCurrentPage() ).isEqualTo( 2 );
  }

  @Test
  public void testAuthorizedOauthScope() {
    MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
    headers.put( "X-OAuth-Scopes", Lists.newArrayList( "repo, user" ) );
    GitHubResponse<Object> response = new GitHubResponse<>( new Object(), headers );

    List<GHScope> authorizedScopes = response.getAuthorizedScopes();

    assertThat( authorizedScopes ).contains( GHScope.REPO, GHScope.USER );
  }

  @Test
  public void testUsedOauthScope() {
    MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
    headers.put( "X-Accepted-OAuth-Scopes", Lists.newArrayList( "repo,user" ) );
    GitHubResponse<Object> response = new GitHubResponse<>( new Object(), headers );

    List<GHScope> usedScopes = response.getUsedScope();

    assertThat( usedScopes ).contains( GHScope.REPO, GHScope.USER );
  }

  @Test
  public void testEmptyUsedOauthScope() {
    MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
    headers.put( "X-Accepted-OAuth-Scopes", null );
    GitHubResponse<Object> response = new GitHubResponse<>( new Object(), headers );

    List<GHScope> usedScopes = response.getUsedScope();

    assertThat( usedScopes ).isEmpty();
  }

  @Test
  public void testHasRateLimit() {
    MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
    headers.put( "X-RateLimit-Limit", Lists.newArrayList( "23" ) );
    GitHubResponse<Object> response = new GitHubResponse<>( new Object(), headers );

    int rateLimit = response.getRateLimit();

    assertThat( rateLimit ).isEqualTo( 23 );
  }

  @Test
  public void testHasRateRemaining() {
    MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
    headers.put( "X-RateLimit-Remaining", Lists.newArrayList( "23" ) );
    GitHubResponse<Object> response = new GitHubResponse<>( new Object(), headers );

    int rateRemaining = response.getRateRemaining();

    assertThat( rateRemaining ).isEqualTo( 23 );
  }

  @Test
  public void testHasRateResetSeconds() {
    MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
    headers.put( "X-RateLimit-Reset", Lists.newArrayList( "23" ) );
    GitHubResponse<Object> response = new GitHubResponse<>( new Object(), headers );

    int resetTime = response.getRateResetTimeSeconds();

    assertThat( resetTime ).isEqualTo( 23 );
  }
}
