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


public class GHPullRequestTest {

  private GHPullRequest request;

  @Before
  public void setUp() {
    request = new Gson().fromJson( new InputStreamReader( GHPullRequestTest.class.getResourceAsStream( "/test-pull-request.json" ) ),
                                   GHPullRequest.class );
  }

  @Test
  public void testHasUrl() {
    String value = request.getUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/pulls/1" );
  }

  @Test
  public void testHasHtmlUrl() {
    String value = request.getHtmlUrl();

    assertThat( value ).isEqualTo( "https://github.com/octocat/Hello-World/pull/1" );
  }

  @Test
  public void testHasDiffUrl() {
    String value = request.getDiffUrl();

    assertThat( value ).isEqualTo( "https://github.com/octocat/Hello-World/pulls/1.diff" );
  }

  @Test
  public void testHasPatchUrl() {
    String value = request.getPatchUrl();

    assertThat( value ).isEqualTo( "https://github.com/octocat/Hello-World/pulls/1.patch" );
  }

  @Test
  public void testHasIssueUrl() {
    String value = request.getIssueUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/issues/1" );
  }

  @Test
  public void testHasCommitsUrl() {
    String value = request.getCommitsUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/pulls/1/commits" );
  }

  @Test
  public void testHasReviewCommentsUrl() {
    String value = request.getReviewCommentsUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/pulls/1/comments" );
  }

  @Test
  public void testHasReviewCommentUrl() {
    String value = request.getReviewCommentUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/pulls/comments/{number}" );
  }

  @Test
  public void testHasCommentsUrl() {
    String value = request.getCommentsUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/issues/1/comments" );
  }

  @Test
  public void testHasStatusesUrl() {
    String value = request.getStatusesUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/statuses/6dcb09b5b57875f334f61aebed695e2e4193db5e" );
  }

  @Test
  public void testHasNumber() {
    String value = request.getNumber();

    assertThat( value ).isEqualTo( "1" );
  }

  @Test
  public void testHasState() {
    String value = request.getState();

    assertThat( value ).isEqualTo( "open" );
  }

  @Test
  public void testHasTitle() {
    String value = request.getTitle();

    assertThat( value ).isEqualTo( "new-feature" );
  }

  @Test
  public void testHasBody() {
    String value = request.getBody();

    assertThat( value ).isEqualTo( "Please pull these awesome changes" );
  }

  @Test
  public void testHasCreatedAt() {
    String value = request.getCreatedAt();

    assertThat( value ).isEqualTo( "2011-01-26T19:01:12Z" );
  }

  @Test
  public void testHasUpdatedAt() {
    String value = request.getUpdatedAt();

    assertThat( value ).isEqualTo( "2011-01-26T19:01:12Z" );
  }

  @Test
  public void testHasClosedAt() {
    String value = request.getClosedAt();

    assertThat( value ).isEqualTo( "2011-01-26T19:01:12Z" );
  }

  @Test
  public void testHasMergedAt() {
    String value = request.getMergedAt();

    assertThat( value ).isEqualTo( "2011-01-26T19:01:12Z" );
  }

  @Test
  public void testHasMergeCommitSha() {
    String value = request.getMergeCommitSha();

    assertThat( value ).isEqualTo( "e5bd3914e2e596debea16f433f57875b5b90bcd6" );
  }

  @Test
  public void testHasMerged() {
    boolean value = request.isMerged();

    assertThat( value ).isFalse();
  }

  @Test
  public void testHasMergable() {
    boolean value = request.isMergeable();

    assertThat( value ).isTrue();
  }

  @Test
  public void testHasComments() {
    int value = request.getComments();

    assertThat( value ).isEqualTo( 10 );
  }

  @Test
  public void testHasCommits() {
    int value = request.getCommits();

    assertThat( value ).isEqualTo( 3 );
  }

  @Test
  public void testHasAdditions() {
    int value = request.getAdditions();

    assertThat( value ).isEqualTo( 100 );
  }

  @Test
  public void testHasDeletions() {
    int value = request.getDeletions();

    assertThat( value ).isEqualTo( 3 );
  }

  @Test
  public void testHasChangedFiles() {
    int value = request.getChangedFiles();

    assertThat( value ).isEqualTo( 5 );
  }

  @Test
  public void testHasUser() {
    GHUser value = request.getUser();

    assertThat( value.getLogin() ).isEqualTo( "octocat" );
  }

  @Test
  public void testHasMergedBy() {
    GHUser value = request.getMergedBy();

    assertThat( value.getLogin() ).isEqualTo( "octocat" );
  }

}
