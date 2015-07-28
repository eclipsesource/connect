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



public class GHNotificationCommitTest {

  private GHNotificationCommit commit;

  @Before
  public void setUp() {
    commit = new Gson().fromJson( new InputStreamReader( GHNotificationTest.class.getResourceAsStream( "/test-notification.json" ) ),
                                GHNotification.class ).getHeadCommit();
  }

  @Test
  public void testHasId() {
    String value = commit.getId();

    assertThat( value ).isEqualTo( "13e1c9b203e8e47c0744b84478911f0c012b90f8" );
  }

  @Test
  public void testHasMessage() {
    String value = commit.getMessage();

    assertThat( value ).isEqualTo( "Update foo.test" );
  }

  @Test
  public void testHasTimestamp() {
    String value = commit.getTimestamp();

    assertThat( value ).isEqualTo( "2014-08-14T10:39:23+02:00" );
  }

  @Test
  public void testHasUrl() {
    String value = commit.getUrl();

    assertThat( value ).isEqualTo( "https://github.com/foo-connect/public-test/commit/13e1c9b203e8e47c0744b84478911f0c012b90f8" );
  }

  @Test
  public void testHasAuthor() {
    GHNotificationUser value = commit.getAuthor();

    assertThat( value.getName() ).isEqualTo( "Mr. Roboto" );
  }

  @Test
  public void testHasCommitter() {
    GHNotificationUser value = commit.getCommitter();

    assertThat( value.getName() ).isEqualTo( "Mr. Roboto" );
  }

  @Test
  public void testHasAdded() {
    List<String> value = commit.getAdded();

    assertThat( value ).isEmpty();
  }

  @Test
  public void testHasRemoved() {
    List<String> value = commit.getAdded();

    assertThat( value ).isEmpty();
  }

  @Test
  public void testHasModified() {
    List<String> value = commit.getModified();

    assertThat( value.get( 0 ) ).isEqualTo( "foo.test" );
  }

  @Test
  public void testHasDistinct() {
    boolean value = commit.isDistinct();

    assertThat( value ).isTrue();
  }
}
