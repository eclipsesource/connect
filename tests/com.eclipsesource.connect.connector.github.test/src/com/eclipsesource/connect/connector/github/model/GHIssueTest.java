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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.connector.github.model.GHIssue.State;
import com.google.gson.Gson;


public class GHIssueTest {

  private GHIssue issue;

  @Before
  public void setUp() {
    issue = new Gson().fromJson( new InputStreamReader( GHIssueTest.class.getResourceAsStream( "/test-issue.json" ) ),
                                 GHIssue.class );
  }

  @Test
  public void testHasUrl() {
    String value = issue.getUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/issues/1347" );
  }

  @Test
  public void testHasHtmlUrl() {
    String value = issue.getHtmlUrl();

    assertThat( value ).isEqualTo( "https://github.com/octocat/Hello-World/issues/1347" );
  }

  @Test
  public void testHasNumber() {
    String value = issue.getNumber();

    assertThat( value ).isEqualTo( "1347" );
  }

  @Test
  public void testHasState() {
    State value = issue.getState();

    assertThat( value ).isSameAs( State.OPEN );
  }

  @Test
  public void testHasTitle() {
    String value = issue.getTitle();

    assertThat( value ).isEqualTo( "Found a bug" );
  }

  @Test
  public void testHasBody() {
    String value = issue.getBody();

    assertThat( value ).isEqualTo( "I'm having a problem with this." );
  }

  @Test
  public void testHasUser() {
    GHUser value = issue.getAssignee();

    assertThat( value.getLogin() ).isEqualTo( "octocat" );
  }

  @Test
  public void testHasAssignee() {
    GHUser value = issue.getUser();

    assertThat( value.getLogin() ).isEqualTo( "octocat" );
  }

  @Test
  public void testHasLabels() {
    List<GHIssueLabel> value = issue.getLabels();

    assertThat( value ).hasSize( 1 );
    assertThat( value.get( 0 ).getName() ).isEqualTo( "bug" );
  }

  @Test
  public void testHasMilestone() {
    GHIssueMilestone value = issue.getMilestone();

    assertThat( value.getNumber() ).isEqualTo( "1" );
  }

  @Test
  public void testHasClosedAt() {
    String value = issue.getClosedAt();

    assertThat( value ).isNull();
  }

  @Test
  public void testHasCreatedAt() {
    String value = issue.getCreatedAt();

    assertThat( value ).isEqualTo( "2011-04-22T13:33:48Z" );
  }

  @Test
  public void testHasUpdatedAt() {
    String value = issue.getUpdatedAt();

    assertThat( value ).isEqualTo( "2011-04-22T13:33:48Z" );
  }

  @Test
  public void testHasComments() {
    int value = issue.getComments();

    assertThat( value ).isEqualTo( 0 );
  }

  @Test
  public void testHasPullRequest() {
    GHPullRequest value = issue.getPullRequest();

    assertThat( value.getUrl() ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/pulls/1347" );
  }
}
