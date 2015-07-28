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
package com.eclipsesource.connect.security.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.eclipsesource.connect.model.User;


public class PrincipalUserTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUser() {
    new PrincipalUser( null );
  }

  @Test
  public void testHasUser() {
    User user = new User( "foo", "bar" );
    PrincipalUser principalUser = new PrincipalUser( user );

    User actualUser = principalUser.getUser();

    assertThat( user ).isSameAs( actualUser );
  }

  @Test
  public void testUsesUserName() {
    User user = new User( "foo", "bar" );
    PrincipalUser principalUser = new PrincipalUser( user );

    String name = principalUser.getName();

    assertThat( name ).isSameAs( "foo" );
  }
}
