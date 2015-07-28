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

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.eclipsesource.connect.api.security.Token;


public class TokenUtil {

  static final int TEN_YEARS = 60 * 60 * 24 * 365 * 10;

  private static final String TOKEN_NAME = "token";

  public static String getToken( ContainerRequestContext context ) {
    Cookie tokenCookie = context.getCookies().get( TOKEN_NAME );
    String token = null;
    if( tokenCookie != null ) {
      token = tokenCookie.getValue();
    } else {
      List<String> parameters = context.getUriInfo().getQueryParameters().get( "access_token" );
      if( parameters != null && !parameters.isEmpty() ) {
        token = parameters.get( 0 );
      }
    }
    return token;
  }

  public static ResponseBuilder attachToken( ResponseBuilder builder, String token ) {
    if( token == null || token.isEmpty() ) {
      return builder.cookie( new NewCookie( TOKEN_NAME, Token.INVALIDATE_TOKEN, "/", null, null, 0, false ) );
    }
    return builder.cookie( new NewCookie( TOKEN_NAME, token, "/", null, null, TEN_YEARS, false ) );
  }

  private TokenUtil() {
    // prevent instantiation
  }
}
