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

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveEmptyResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.connect.connector.github.model.GHCommit;
import com.eclipsesource.connect.connector.github.model.GHEmail;
import com.eclipsesource.connect.connector.github.model.GHHook;
import com.eclipsesource.connect.connector.github.model.GHHookConfiguration;
import com.eclipsesource.connect.connector.github.model.GHIssue;
import com.eclipsesource.connect.connector.github.model.GHIssueFilter;
import com.eclipsesource.connect.connector.github.model.GHMarkdownConfiguration;
import com.eclipsesource.connect.connector.github.model.GHOrganization;
import com.eclipsesource.connect.connector.github.model.GHRate;
import com.eclipsesource.connect.connector.github.model.GHRepository;
import com.eclipsesource.connect.connector.github.model.GHRepositoryFilter;
import com.eclipsesource.connect.connector.github.model.GHRepositoryFilter.Type;
import com.eclipsesource.connect.connector.github.model.GHUser;
import com.eclipsesource.connect.test.util.FileHelper;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;
import com.google.common.collect.Lists;
import com.google.gson.Gson;


public class GitHubTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutAccessToken() {
    new GitHub( ( String )null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBaseUrl() {
    new GitHub( "foo", ( String )null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyBaseUrl() {
    new GitHub( "foo", "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceBaseUrl() {
    new GitHub( "foo", " " );
  }

  @Test
  public void testHasBaseUrl() {
    assertThat( GitHub.API_URL ).isEqualTo( "https://api.github.com" );
  }

  @Test
  public void testFetchesUser() {
    driver.addExpectation( onRequestTo( "/user" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-user.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );

    GHUser userData = gitHub.getUser();

    assertThat( userData.getLogin() ).isEqualTo( "octocat" );
  }

  @Test
  public void testFetchesUserEmails() {
    driver.addExpectation( onRequestTo( "/user/emails" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-emails.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );

    List<GHEmail> emails = gitHub.getEmails();

    assertThat( emails ).hasSize( 2 );
    assertThat( emails.get( 0 ).getEmail() ).isEqualTo( "octocat@github.com" );
  }

  @Test
  public void testFetchesRate() {
    driver.addExpectation( onRequestTo( "/rate_limit" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-rate.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );

    GHRate rate = gitHub.getRate();

    assertThat( rate.getCoreLimit() ).isEqualTo( 5000 );
  }

  @Test
  public void testFetchesOrganiations() {
    driver.addExpectation( onRequestTo( "/user/orgs" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withParam( "per_page", "100" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-organizations.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );

    GitHubResponse<GHOrganization[]> organizations = gitHub.getOrganizations();

    assertThat( organizations.getEntity() ).hasSize( 2 );
    assertThat( organizations.getEntity()[ 0 ].getId() ).isEqualTo( "1" );
    assertThat( organizations.getEntity()[ 1 ].getId() ).isEqualTo( "2" );
  }

  @Test
  public void testFetchesOrganiation() {
    driver.addExpectation( onRequestTo( "/orgs/foo" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-org.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );

    GitHubResponse<GHOrganization> organizations = gitHub.getOrganization( "foo" );

    assertThat( organizations.getEntity().getId() ).isEqualTo( "1" );
  }

  @Test
  public void testFetchesUserRepositories() {
    driver.addExpectation( onRequestTo( "/user/repos" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withParam( "per_page", "15" )
                             .withParam( "page", "2" )
                             .withParam( "type", "owner" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-repos.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );

    GitHubResponse<GHRepository[]> repositories = gitHub.getRepositories( new GHRepositoryFilter().setType( Type.OWNER ), 2 );

    assertThat( repositories.getEntity() ).hasSize( 1 );
    assertThat( repositories.getEntity()[ 0 ].getId() ).isEqualTo( "1296269" );
  }

  @Test
  public void testFetchesSingleRepository() {
    driver.addExpectation( onRequestTo( "/repos/foo/bar" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-repo.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );

    GitHubResponse<GHRepository> response = gitHub.getRepository( "foo", "bar" );

    assertThat( response.getEntity().getId() ).isEqualTo( "1296269" );
  }

  @Test
  public void testFetchesOrganizationRepositories() {
    driver.addExpectation( onRequestTo( "/orgs/foo/repos" )
                             .withMethod( GET )
                             .withParam( "access_token", "foo" )
                             .withParam( "per_page", "15" )
                             .withParam( "page", "2" )
                             .withParam( "type", "owner" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-repos.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );
    GHOrganization organization = mock( GHOrganization.class );
    when( organization.getLogin() ).thenReturn( "foo" );

    GitHubResponse<GHRepository[]> repositories = gitHub.getRepositories( organization, new GHRepositoryFilter().setType( Type.OWNER ), 2 );

    assertThat( repositories.getEntity() ).hasSize( 1 );
    assertThat( repositories.getEntity()[ 0 ].getId() ).isEqualTo( "1296269" );
  }

  @Test
  public void testCreatesWebHook() {
    driver.addExpectation( onRequestTo( "/repos/octocat/Hello-World/hooks" )
                             .withMethod( Method.POST )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hook.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );
    GHHook hook = new GHHook( "foo", new GHHookConfiguration( "foo", "bar" ), Lists.newArrayList( "push" ), true );
    GHRepository repository = new Gson().fromJson( read( "/test-repo.json" ), GHRepository.class );

    GitHubResponse<GHHook> response = gitHub.createWebHook( hook, repository );

    assertThat( response.getEntity().getId() ).isEqualTo( "1" );
  }

  @Test
  public void testCreatesWebHookUsesExistingHooksIfPresent() {
    driver.addExpectation( onRequestTo( "/repos/octocat/Hello-World/hooks" )
                             .withMethod( Method.POST )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-error.json" ), APPLICATION_JSON ) );
    driver.addExpectation( onRequestTo( "/repos/octocat/Hello-World/hooks" )
                             .withMethod( Method.GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hooks.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );
    GHHook hook = new GHHook( "foo", new GHHookConfiguration( "http://example.com/foo", "bar" ), Lists.newArrayList( "push" ), true );
    GHRepository repository = new Gson().fromJson( read( "/test-repo.json" ), GHRepository.class );

    GitHubResponse<GHHook> response = gitHub.createWebHook( hook, repository );

    assertThat( response.getEntity().getId() ).isEqualTo( "2" );
  }

  @Test
  public void testUpdatesWebHook() {
    driver.addExpectation( onRequestTo( "/repos/octocat/Hello-World/hooks/1" )
                             .withMethod( Method.POST )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hook.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );
    GHHook hook = new Gson().fromJson( read( "/test-hook.json" ), GHHook.class );
    GHRepository repository = new Gson().fromJson( read( "/test-repo.json" ), GHRepository.class );

    GitHubResponse<GHHook> response = gitHub.updateWebHook( repository, hook );

    assertThat( response.getEntity().getId() ).isEqualTo( "1" );
  }

  @Test
  public void testFetchesWebHook() {
    driver.addExpectation( onRequestTo( "/repos/octocat/Hello-World/hooks/1" )
                             .withMethod( Method.GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-hook.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );
    GHRepository repository = new Gson().fromJson( read( "/test-repo.json" ), GHRepository.class );

    GitHubResponse<GHHook> response = gitHub.getWebHook( repository, "1" );

    assertThat( response.getEntity().getId() ).isEqualTo( "1" );
  }

  @Test
  public void testDeletesWebHook() {
    driver.addExpectation( onRequestTo( "/repos/octocat/Hello-World/hooks/1" )
                             .withMethod( Method.DELETE )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveEmptyResponse() );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );
    GHHook hook = new Gson().fromJson( read( "/test-hook.json" ), GHHook.class );
    GHRepository repository = new Gson().fromJson( read( "/test-repo.json" ), GHRepository.class );

    gitHub.deleteWebHook( repository, hook );
  }

  @Test
  public void testFetchesRepositoryIssues() {
    driver.addExpectation( onRequestTo( "/repos/octocat/Hello-World/issues" )
                             .withMethod( Method.GET )
                             .withParam( "access_token", "foo" )
                             .withParam( "milestone", "m1" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-issues.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );
    GHRepository repository = new Gson().fromJson( read( "/test-repo.json" ), GHRepository.class );

    GitHubResponse<GHIssue[]> response = gitHub.getRepositoryIssues( repository, new GHIssueFilter().setMilestone( "m1" ) );

    assertThat( response.getEntity()[ 0 ].getNumber() ).isEqualTo( "1347" );
    assertThat( response.getEntity()[ 1 ].getNumber() ).isEqualTo( "1348" );
  }

  @Test
  public void testFetchesRepositoryCommits() {
    driver.addExpectation( onRequestTo( "/repos/octocat/Hello-World/commits" )
                             .withMethod( Method.GET )
                             .withParam( "access_token", "foo" )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( read( "/test-commits.json" ), APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "foo", driver.getBaseUrl() );
    GHRepository repository = new Gson().fromJson( read( "/test-repo.json" ), GHRepository.class );

    GitHubResponse<GHCommit[]> response = gitHub.getRepositoryCommits( repository );

    assertThat( response.getEntity()[ 0 ].getSha() ).isEqualTo( "6dcb09b5b57875f334f61aebed695e2e4193db5e" );
    assertThat( response.getEntity()[ 1 ].getSha() ).isEqualTo( "6dcb09b5b57875f334f61aebed695e2e4193db5a" );
  }

  @Test
  public void testFetchesMarkdwon() {
    driver.addExpectation( onRequestTo( "/markdown" )
                             .withMethod( Method.POST )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( "foo", APPLICATION_JSON ) );
    GitHub gitHub = new GitHub( "", driver.getBaseUrl() );

    GitHubResponse<String> response = gitHub.getMarkdown( new GHMarkdownConfiguration( "foo" ) );

    assertThat( response.getEntity() ).isEqualTo( "foo" );
  }

  private String read( String fielName ) {
    return FileHelper.readFile( fielName, GitHubTest.class );
  }

}
