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
package com.eclipsesource.connect.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;

import com.eclipsesource.connect.api.security.Token;
import com.eclipsesource.connect.security.test.TokenTestUtil;
import com.google.common.collect.Lists;


public class TokenUtilTest {

  @Test
  public void testFindsTokenInCookie() {
    NewCookie cookie = new NewCookie( "token", "foo" );
    ContainerRequestContext context = TokenTestUtil.createContext( cookie );

    String token = TokenUtil.getToken( context );

    assertThat( token ).isEqualTo( "foo" );
  }

  @Test
  public void testFindsNoTokenInCookieWhenNoTokenIsSet() {
    NewCookie cookie = new NewCookie( "bar", "foo" );
    ContainerRequestContext context = TokenTestUtil.createContext( cookie );
    UriInfo uriInfo = mock( UriInfo.class );
    when( uriInfo.getQueryParameters() ).thenReturn( new MultivaluedHashMap<>() );
    when( context.getUriInfo() ).thenReturn( uriInfo );

    String token = TokenUtil.getToken( context );

    assertThat( token ).isNull();
  }

  @Test
  public void testFindsTokenInQueryParameterWhenNoCookieTokenIsSet() {
    NewCookie cookie = new NewCookie( "bar", "foo" );
    ContainerRequestContext context = TokenTestUtil.createContext( cookie );
    UriInfo uriInfo = mock( UriInfo.class );
    MultivaluedHashMap<String, String> parameters = new MultivaluedHashMap<>();
    parameters.put( "access_token", Lists.newArrayList( "foo" ) );
    when( uriInfo.getQueryParameters() ).thenReturn( parameters );
    when( context.getUriInfo() ).thenReturn( uriInfo );

    String token = TokenUtil.getToken( context );

    assertThat( token ).isEqualTo( "foo" );
  }

  @Test
  public void testAttachesTokenCookieWithMaxAge() {
    Response response = TokenUtil.attachToken( Response.status( 200 ), "foo", TokenUtil.TEN_YEARS ).build();

    NewCookie tokenCookie = response.getCookies().get( "token" );
    assertThat( tokenCookie ).isNotNull();
    assertThat( tokenCookie.getMaxAge() ).isEqualTo( TokenUtil.TEN_YEARS );
    assertThat( tokenCookie.getPath() ).isEqualTo( "/" );
    assertThat( tokenCookie.getValue() ).isEqualTo( "foo" );
  }

  @Test
  public void testAttachesDeleteTokenCookieWithEmptyToken() {
    Response response = TokenUtil.attachToken( Response.status( 200 ), Token.INVALIDATE_TOKEN, 0 ).build();

    NewCookie tokenCookie = response.getCookies().get( "token" );
    assertThat( tokenCookie ).isNotNull();
    assertThat( tokenCookie.getMaxAge() ).isEqualTo( 0 );
    assertThat( tokenCookie.getPath() ).isEqualTo( "/" );
    assertThat( tokenCookie.getValue() ).isEqualTo( "" );
  }

  @Test
  public void testAttachesDeleteTokenCookieWithNullToken() {
    Response response = TokenUtil.attachToken( Response.status( 200 ), null, 0 ).build();

    NewCookie tokenCookie = response.getCookies().get( "token" );
    assertThat( tokenCookie ).isNotNull();
    assertThat( tokenCookie.getMaxAge() ).isEqualTo( 0 );
    assertThat( tokenCookie.getPath() ).isEqualTo( "/" );
    assertThat( tokenCookie.getValue() ).isEqualTo( "" );
  }
}
