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
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.container.ContainerRequestContext;

import org.junit.Before;
import org.junit.Test;


public class ETagCacheTest {

  private ETagCache cache;

  @Before
  public void setUp() {
    cache = new ETagCache();
  }

  @Test
  public void testPutsETag() {
    ContainerRequestContext requestContext = createRequestContext( "foo" );

    cache.putETag( requestContext, "tag" );
    String eTag = cache.getETag( requestContext );

    assertThat( eTag ).isEqualTo( "tag" );
  }

  @Test
  public void testPutsETagAndUsesPathAsKey() {
    ContainerRequestContext requestContext = createRequestContext( "foo" );
    ContainerRequestContext requestContext2 = createRequestContext( "foo2" );

    cache.putETag( requestContext, "tag" );
    String eTag = cache.getETag( requestContext2 );

    assertThat( eTag ).isNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testPutFailsWithNullContext() {
    cache.putETag( null, "tag" );
  }

  @Test
  public void testReplacesETagValue() {
    ContainerRequestContext requestContext = createRequestContext( "foo" );

    cache.putETag( requestContext, "tag" );
    cache.updateETag( requestContext, "tag2" );
    String eTag = cache.getETag( requestContext );

    assertThat( eTag ).isEqualTo( "tag2" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testReplaceFailsWithNullContext() {
    cache.updateETag( null, "tag" );
  }

  @Test
  public void testDeletesETagValue() {
    ContainerRequestContext requestContext = createRequestContext( "foo" );

    cache.putETag( requestContext, "tag" );
    cache.removeETag( requestContext );
    String eTag = cache.getETag( requestContext );

    assertThat( eTag ).isNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testDeleteFailsWithNullContext() {
    cache.removeETag( null );
  }

}
