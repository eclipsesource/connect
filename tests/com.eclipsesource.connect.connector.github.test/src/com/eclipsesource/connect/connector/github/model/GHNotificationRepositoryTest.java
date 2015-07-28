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


public class GHNotificationRepositoryTest {

  private GHNotificationRepository repo;

  @Before
  public void setUp() {
    repo = new Gson().fromJson( new InputStreamReader( GHUserTest.class.getResourceAsStream( "/test-notification-org.json" ) ),
                                GHNotification.class ).getRepository();
  }

  @Test
  public void testHasId() {
    String id = repo.getId();

    assertThat( id ).isEqualTo( "23107533" );
  }

  @Test
  public void testHasOwner() {
    GHNotificationUser owner = repo.getOwner();
    String name = owner.getName();

    assertThat( name ).isEqualTo( "foo-connect-org" );
  }

  @Test
  public void testName() {
    String name = repo.getName();

    assertThat( name ).isEqualTo( "public-test" );
  }

  @Test
  public void testFullName() {
    String name = repo.getFullName();

    assertThat( name ).isEqualTo( "foo-connect-org/public-test" );
  }

  @Test
  public void testHasFullDescription() {
    String description = repo.getDescription();

    assertThat( description ).isEqualTo( "" );
  }

  @Test
  public void testHasPrivate() {
    boolean isPrivate = repo.isPrivate();

    assertThat( isPrivate ).isFalse();
  }

  @Test
  public void testHasFork() {
    boolean isFork = repo.isFork();

    assertThat( isFork ).isFalse();
  }

  @Test
  public void testHasUrl() {
    String url = repo.getUrl();

    assertThat( url ).isEqualTo( "https://github.com/foo-connect-org/public-test" );
  }

  @Test
  public void testHasHtmlUrl() {
    String url = repo.getHtmlUrl();

    assertThat( url ).isEqualTo( "https://github.com/foo-connect-org/public-test" );
  }

  @Test
  public void testHasCloneUrl() {
    String url = repo.getCloneUrl();

    assertThat( url ).isEqualTo( "https://github.com/foo-connect-org/public-test.git" );
  }

  @Test
  public void testHasGitUrl() {
    String url = repo.getGitUrl();

    assertThat( url ).isEqualTo( "git://github.com/foo-connect-org/public-test.git" );
  }

  @Test
  public void testHasSSHUrl() {
    String url = repo.getSshUrl();

    assertThat( url ).isEqualTo( "git@github.com:foo-connect-org/public-test.git" );
  }

  @Test
  public void testHasSvnUrl() {
    String url = repo.getSvnUrl();

    assertThat( url ).isEqualTo( "https://github.com/foo-connect-org/public-test" );
  }

  @Test
  public void testHasHomepage() {
    String url = repo.getHomepage();

    assertThat( url ).isEqualTo( null );
  }

  @Test
  public void testHasForkCount() {
    int count = repo.getForksCount();

    assertThat( count ).isEqualTo( 0 );
  }

  @Test
  public void testHasStargazersCount() {
    int count = repo.getStargazersCount();

    assertThat( count ).isEqualTo( 0 );
  }

  @Test
  public void testHasWatchersCount() {
    int count = repo.getWatchersCount();

    assertThat( count ).isEqualTo( 0 );
  }

  @Test
  public void testHasSize() {
    int size = repo.getSize();

    assertThat( size ).isEqualTo( 0 );
  }

  @Test
  public void testHasOpenIssuesCount() {
    int count = repo.getOpenIssuesCount();

    assertThat( count ).isEqualTo( 0 );
  }

  @Test
  public void testHasIssues() {
    boolean hasIssues = repo.hasIssues();

    assertThat( hasIssues ).isTrue();
  }

  @Test
  public void testHasWiki() {
    boolean hasWiki = repo.hasWiki();

    assertThat( hasWiki ).isTrue();
  }

  @Test
  public void testHasDownloads() {
    boolean hasDownloads = repo.hasDownloads();

    assertThat( hasDownloads ).isTrue();
  }

  @Test
  public void testHasPublishedAt() {
    String publishedAt = repo.getPushedAt();

    assertThat( publishedAt ).isEqualTo( "1408453937" );
  }

  @Test
  public void testHasCreatedAt() {
    String createdAt = repo.getCreatedAt();

    assertThat( createdAt ).isEqualTo( "1408447383" );
  }

  @Test
  public void testHasUpdatedAt() {
    String updatedAt = repo.getUpdatedAt();

    assertThat( updatedAt ).isEqualTo( "2014-08-19T11:23:03Z" );
  }

  @Test
  public void testHasSubscriberCount() {
    int count = repo.getSubscribersCount();

    assertThat( count ).isEqualTo( 0 );
  }

  @Test
  public void testHasOrganization() {
    String organization = repo.getOrganization();

    assertThat( organization ).isEqualTo( "foo-connect-org" );
  }


  @Test
  public void testHasDefaultBranch() {
    String defaultBranch = repo.getDefaultBranch();

    assertThat( defaultBranch ).isEqualTo( "master" );
  }
}
