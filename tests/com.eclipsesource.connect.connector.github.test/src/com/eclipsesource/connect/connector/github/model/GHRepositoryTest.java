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


public class GHRepositoryTest {

  private GHRepository repo;

  @Before
  public void setUp() {
    repo = new Gson().fromJson( new InputStreamReader( GHUserTest.class.getResourceAsStream( "/test-repo.json" ) ),
                                GHRepository.class );
  }

  @Test
  public void testHasId() {
    String id = repo.getId();

    assertThat( id ).isEqualTo( "1296269" );
  }

  @Test
  public void testHasOwner() {
    GHUser owner = repo.getOwner();
    String login = owner.getLogin();

    assertThat( login ).isEqualTo( "octocat" );
  }

  @Test
  public void testName() {
    String name = repo.getName();

    assertThat( name ).isEqualTo( "Hello-World" );
  }

  @Test
  public void testFullName() {
    String name = repo.getFullName();

    assertThat( name ).isEqualTo( "octocat/Hello-World" );
  }

  @Test
  public void testHasFullDescription() {
    String description = repo.getDescription();

    assertThat( description ).isEqualTo( "This your first repo!" );
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

    assertThat( url ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World" );
  }

  @Test
  public void testHasHtmlUrl() {
    String url = repo.getHtmlUrl();

    assertThat( url ).isEqualTo( "https://github.com/octocat/Hello-World" );
  }

  @Test
  public void testHasCloneUrl() {
    String url = repo.getCloneUrl();

    assertThat( url ).isEqualTo( "https://github.com/octocat/Hello-World.git" );
  }

  @Test
  public void testHasGitUrl() {
    String url = repo.getGitUrl();

    assertThat( url ).isEqualTo( "git://github.com/octocat/Hello-World.git" );
  }

  @Test
  public void testHasSSHUrl() {
    String url = repo.getSshUrl();

    assertThat( url ).isEqualTo( "git@github.com:octocat/Hello-World.git" );
  }

  @Test
  public void testHasSvnUrl() {
    String url = repo.getSvnUrl();

    assertThat( url ).isEqualTo( "https://svn.github.com/octocat/Hello-World" );
  }

  @Test
  public void testHasMirrorUrl() {
    String url = repo.getMirrorUrl();

    assertThat( url ).isEqualTo( "git://git.example.com/octocat/Hello-World" );
  }

  @Test
  public void testHasHomepage() {
    String url = repo.getHomepage();

    assertThat( url ).isEqualTo( "https://github.com" );
  }

  @Test
  public void testHasLanguage() {
    String language = repo.getLanguage();

    assertThat( language ).isEqualTo( "german" );
  }

  @Test
  public void testHasForkCount() {
    int count = repo.getForksCount();

    assertThat( count ).isEqualTo( 9 );
  }

  @Test
  public void testHasStargazersCount() {
    int count = repo.getStargazersCount();

    assertThat( count ).isEqualTo( 80 );
  }

  @Test
  public void testHasWatchersCount() {
    int count = repo.getWatchersCount();

    assertThat( count ).isEqualTo( 80 );
  }

  @Test
  public void testHasSize() {
    int size = repo.getSize();

    assertThat( size ).isEqualTo( 108 );
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

    assertThat( publishedAt ).isEqualTo( "2011-01-26T19:06:43Z" );
  }

  @Test
  public void testHasCreatedAt() {
    String createdAt = repo.getCreatedAt();

    assertThat( createdAt ).isEqualTo( "2011-01-26T19:01:12Z" );
  }

  @Test
  public void testHasUpdatedAt() {
    String updatedAt = repo.getUpdatedAt();

    assertThat( updatedAt ).isEqualTo( "2011-01-26T19:14:43Z" );
  }

  @Test
  public void testHasPermissions() {
    GHPermission permissions = repo.getPermissions();

    assertThat( permissions ).isNotNull();
  }

  @Test
  public void testHasSubscriberCount() {
    int count = repo.getSubscribersCount();

    assertThat( count ).isEqualTo( 42 );
  }

  @Test
  public void testHasOrganization() {
    GHOrganization organization = repo.getOrganization();
    String login = organization.getLogin();

    assertThat( login ).isEqualTo( "octocat" );
  }

  @Test
  public void testHasParent() {
    GHRepository parent = repo.getParent();
    String id = parent.getId();

    assertThat( id ).isEqualTo( "1296269" );
  }

  @Test
  public void testHasSource() {
    GHRepository parent = repo.getSource();
    String id = parent.getId();

    assertThat( id ).isEqualTo( "1296269" );
  }

  @Test
  public void testHasDefaultBranch() {
    String defaultBranch = repo.getDefaultBranch();

    assertThat( defaultBranch ).isEqualTo( "master" );
  }
}
