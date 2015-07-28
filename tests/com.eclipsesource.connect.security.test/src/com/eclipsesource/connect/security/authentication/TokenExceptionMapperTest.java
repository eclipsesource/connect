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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.security.Token;
import com.eclipsesource.connect.api.security.TokenException;
import com.eclipsesource.connect.api.util.JsonResponse;


public class TokenExceptionMapperTest {

  private HttpServletRequest request;
  private TokenExceptionMapper mapper;

  @Before
  public void setUp() {
    mapper = new TokenExceptionMapper();
    request = mock( HttpServletRequest.class );
    mapper.request = request;
  }

  @Test
  public void testRedirectsToBase() {
    UriInfo uriInfo = mock( UriInfo.class );
    when( uriInfo.getBaseUri() ).thenReturn( UriBuilder.fromUri( "/" ).build() );
    mapper.uriInfo = uriInfo;

    Response response = mapper.toResponse( new TokenException( "foo" ) );

    assertThat( response.getStatus() ).isEqualTo( 303 );
  }

  @Test
  public void testRemovesTokenCookie() {
    UriInfo uriInfo = mock( UriInfo.class );
    when( uriInfo.getBaseUri() ).thenReturn( UriBuilder.fromUri( "/" ).build() );
    mapper.uriInfo = uriInfo;

    Response response = mapper.toResponse( new TokenException( "foo" ) );

    NewCookie newCookie = response.getCookies().get( "token" );
    assertThat( newCookie.getValue() ).isEqualTo( Token.INVALIDATE_TOKEN );
    assertThat( newCookie.getMaxAge() ).isEqualTo( 0 );
  }

  @Test
  public void testProducesErrorForJson() {
    when( request.getHeader( "Accept" ) ).thenReturn( MediaType.APPLICATION_JSON );
    Response response = mapper.toResponse( new TokenException( "foo" ) );

    assertThat( response.getStatus() ).isEqualTo( 401 );
    assertThat( response.getEntity() ).isInstanceOf( JsonResponse.Error.class );
    assertThat( ( ( JsonResponse.Error ) response.getEntity() ).getReason() ).isEqualTo( "Token not valid" );
  }
}
