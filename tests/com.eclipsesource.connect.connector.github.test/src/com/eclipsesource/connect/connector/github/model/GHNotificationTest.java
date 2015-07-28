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



public class GHNotificationTest {

  private GHNotification notification;

  @Before
  public void setUp() {
    notification = new Gson().fromJson( new InputStreamReader( GHNotificationTest.class.getResourceAsStream( "/test-notification.json" ) ),
                                GHNotification.class );
  }

  @Test
  public void testHasRef() {
    String value = notification.getRef();

    assertThat( value ).isEqualTo( "refs/heads/master" );
  }

  @Test
  public void testHasAfter() {
    String value = notification.getAfter();

    assertThat( value ).isEqualTo( "13e1c9b203e8e47c0744b84478911f0c012b90f8" );
  }

  @Test
  public void testHasBefore() {
    String value = notification.getBefore();

    assertThat( value ).isEqualTo( "36168c206608f0873abdb7da14492ea7972a9433" );
  }

  @Test
  public void testHasCompare() {
    String value = notification.getCompare();

    assertThat( value ).isEqualTo( "https://github.com/foo-connect/public-test/compare/36168c206608...13e1c9b203e8" );
  }

  @Test
  public void testHasCommits() {
    List<GHNotificationCommit> value = notification.getCommits();

    assertThat( value ).hasSize( 1 );
  }

  @Test
  public void testHasHeadCommit() {
    GHNotificationCommit value = notification.getHeadCommit();

    assertThat( value ).isNotNull();
  }

  @Test
  public void testHasRepository() {
    GHNotificationRepository value = notification.getRepository();

    assertThat( value.getId() ).isEqualTo( "21727707" );
  }

  @Test
  public void testHasPusher() {
    GHUser value = notification.getPusher();

    assertThat( value.getName() ).isEqualTo( "foo-connect" );
  }

  @Test
  public void testHasCreated() {
    boolean value = notification.isCreated();

    assertThat( value ).isFalse();
  }

  @Test
  public void testHasDeleted() {
    boolean value = notification.isDeleted();

    assertThat( value ).isFalse();
  }

  @Test
  public void testHasForces() {
    boolean value = notification.isForced();

    assertThat( value ).isFalse();
  }
}
