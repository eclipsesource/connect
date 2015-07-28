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
package com.eclipsesource.connect.security.injection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.SecurityContext;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.inject.Resolver;
import com.eclipsesource.connect.model.User;
import com.eclipsesource.connect.security.authentication.PrincipalUser;


public class UserContextProviderTest {

  private UserContextProvider provider;

  @Before
  public void setUp() {
    provider = new UserContextProvider();
  }

  @Test
  public void testHasUserType() {
    Class<User> type = provider.getType();

    assertThat( type ).isSameAs( User.class );
  }

  @Test
  public void testResolvesUser() {
    User user = new User( "foo", "bar" );
    SecurityContext context = mock( SecurityContext.class );
    when( context.getUserPrincipal() ).thenReturn( new PrincipalUser( user ) );
    Resolver resolver = mock( Resolver.class );
    when( resolver.resolve( SecurityContext.class ) ).thenReturn( context );

    User actualUser = provider.provide( resolver );

    assertThat( actualUser ).isSameAs( user );
  }

  @Test
  public void testResolvesNoUserOfNotExistent() {
    SecurityContext context = mock( SecurityContext.class );
    Resolver resolver = mock( Resolver.class );
    when( resolver.resolve( SecurityContext.class ) ).thenReturn( context );

    User actualUser = provider.provide( resolver );

    assertThat( actualUser ).isNull();
  }
}
