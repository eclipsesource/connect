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

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveEmptyResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.connect.connector.github.GitHubException;
import com.eclipsesource.connect.connector.github.GitHubParticipant;
import com.eclipsesource.connect.connector.github.GitHubResponse;
import com.eclipsesource.connect.connector.github.model.GHHook;
import com.eclipsesource.connect.connector.github.model.GHHookConfiguration;
import com.eclipsesource.connect.connector.github.model.GHUser;
import com.eclipsesource.connect.test.util.FileHelper;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;
import com.google.common.collect.Lists;
import com.google.gson.Gson;


public class GitHubFactoryTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBaseUrl() {
    new GitHubFactory( null, "token" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyBaseUrl() {
    new GitHubFactory( "", "token" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceBaseUrl() {
    new GitHubFactory( " ", "token" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullToken() {
    new GitHubFactory( "url", null );
  }

  @Test
  public void testSendsGetRequest() {
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-user.json" ), APPLICATION_JSON ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );

    GHUser user = factory.get( "/foo", GHUser.class ).getEntity();

    assertThat( user.getLogin() ).isEqualTo( "octocat" );
  }

  @Test
  public void testSendsGetRequestAndNotifiesParticipants() {
    GitHubParticipant participant = mock( GitHubParticipant.class );
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-user.json" ), APPLICATION_JSON ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo", participant );

    GitHubResponse<GHUser> response = factory.get( "/foo", GHUser.class );

    verify( participant ).participate( response );
  }

  @Test( expected = GitHubException.class )
  public void testSendsGetRequestAndThrowsGitHubException() {
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-user.json" ), APPLICATION_JSON ).withStatus( 401 ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );

    factory.get( "/foo", GHUser.class ).getEntity();
  }

  @Test
  public void testSendsGetRequestWithQueryParameter() {
    driver.addExpectation( onRequestTo( "/foo" )
                           .withMethod( GET )
                           .withParam( "access_token", "foo" )
                           .withParam( "foo", "bar" )
                           .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-user.json" ), APPLICATION_JSON ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );
    Map<String, String> params = new HashMap<>();
    params.put( "foo", "bar" );

    GHUser user = factory.get( "/foo", params, GHUser.class ).getEntity();

    assertThat( user.getLogin() ).isEqualTo( "octocat" );
  }

  @Test( expected = GitHubException.class )
  public void testSendsGetRequestWithQueryParameterAndThrowsGitHubException() {
    driver.addExpectation( onRequestTo( "/foo" )
                           .withMethod( GET )
                           .withParam( "access_token", "foo" )
                           .withParam( "foo", "bar" )
                           .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-user.json" ), APPLICATION_JSON ).withStatus( 401 ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );
    Map<String, String> params = new HashMap<>();
    params.put( "foo", "bar" );

    factory.get( "/foo", params, GHUser.class ).getEntity();
  }

  @Test
  public void testSendsPostRequest() {
    GHHook hook = new GHHook( "foo", new GHHookConfiguration( "foo", "bar" ), Lists.newArrayList( "push" ), true );
    String body = new Gson().toJson( hook );
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( Method.POST )
                             .withParam( "access_token", "foo" )
                             .withBody( body, "application/json" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hook.json" ), APPLICATION_JSON ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );

    GHHook ghHook = factory.post( "/foo", hook, GHHook.class ).getEntity();

    assertThat( ghHook.getId() ).isEqualTo( "1" );
  }

  @Test
  public void testSendsPostRequestAndNotifiesParticipants() {
    GitHubParticipant participant = mock( GitHubParticipant.class );
    GHHook hook = new GHHook( "foo", new GHHookConfiguration( "foo", "bar" ), Lists.newArrayList( "push" ), true );
    String body = new Gson().toJson( hook );
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( Method.POST )
                             .withParam( "access_token", "foo" )
                             .withBody( body, "application/json" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hook.json" ), APPLICATION_JSON ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo", participant );

    GitHubResponse<GHHook> response = factory.post( "/foo", hook, GHHook.class );

    verify( participant ).participate( response );
  }

  @Test( expected = GitHubException.class )
  public void testSendsPostRequestAndThrowsGitHubException() {
    GHHook hook = new GHHook( "foo", new GHHookConfiguration( "foo", "bar" ), Lists.newArrayList( "push" ), true );
    String body = new Gson().toJson( hook );
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( Method.POST )
                             .withParam( "access_token", "foo" )
                             .withBody( body, "application/json" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hook.json" ), APPLICATION_JSON ).withStatus( 401 ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );

    factory.post( "/foo", hook, GHHook.class ).getEntity();
  }

  @Test
  public void testSendsPatchRequest() {
    GHHook hook = new GHHook( "foo", new GHHookConfiguration( "foo", "bar" ), Lists.newArrayList( "push" ), true );
    String body = new Gson().toJson( hook );
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( Method.POST )
                             .withParam( "access_token", "foo" )
                             .withBody( body, "application/json" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hook.json" ), APPLICATION_JSON ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );

    GHHook ghHook = factory.patch( "/foo", hook, GHHook.class ).getEntity();

    assertThat( ghHook.getId() ).isEqualTo( "1" );
  }

  @Test
  public void testSendsPatchRequestAndNotifiesParticipants() {
    GitHubParticipant participant = mock( GitHubParticipant.class );
    GHHook hook = new GHHook( "foo", new GHHookConfiguration( "foo", "bar" ), Lists.newArrayList( "push" ), true );
    String body = new Gson().toJson( hook );
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( Method.POST )
                             .withParam( "access_token", "foo" )
                             .withBody( body, "application/json" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hook.json" ), APPLICATION_JSON ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo", participant );

    GitHubResponse<GHHook> response = factory.patch( "/foo", hook, GHHook.class );

    verify( participant ).participate( response );
  }

  @Test( expected = GitHubException.class )
  public void testSendsPatchRequestAndThrowsGitHubException() {
    GHHook hook = new GHHook( "foo", new GHHookConfiguration( "foo", "bar" ), Lists.newArrayList( "push" ), true );
    String body = new Gson().toJson( hook );
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( Method.POST )
                             .withParam( "access_token", "foo" )
                             .withBody( body, "application/json" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hook.json" ), APPLICATION_JSON ).withStatus( 401 ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );

    factory.patch( "/foo", hook, GHHook.class ).getEntity();
  }

  @Test
  public void testSendsDeleteRequest() {
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( Method.DELETE )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveEmptyResponse() );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );

    factory.delete( "/foo" );
  }

  @Test
  public void testSendsDeleteRequestAndNotifiesParticipants() {
    GitHubParticipant participant = mock( GitHubParticipant.class );
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( Method.DELETE )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveEmptyResponse() );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo", participant );

    factory.delete( "/foo" );

    verify( participant ).participate( any( GitHubResponse.class ) );
  }

  @Test( expected = GitHubException.class )
  public void testSendsDeleteRequestAndThrowsGitHubException() {
    driver.addExpectation( onRequestTo( "/foo" )
                             .withMethod( Method.DELETE )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveEmptyResponse().withStatus( 401 ) );
    GitHubFactory factory = new GitHubFactory( driver.getBaseUrl(), "foo" );

    factory.delete( "/foo" );
  }

  private String read( String fielName ) {
    return FileHelper.readFile( fielName, GitHubFactoryTest.class );
  }
}
