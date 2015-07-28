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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.github.mustachejava.Mustache;
import com.google.common.base.Charsets;


public class MustacheTemplateProcessorTest {

  private MustacheTemplateProcessor mustacheTemplateProcessor;

  @Before
  public void setUp() {
    mustacheTemplateProcessor = new MustacheTemplateProcessor();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddNullAssetsFinder() {
    mustacheTemplateProcessor.setAssetsFinder( null );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsResolvingWithoutResourceFinder() {
    mustacheTemplateProcessor.resolve( "foo.mustache", MediaType.TEXT_HTML_TYPE );
  }

  @Test
  public void testKeepsAssetsFinder() {
    AssetsFinder finder = new TestAssetsFinder();

    mustacheTemplateProcessor.setAssetsFinder( finder );

    AssetsFinder actualFinder = mustacheTemplateProcessor.getAssetsFinder();
    assertThat( actualFinder ).isSameAs( finder );
  }

  @Test
  public void testResolvesTemplateFromFinder() {
    AssetsFinder finder = spy( new TestAssetsFinder() );
    mustacheTemplateProcessor.setAssetsFinder( finder );

    mustacheTemplateProcessor.resolve( "/test.mustache", MediaType.TEXT_HTML_TYPE );

    verify( finder ).find( "/test.mustache" );
  }

  @Test
  public void testWritesTemplate() throws IOException {
    mustacheTemplateProcessor.setAssetsFinder( new TestAssetsFinder() );
    Mustache mustache = mustacheTemplateProcessor.resolve( "/test.mustache", MediaType.TEXT_HTML_TYPE );
    Viewable viewable = mock( Viewable.class );
    when( viewable.getModel() ).thenReturn( new TestModel( "bar" ) );
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    MultivaluedMap<String, Object> httpHeaders = new MultivaluedHashMap<>();

    mustacheTemplateProcessor.writeTo( mustache, viewable, MediaType.TEXT_HTML_TYPE, httpHeaders, out );

    out.close();
    String result = new String( out.toByteArray(), Charsets.UTF_8 );
    assertThat( result ).isEqualTo( "test bar" );
  }

  @Test
  public void testWritesTemplateAndSetsEncodingHeader() throws IOException {
    mustacheTemplateProcessor.setAssetsFinder( new TestAssetsFinder() );
    Mustache mustache = mustacheTemplateProcessor.resolve( "/test.mustache", MediaType.TEXT_HTML_TYPE );
    Viewable viewable = mock( Viewable.class );
    when( viewable.getModel() ).thenReturn( new TestModel( "bar" ) );
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    MultivaluedMap<String, Object> httpHeaders = new MultivaluedHashMap<>();
    MediaType type = MediaType.TEXT_HTML_TYPE.withCharset( "ISO-8859-1" );

    mustacheTemplateProcessor.writeTo( mustache, viewable, type, httpHeaders, out );

    out.close();
    String result = new String( out.toByteArray(), Charset.forName( "ISO-8859-1" ) );
    assertThat( result ).isEqualTo( "test bar" );
    assertThat( httpHeaders.get( HttpHeaders.CONTENT_TYPE ).get( 0 ) ).isEqualTo( "text/html;charset=ISO-8859-1" );
  }

  class TestModel {

    private String foo;

    public TestModel( String foo ) {
      this.foo = foo;
    }

    public String getFoo() {
      return foo;
    }

  }
}
