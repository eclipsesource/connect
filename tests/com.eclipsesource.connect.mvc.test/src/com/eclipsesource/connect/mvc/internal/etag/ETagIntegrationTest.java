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

import static com.eclipsesource.connect.mvc.internal.etag.ETagTestUtil.createRequestContext;
import static com.eclipsesource.connect.mvc.internal.etag.ETagTestUtil.createResourceInfo;
import static com.eclipsesource.connect.mvc.internal.etag.ETagTestUtil.createResponseContext;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;



public class ETagIntegrationTest {

  private ETagRequestFilter requestFilter;
  private ETagCache eTagCache;
  private ETagResponseFilter responseFilter;

  @Before
  public void setUp() {
    requestFilter = new ETagRequestFilter();
    responseFilter = new ETagResponseFilter();
    eTagCache = new ETagCache();
    requestFilter.setETagCache( eTagCache );
    responseFilter.setETagCache( eTagCache );
    requestFilter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );
    responseFilter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );
  }

  @Test
  public void testReusesETagIfTagWasCreatedBefore() throws IOException {
    ContainerRequestContext requestContext1 = createRequestContext( "cachable" );
    ContainerResponseContext responseContext1 = createResponseContext( Status.Family.SUCCESSFUL, "foo" );
    ContainerRequestContext requestContext2 = createRequestContext( "cachable" );
    ContainerResponseContext responseContext2 = createResponseContext( Status.Family.SUCCESSFUL, "foo" );

    requestFilter.filter( requestContext1 );
    responseFilter.filter( requestContext1, responseContext1 );
    String etag = ( String )responseContext1.getHeaders().get( "ETag" ).get( 0 );
    requestFilter.filter( requestContext2 );
    responseFilter.filter( requestContext2, responseContext2 );
    String etag2 = ( String )responseContext2.getHeaders().get( "ETag" ).get( 0 );

    assertThat( etag ).isEqualTo( etag2 );
  }


}
