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
package com.eclipsesource.connect.connector.github.model;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;


public class GHUserTest {

  private GHUser user;

  @Before
  public void setUp() {
    user = new Gson().fromJson( new InputStreamReader( GHUserTest.class.getResourceAsStream( "/test-user.json" ) ),
                                GHUser.class );
  }

  @Test
  public void testHasLogin() {
    String login = user.getLogin();

    assertThat( login ).isEqualTo( "octocat" );
  }

  @Test
  public void testHasId() {
    int id = user.getId();

    assertThat( id ).isEqualTo( 1 );
  }

  @Test
  public void testHasAvatarUrl() {
    String avatar_url = user.getAvatarUrl();

    assertThat( avatar_url ).isEqualTo( "https://github.com/images/error/octocat_happy.gif" );
  }

  @Test
  public void testHasGravatarId() {
    String gravatar_id = user.getGravatarId();

    assertThat( gravatar_id ).isEqualTo( "somehexcode" );
  }

  @Test
  public void testHasUrl() {
    String url = user.getUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat" );
  }

  @Test
  public void testHasHtmlUrl() {
    String url = user.getHtmlUrl();

    assertThat( url ).isEqualTo( "https://github.com/octocat" );
  }

  @Test
  public void testHasFollowersUrl() {
    String url = user.getFollowersUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat/followers" );
  }

  @Test
  public void testHasFollowingUrl() {
    String url = user.getFollowingUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat/following{/other_user}" );
  }

  @Test
  public void testHasGistsUrl() {
    String url = user.getGistsUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat/gists{/gist_id}" );
  }

  @Test
  public void testHasStarredUrl() {
    String url = user.getStarredUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat/starred{/owner}{/repo}" );
  }

  @Test
  public void testHasSubscriptionUrl() {
    String url = user.getSubscriptionsUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat/subscriptions" );
  }

  @Test
  public void testHasOrganizationUrl() {
    String url = user.getOrganizationsUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat/orgs" );
  }

  @Test
  public void testHasReposUrl() {
    String url = user.getReposUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat/repos" );
  }

  @Test
  public void testHasEventsUrl() {
    String url = user.getEventsUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat/events{/privacy}" );
  }

  @Test
  public void testHasReceivedEventsUrl() {
    String url = user.getReceivedEventsUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/users/octocat/received_events" );
  }

  @Test
  public void testHasType() {
    String type = user.getType();

    assertThat( type ).isEqualTo( "User" );
  }

  @Test
  public void testHasSiteAdmin() {
    boolean site_admin = user.isSiteAdmin();

    assertThat( site_admin ).isFalse();
  }

  @Test
  public void testHashirable() {
    boolean isHirable = user.isHireable();

    assertThat( isHirable ).isFalse();
  }

  @Test
  public void testHasName() {
    String name = user.getName();

    assertThat( name ).isEqualTo( "monalisa octocat" );
  }

  @Test
  public void testHasCompany() {
    String company = user.getCompany();

    assertThat( company ).isEqualTo( "GitHub" );
  }

  @Test
  public void testHasBlog() {
    String blog = user.getBlog();

    assertThat( blog ).isEqualTo( "https://github.com/blog" );
  }

  @Test
  public void testHasLocation() {
    String location = user.getLocation();

    assertThat( location ).isEqualTo( "San Francisco" );
  }

  @Test
  public void testHasBio() {
    String bio = user.getBio();

    assertThat( bio ).isEqualTo( "There once was..." );
  }

  @Test
  public void testHasPublicRepos() {
    int repos = user.getPublicRepos();

    assertThat( repos ).isEqualTo( 2 );
  }

  @Test
  public void testHasPublicGists() {
    int gists = user.getPublicGists();

    assertThat( gists ).isEqualTo( 1 );
  }

  @Test
  public void testHasFollowers() {
    int followers = user.getFollowers();

    assertThat( followers ).isEqualTo( 20 );
  }

  @Test
  public void testHasFollowing() {
    int following = user.getFollowing();

    assertThat( following ).isEqualTo( 0 );
  }

  @Test
  public void testHasCreatedAt() {
    String createdAt = user.getCreatedAt();

    assertThat( createdAt ).isEqualTo( "2008-01-14T04:33:35Z" );
  }

  @Test
  public void testHasUpdatedAt() {
    String updatedAt = user.getUpdatedAt();

    assertThat( updatedAt ).isEqualTo( "2008-01-14T04:33:35Z" );
  }
}
