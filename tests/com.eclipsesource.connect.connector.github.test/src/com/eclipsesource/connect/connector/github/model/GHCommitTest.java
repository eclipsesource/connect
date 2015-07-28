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

import com.google.gson.Gson;


public class GHCommitTest {

  private GHCommit commit;

  @Before
  public void setUp() {
    commit = new Gson().fromJson( new InputStreamReader( GHCommitTest.class.getResourceAsStream( "/test-commit.json" ) ),
                                  GHCommit.class );
  }

  @Test
  public void testHasUrl() {
    String value = commit.getUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/commits/6dcb09b5b57875f334f61aebed695e2e4193db5e" );
  }

  @Test
  public void testHasSha() {
    String value = commit.getSha();

    assertThat( value ).isEqualTo( "6dcb09b5b57875f334f61aebed695e2e4193db5e" );
  }

  @Test
  public void testHasHtmlUrl() {
    String value = commit.getHtmlUrl();

    assertThat( value ).isEqualTo( "https://github.com/octocat/Hello-World/commit/6dcb09b5b57875f334f61aebed695e2e4193db5e" );
  }

  @Test
  public void testHasCommentsUrl() {
    String value = commit.getCommentsUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/commits/6dcb09b5b57875f334f61aebed695e2e4193db5e/comments" );
  }

  @Test
  public void testHasGitCommit() {
    GHGitCommit value = commit.getCommit();

    assertThat( value.getMessage() ).isEqualTo( "Fix all the bugs" );
  }

  @Test
  public void testHasAuthor() {
    GHUser value = commit.getAuthor();

    assertThat( value.getLogin() ).isEqualTo( "octocat" );
  }

  @Test
  public void testHasCommitter() {
    GHUser value = commit.getCommitter();

    assertThat( value.getLogin() ).isEqualTo( "octocat" );
  }

  @Test
  public void testHasParents() {
    List<GHCommit> value = commit.getParents();

    assertThat( value.get( 0 ).getSha() ).isEqualTo( "6dcb09b5b57875f334f61aebed695e2e4193db5e" );
  }

  @Test
  public void testHasStats() {
    GHCommitStats value = commit.getStats();

    assertThat( value.getAdditions() ).isEqualTo( 104 );
    assertThat( value.getDeletions() ).isEqualTo( 4 );
    assertThat( value.getTotal() ).isEqualTo( 108 );
  }

  @Test
  public void testHasFiles() {
    List<GHCommitFile> value = commit.getFiles();

    assertThat( value.get( 0 ).getFilename() ).isEqualTo( "file1.txt" );
  }
}
