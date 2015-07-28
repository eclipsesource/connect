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


public class GHCommitFileTest {

  private GHCommitFile file;

  @Before
  public void setUp() {
    file = new Gson().fromJson( new InputStreamReader( GHCommitFileTest.class.getResourceAsStream( "/test-commit-file.json" ) ),
                                GHCommitFile.class );
  }

  @Test
  public void testHasFilename() {
    String value = file.getFilename();

    assertThat( value ).isEqualTo( "file1.txt" );
  }

  @Test
  public void testHasAdditions() {
    int value = file.getAdditions();

    assertThat( value ).isEqualTo( 10 );
  }

  @Test
  public void testHasDeletions() {
    int value = file.getDeletions();

    assertThat( value ).isEqualTo( 2 );
  }

  @Test
  public void testHasChanges() {
    int value = file.getChanges();

    assertThat( value ).isEqualTo( 12 );
  }

  @Test
  public void testHasStatus() {
    String value = file.getStatus();

    assertThat( value ).isEqualTo( "modified" );
  }

  @Test
  public void testHasRawUrl() {
    String value = file.getRawUrl();

    assertThat( value ).isEqualTo( "https://github.com/octocat/Hello-World/raw/7ca483543807a51b6079e54ac4cc392bc29ae284/file1.txt" );
  }

  @Test
  public void testHasBlobUrl() {
    String value = file.getBlobUrl();

    assertThat( value ).isEqualTo( "https://github.com/octocat/Hello-World/blob/7ca483543807a51b6079e54ac4cc392bc29ae284/file1.txt" );
  }

  @Test
  public void testHasPatch() {
    String value = file.getPatch();

    assertThat( value ).isEqualTo( "@@ -29,7 +29,7 @@\n....." );
  }
}
