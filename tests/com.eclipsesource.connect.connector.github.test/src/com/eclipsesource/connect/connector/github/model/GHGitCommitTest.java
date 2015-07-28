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


public class GHGitCommitTest {

  private GHGitCommit commit;

  @Before
  public void setUp() {
    commit = new Gson().fromJson( new InputStreamReader( GHCommitTest.class.getResourceAsStream( "/test-git-commit.json" ) ),
                                  GHGitCommit.class );
  }

  @Test
  public void testHasUrl() {
    String value = commit.getUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/git/commits/6dcb09b5b57875f334f61aebed695e2e4193db5e" );
  }

  @Test
  public void testHasAuthor() {
    GHGitCommiter value = commit.getAuthor();

    assertThat( value.getName() ).isEqualTo( "Monalisa Octocat" );
    assertThat( value.getEmail() ).isEqualTo( "support@github.com" );
    assertThat( value.getDate() ).isEqualTo( "2011-04-14T16:00:49Z" );
  }

  @Test
  public void testHasCommitter() {
    GHGitCommiter value = commit.getCommitter();

    assertThat( value.getName() ).isEqualTo( "Monalisa Octocat" );
    assertThat( value.getEmail() ).isEqualTo( "support@github.com" );
    assertThat( value.getDate() ).isEqualTo( "2011-04-14T16:00:49Z" );
  }

  @Test
  public void testHasMessage() {
    String value = commit.getMessage();

    assertThat( value ).isEqualTo( "Fix all the bugs" );
  }

  @Test
  public void testHasTree() {
    GHCommit value = commit.getTree();

    assertThat( value.getSha() ).isEqualTo( "6dcb09b5b57875f334f61aebed695e2e4193db5e" );
  }

  @Test
  public void testHasCommentCount() {
    int value = commit.getCommentCount();

    assertThat( value ).isEqualTo( 0 );
  }

}
