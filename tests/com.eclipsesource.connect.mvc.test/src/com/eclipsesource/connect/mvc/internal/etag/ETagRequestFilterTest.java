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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.connect.mvc.internal.StaticResourceConfiguration;
import com.google.common.collect.Lists;


public class ETagRequestFilterTest {

  private ETagRequestFilter filter;
  private ETagCache eTagCache;

  @Before
  public void setUp() {
    filter = new ETagRequestFilter();
    eTagCache = new ETagCache();
    filter.setETagCache( eTagCache );
    StaticResourceConfiguration configuration = mock( StaticResourceConfiguration.class );
    when( configuration.useCache() ).thenReturn( true );
    filter.setStaticResourceConfiguration( configuration );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutETagCache() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );
    filter.unsetETagCache( null );

    filter.filter( mock( ContainerRequestContext.class ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutResourceInfo() throws IOException {
    filter.filter( mock( ContainerRequestContext.class ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutStaticResourceConfig() throws IOException {
    filter.unsetStaticResourceConfiguration( null );

    filter.filter( mock( ContainerRequestContext.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullETagCache() throws IOException {
    filter.setETagCache( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullStaticResourceConfig() throws IOException {
    filter.setStaticResourceConfiguration( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToFilterNullContext() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );

    filter.filter( null );
  }

  @Test
  public void testDoesNotAbortGETRequestToCachableResourceWithoutETag() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );
    ContainerRequestContext context = createRequestContext( "cachable" );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testAbortGETRequestToCachableResourceWithMatchingETagUsingNotModified() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );
    ContainerRequestContext context = createRequestContext( "cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-None-Match", Lists.newArrayList( "12345" ) );

    filter.filter( context );

    ArgumentCaptor<Response> captor = ArgumentCaptor.forClass( Response.class );
    verify( context ).abortWith( captor.capture() );
    assertThat( captor.getValue().getStatus() ).isEqualTo( 304 );
  }

  @Test
  public void testDoesNotAbortGETRequestToCachableResourceWithNonMatchingETagUsingNotModified() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "get" );
    ContainerRequestContext context = createRequestContext( "cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-None-Match", Lists.newArrayList( "54321" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testAbortPUTRequestToCachableResourceWithNonMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "put" );
    ContainerRequestContext context = createRequestContext( "cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "54321" ) );

    filter.filter( context );

    ArgumentCaptor<Response> captor = ArgumentCaptor.forClass( Response.class );
    verify( context ).abortWith( captor.capture() );
    assertThat( captor.getValue().getStatus() ).isEqualTo( 412 );
  }

  @Test
  public void testAbortPUTRequestToCachableResourceWithMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "put" );
    ContainerRequestContext context = createRequestContext( "cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "12345" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testAbortPOSTRequestToCachableResourceWithNonMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "post" );
    ContainerRequestContext context = createRequestContext( "cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "54321" ) );

    filter.filter( context );

    ArgumentCaptor<Response> captor = ArgumentCaptor.forClass( Response.class );
    verify( context ).abortWith( captor.capture() );
    assertThat( captor.getValue().getStatus() ).isEqualTo( 412 );
  }

  @Test
  public void testAbortPOSTRequestToCachableResourceWithMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "post" );
    ContainerRequestContext context = createRequestContext( "cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "12345" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testAbortDELETERequestToCachableResourceWithNonMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "delete" );
    ContainerRequestContext context = createRequestContext( "cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "54321" ) );

    filter.filter( context );

    ArgumentCaptor<Response> captor = ArgumentCaptor.forClass( Response.class );
    verify( context ).abortWith( captor.capture() );
    assertThat( captor.getValue().getStatus() ).isEqualTo( 412 );
  }

  @Test
  public void testAbortDELETERequestToCachableResourceWithMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( CacheableResource.class, "delete" );
    ContainerRequestContext context = createRequestContext( "cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "12345" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testDoesNotAbortGETRequestToNonCachableResourceWithoutETag() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "get" );
    ContainerRequestContext context = createRequestContext( "non-cachable" );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testDoesNotAbortGETRequestToNonCachableResourceWithMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "get" );
    ContainerRequestContext context = createRequestContext( "non-cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-None-Match", Lists.newArrayList( "12345" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testDoesNotAbortGETRequestToNonCachableResourceWithNonMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "get" );
    ContainerRequestContext context = createRequestContext( "non-cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-None-Match", Lists.newArrayList( "54321" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testDoesNotAbortPUTRequestToNonCachableResourceWithNonMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "put" );
    ContainerRequestContext context = createRequestContext( "non-cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "54321" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testDoesAbortPUTRequestToNonCachableResourceWithMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "put" );
    ContainerRequestContext context = createRequestContext( "non-cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "12345" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testDoesNotAbortPOSTRequestToCachableResourceWithNonMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "post" );
    ContainerRequestContext context = createRequestContext( "non-cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "54321" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testDoesNotAbortPOSTRequestToNonCachableResourceWithMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "post" );
    ContainerRequestContext context = createRequestContext( "non-cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "12345" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testDoesNotAbortDELETERequestToNonCachableResourceWithNonMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "delete" );
    ContainerRequestContext context = createRequestContext( "non-cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "54321" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

  @Test
  public void testDoesNotAbortDELETERequestToNonCachableResourceWithMatchingETag() throws IOException {
    filter.resourceInfo = createResourceInfo( NonCacheableResource.class, "delete" );
    ContainerRequestContext context = createRequestContext( "non-cachable" );
    eTagCache.putETag( context, "12345" );
    context.getHeaders().put( "If-Match", Lists.newArrayList( "12345" ) );

    filter.filter( context );

    verify( context, never() ).abortWith( any( Response.class ) );
  }

}
