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
package com.eclipsesource.connect.mvc.internal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path( "/proxy" )
public class ProxyResource {

  private final Client client;

  public ProxyResource() {
    client = ClientBuilder.newClient();
  }

  @GET
  @Produces( MediaType.WILDCARD )
  public Response get( @QueryParam( "source" ) String source ) {
    if( source == null || source.trim().isEmpty() ) {
      return Response.status( 400 ).build();
    }
    return client.target( source ).request().get();
  }
}
