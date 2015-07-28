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



public class GHPermissionTest {

  private GHPermission permission;

  @Before
  public void setUp() {
    permission = new Gson().fromJson( new InputStreamReader( GHUserTest.class.getResourceAsStream( "/test-permission.json" ) ),
                                      GHPermission.class );
  }

  @Test
  public void testHasAdmin() {
    boolean admin = permission.hasAdmin();

    assertThat( admin ).isFalse();
  }

  @Test
  public void testHasPush() {
    boolean push = permission.hasPush();

    assertThat( push ).isFalse();
  }

  @Test
  public void testHasPull() {
    boolean pull = permission.hasPull();

    assertThat( pull ).isTrue();
  }
}
