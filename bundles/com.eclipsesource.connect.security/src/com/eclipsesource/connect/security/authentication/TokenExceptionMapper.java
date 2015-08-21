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

import static com.eclipsesource.connect.api.util.JsonResponse.error;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.eclipsesource.connect.api.security.Token;
import com.eclipsesource.connect.api.security.TokenException;
import com.eclipsesource.connect.security.TokenUtil;


@Provider
public class TokenExceptionMapper implements ExceptionMapper<TokenException> {

  @Context
  UriInfo uriInfo;
  @Context
  HttpServletRequest request;

  @Override
  public Response toResponse( TokenException exception ) {
    String header = request.getHeader( "Accept" );
    if( header != null && header.contains( MediaType.APPLICATION_JSON ) ) {
      return Response.status( 401 ).type( MediaType.APPLICATION_JSON_TYPE ).entity( error( "Token not valid" ) ).build();
    }
    URI uri = UriBuilder.fromUri( uriInfo.getBaseUri() ).build();
    return TokenUtil.attachToken( Response.seeOther( uri ), Token.INVALIDATE_TOKEN, -1 ).build();
  }
}
