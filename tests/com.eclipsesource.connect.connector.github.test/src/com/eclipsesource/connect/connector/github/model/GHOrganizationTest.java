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


public class GHOrganizationTest {

  private GHOrganization org;

  @Before
  public void setUp() {
    org = new Gson().fromJson( new InputStreamReader( GHUserTest.class.getResourceAsStream( "/test-org.json" ) ),
                               GHOrganization.class );
  }

  @Test
  public void testHasLogin() {
    String login = org.getLogin();

    assertThat( login ).isEqualTo( "github" );
  }

  @Test
  public void testHasId() {
    String id = org.getId();

    assertThat( id ).isEqualTo( "1" );
  }

  @Test
  public void testHasUrl() {
    String url = org.getUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/orgs/github" );
  }

  @Test
  public void testHasAvatarUrl() {
    String url = org.getAvatarUrl();

    assertThat( url ).isEqualTo( "https://github.com/images/error/octocat_happy.gif" );
  }

  @Test
  public void testHasName() {
    String name = org.getName();

    assertThat( name ).isEqualTo( "github" );
  }

  @Test
  public void testHasCompany() {
    String company = org.getCompany();

    assertThat( company ).isEqualTo( "GitHub" );
  }

  @Test
  public void testHasBlog() {
    String blog = org.getBlog();

    assertThat( blog ).isEqualTo( "https://github.com/blog" );
  }

  @Test
  public void testHasLocation() {
    String location = org.getLocation();

    assertThat( location ).isEqualTo( "San Francisco" );
  }

  @Test
  public void testHasEmail() {
    String email = org.getEmail();

    assertThat( email ).isEqualTo( "octocat@github.com" );
  }

  @Test
  public void testHasPublicRepos() {
    int publicRepos = org.getPublicRepos();

    assertThat( publicRepos ).isEqualTo( 2 );
  }

  @Test
  public void testHasPublicGists() {
    int publicGists = org.getPublicGists();

    assertThat( publicGists ).isEqualTo( 1 );
  }

  @Test
  public void testHasFollowers() {
    int count = org.getFollowers();

    assertThat( count ).isEqualTo( 20 );
  }

  @Test
  public void testHasFollowing() {
    int count = org.getFollowing();

    assertThat( count ).isEqualTo( 0 );
  }

  @Test
  public void testHasPrivateRepos() {
    int count = org.getTotalPrivateRepos();

    assertThat( count ).isEqualTo( 100 );
  }

  @Test
  public void testHasOwnedPrivateRepos() {
    int count = org.getOwnedPrivateRepos();

    assertThat( count ).isEqualTo( 100 );
  }

  @Test
  public void testHasPrivateGists() {
    int count = org.getPrivateGists();

    assertThat( count ).isEqualTo( 81 );
  }

  @Test
  public void testHasDiskUsage() {
    int count = org.getDiskUsage();

    assertThat( count ).isEqualTo( 10000 );
  }

  @Test
  public void testHasCollaborators() {
    int count = org.getCollaborators();

    assertThat( count ).isEqualTo( 8 );
  }

  @Test
  public void testHasHtmlUrl() {
    String htmlUrl = org.getHtmlUrl();

    assertThat( htmlUrl ).isEqualTo( "https://github.com/octocat" );
  }

  @Test
  public void testHasCreatedAt() {
    String createdAt = org.getCreatedAt();

    assertThat( createdAt ).isEqualTo( "2008-01-14T04:33:35Z" );
  }

  @Test
  public void testHasType() {
    String type = org.getType();

    assertThat( type ).isEqualTo( "Organization" );
  }

  @Test
  public void testHasBillingEmail() {
    String email = org.getBillingEmail();

    assertThat( email ).isEqualTo( "support@github.com" );
  }

  @Test
  public void testHasPlan() {
    GHPlan plan = org.getPlan();

    assertThat( plan ).isNotNull();
  }
}
