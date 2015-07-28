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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.security.TokenAdmin;
import com.eclipsesource.connect.model.User;
import com.eclipsesource.connect.security.test.TokenTestUtil;


public class SecurityHandlerTest {

  private SecurityHandler securityHandler;

  @Before
  public void setUp() {
    securityHandler = new SecurityHandler();
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutTokenAdmin() {
    securityHandler.authenticate( mock( ContainerRequestContext.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTokenAdmin() {
    securityHandler.setTokenAdmin( null );
  }

  @Test
  public void testAuthenticateReturnsPrincipalUser() {
    NewCookie cookie = new NewCookie( "token", "foo" );
    ContainerRequestContext context = TokenTestUtil.createContext( cookie );
    User user = new User( "foo", "bar" );
    securityHandler.setTokenAdmin( createTokenAdmin( user ) );

    Principal principal = securityHandler.authenticate( context );

    assertThat( principal ).isInstanceOf( PrincipalUser.class );
    assertThat( user ).isSameAs( ( ( PrincipalUser )principal ).getUser() );
  }

  @Test
  public void testAuthenticateReturnsNullWhenNoTokenAvailable() {
    NewCookie cookie = new NewCookie( "bar", "foo" );
    ContainerRequestContext context = TokenTestUtil.createContext( cookie );
    UriInfo uriInfo = mock( UriInfo.class );
    MultivaluedHashMap<String, String> parameters = new MultivaluedHashMap<>();
    when( uriInfo.getQueryParameters() ).thenReturn( parameters );
    when( context.getUriInfo() ).thenReturn( uriInfo );
    User user = new User( "foo", "bar" );
    securityHandler.setTokenAdmin( createTokenAdmin( user ) );

    Principal principal = securityHandler.authenticate( context );

    assertThat( principal ).isNull();
  }

  @Test
  public void testAuthenticationSchemeIsNull() {
    String scheme = securityHandler.getAuthenticationScheme();

    assertThat( scheme ).isNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testInRoleFailsWithWrongPrincipalType() {
    securityHandler.isUserInRole( mock( Principal.class ), "foo" );
  }

  @Test
  public void testInRoleUsesUserRoles() {
    User user = new User( "foo", "bar" );
    user.addRoles( "foo" );

    boolean isInRole = securityHandler.isUserInRole( new PrincipalUser( user ), "foo" );

    assertThat( isInRole ).isTrue();
  }

  @Test
  public void testIsNotInRoleWhenUserHasNotRightRoles() {
    User user = new User( "foo", "bar" );
    user.addRoles( "foo2" );

    boolean isInRole = securityHandler.isUserInRole( new PrincipalUser( user ), "foo" );

    assertThat( isInRole ).isFalse();
  }

  private TokenAdmin createTokenAdmin( User user ) {
    TokenAdmin tokenAdmin = mock( TokenAdmin.class );
    when( tokenAdmin.get( anyString() ) ).thenReturn( user );
    return tokenAdmin;
  }
}
