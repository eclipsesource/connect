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
import static org.mockito.Mockito.mock;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;


public class ETagResponseFilterTest {

  private ETagResponseFilter filter;
  private ETagCache eTagCache;

  @Before
  public void setUp() {
    filter = new ETagResponseFilter();
    eTagCache = new ETagCache();
    filter.setETagCache( eTagCache );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutETagCache() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );
    filter.unsetETagCache( null );

    filter.filter( mock( ContainerRequestContext.class ), mock( ContainerResponseContext.class ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutResourceInfo() throws IOException {
    filter.filter( mock( ContainerRequestContext.class ), mock( ContainerResponseContext.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullETagCache() throws IOException {
    filter.setETagCache( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToFilterNullRequestContext() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );

    filter.filter( null, mock( ContainerResponseContext.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToFilterNullResponseContext() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );

    filter.filter( mock( ContainerRequestContext.class ), null );
  }

  @Test
  public void testAddETagOnSuccessIfMissingOnCachableResourceToGETResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );
    ContainerRequestContext requestContext = createRequestContext( "cachable" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SUCCESSFUL, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ).get( 0 ) ).isNotNull();
    assertThat( eTagCache.getETag( requestContext ) ).isNotNull();
  }

  @Test
  public void testAddNoETagOnFailIfMissingOnCachableResourceToGETResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );
    ContainerRequestContext requestContext = createRequestContext( "cachable" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SERVER_ERROR, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isNull();
  }

  @Test
  public void testUpdateETagOnSuccessOnCachableResourceToPUTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "put" );
    ContainerRequestContext requestContext = createRequestContext( "cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SUCCESSFUL, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ).get( 0 ) ).isEqualTo( String.valueOf( "foo".hashCode() ) );
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( String.valueOf( "foo".hashCode() ) );
  }

  @Test
  public void testUpdateNoETagOnFailOnCachableResourceToPUTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "put" );
    ContainerRequestContext requestContext = createRequestContext( "cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SERVER_ERROR, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( "bar" );
  }

  @Test
  public void testUpdateETagOnSuccessOnCachableResourceToPOSTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "post" );
    ContainerRequestContext requestContext = createRequestContext( "cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SUCCESSFUL, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ).get( 0 ) ).isEqualTo( String.valueOf( "foo".hashCode() ) );
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( String.valueOf( "foo".hashCode() ) );
  }

  @Test
  public void testUpdateNoETagOnFailOnCachableResourceToPOSTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "post" );
    ContainerRequestContext requestContext = createRequestContext( "cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SERVER_ERROR, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( "bar" );
  }

  @Test
  public void testRemoveETagOnSuccessOnCachableResourceToPOSTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "delete" );
    ContainerRequestContext requestContext = createRequestContext( "cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SUCCESSFUL, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isNull();
  }

  @Test
  public void testRemoveNoETagOnFailOnCachableResourceToPOSTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "delete" );
    ContainerRequestContext requestContext = createRequestContext( "cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SERVER_ERROR, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( "bar" );
  }

  @Test
  public void testAddNoETagOnSuccessIfMissingOnNonCachableResourceToGETResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "get" );
    ContainerRequestContext requestContext = createRequestContext( "non-cachable" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SUCCESSFUL, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isNull();
  }

  @Test
  public void testAddNoETagOnFailIfMissingOnNonCachableResourceToGETResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "get" );
    ContainerRequestContext requestContext = createRequestContext( "non-cachable" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SERVER_ERROR, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isNull();
  }

  @Test
  public void testUpdateNoETagOnSuccessOnNonCachableResourceToPUTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "put" );
    ContainerRequestContext requestContext = createRequestContext( "non-cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SUCCESSFUL, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( "bar" );
  }

  @Test
  public void testUpdateNoETagOnFailOnNonCachableResourceToPUTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "put" );
    ContainerRequestContext requestContext = createRequestContext( "non-cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SERVER_ERROR, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( "bar" );
  }

  @Test
  public void testUpdateNoETagOnSuccessOnNonCachableResourceToPOSTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "post" );
    ContainerRequestContext requestContext = createRequestContext( "cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SUCCESSFUL, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( "bar" );
  }

  @Test
  public void testUpdateNoETagOnFailOnNonCachableResourceToPOSTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "post" );
    ContainerRequestContext requestContext = createRequestContext( "non-cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SERVER_ERROR, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( "bar" );
  }

  @Test
  public void testRemoveNoETagOnSuccessOnNonCachableResourceToPOSTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "delete" );
    ContainerRequestContext requestContext = createRequestContext( "non-cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SUCCESSFUL, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( "bar" );
  }

  @Test
  public void testRemoveNoETagOnFailOnNonCachableResourceToPOSTResponse() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "delete" );
    ContainerRequestContext requestContext = createRequestContext( "non-cachable" );
    eTagCache.putETag( requestContext, "bar" );
    ContainerResponseContext responseContext = createResponseContext( Status.Family.SERVER_ERROR, "foo" );

    filter.filter( requestContext, responseContext );

    assertThat( responseContext.getHeaders().get( "ETag" ) ).isNull();
    assertThat( eTagCache.getETag( requestContext ) ).isEqualTo( "bar" );
  }

}
