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
package com.eclipsesource.connect.mvc.internal.etag;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.eclipsesource.connect.api.asset.ETag;


@Path( "cachable" )
public class CacheableResource {

  private String value;

  public CacheableResource( String value ) {
    this.value = value;
  }

  @GET
  @ETag
  public String get() {
    return value;
  }

  @PUT
  @ETag
  public String put( String value ) {
    this.value = value;
    return value;
  }

  @POST
  @ETag
  public String post( String value ) {
    this.value = value;
    return value;
  }

  @DELETE
  @ETag
  public void delete() {
    this.value = null;
  }

}
