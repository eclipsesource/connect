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


public class GHNotificationUserTest {

  private GHNotificationUser user;

  @Before
  public void setUp() {
    user = new Gson().fromJson( new InputStreamReader( GHNotificationTest.class.getResourceAsStream( "/test-notification.json" ) ),
                                GHNotification.class ).getHeadCommit().getAuthor();
  }

  @Test
  public void testHasName() {
    String value = user.getName();

    assertThat( value ).isEqualTo( "Mr. Roboto" );
  }

  @Test
  public void testHasEmail() {
    String value = user.getEmail();

    assertThat( value ).isEqualTo( "hstaudacher+foo-connect@eclipsesource.com" );
  }

  @Test
  public void testHasUserName() {
    String value = user.getUsername();

    assertThat( value ).isEqualTo( "foo-connect" );
  }
}
