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

import static com.google.common.base.Charsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;
import com.eclipsesource.connect.test.util.FileHelper;
import com.google.common.io.CharSource;


public class StaticResourceHandlerTest {

  private StaticResourceHandler handler;

  @Before
  public void setUp() {
    handler = new StaticResourceHandler();
    mockConfiguration( true, false );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullConfig() {
    handler.setStaticResourceConfiguration( null );
  }

  @Test
  public void testHasPathAnnotation() {
    Path path = StaticResourceHandler.class.getAnnotation( Path.class );

    String value = path.value();

    assertThat( value ).isEqualTo( "/assets" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddNullAssetsFinder() {
    handler.setAssetsFinder( null );
  }

  @Test( expected = NotFoundException.class )
  public void testFailsWhenFinderCantLoadResource() {
    handler.setAssetsFinder( mock( AssetsFinder.class ) );

    handler.get( "test.mustache" );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToLoadWithoutConfiguration() {
    handler.unsetStaticResourceConfiguration( null );
    TestAssetsFinder finder = spy( new TestAssetsFinder() );
    handler.setAssetsFinder( finder );

    handler.get( "test.mustache" );
  }

  @Test
  public void testCompressesJsWithoutCache() throws IOException {
    mockConfiguration( false, true );
    handler.setAssetsFinder( new TestAssetsFinder() );

    Response response = handler.get( "uncompressed.js" );

    String content = readResponseString( response );
    assertThat( content ).isNotEqualTo( FileHelper.readFile( "/uncompressed.js", StaticResourceHandlerTest.class ) );
    assertThat( content ).isEqualTo( "var foo=function(){var b=\"foo\";return b};" );
  }

  @Test
  public void testCompressesCssWithoutCache() throws IOException {
    mockConfiguration( false, true );
    handler.setAssetsFinder( new TestAssetsFinder() );

    Response response = handler.get( "uncompressed.css" );

    String content = readResponseString( response );
    assertThat( content ).isNotEqualTo( FileHelper.readFile( "/uncompressed.css", StaticResourceHandlerTest.class ) );
    assertThat( content ).isEqualTo( ".foo{color:#333;border:0}" );
  }

  @Test
  public void testDoesNotCompressesJsWithoutCache() throws IOException {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new TestAssetsFinder() );

    Response response = handler.get( "uncompressed.js" );

    String content = readResponseString( response );
    assertThat( content ).isEqualTo( FileHelper.readFile( "/uncompressed.js", StaticResourceHandlerTest.class ) );
  }

  @Test
  public void testDoesNotCompressesCssWithoutCache() throws IOException {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new TestAssetsFinder() );

    Response response = handler.get( "uncompressed.css" );

    String content = readResponseString( response );
    assertThat( content ).isEqualTo( FileHelper.readFile( "/uncompressed.css", StaticResourceHandlerTest.class ) );
  }

  @Test
  public void testCompressesJsWithCache() throws IOException {
    mockConfiguration( true, true );
    handler.setAssetsFinder( new TestAssetsFinder() );

    Response response = handler.get( "uncompressed.js" );

    String content = readResponseString( response );
    assertThat( content ).isNotEqualTo( FileHelper.readFile( "/uncompressed.js", StaticResourceHandlerTest.class ) );
    assertThat( content ).isEqualTo( "var foo=function(){var b=\"foo\";return b};" );
  }

  @Test
  public void testCompressesCssWithCache() throws IOException {
    mockConfiguration( true, true );
    handler.setAssetsFinder( new TestAssetsFinder() );

    Response response = handler.get( "uncompressed.css" );

    String content = readResponseString( response );
    assertThat( content ).isNotEqualTo( FileHelper.readFile( "/uncompressed.css", StaticResourceHandlerTest.class ) );
    assertThat( content ).isEqualTo( ".foo{color:#333;border:0}" );
  }

  @Test
  public void testDoesNotCompressesJsWithCache() throws IOException {
    mockConfiguration( true, false );
    handler.setAssetsFinder( new TestAssetsFinder() );

    Response response = handler.get( "uncompressed.js" );

    String content = readResponseString( response );
    assertThat( content ).isEqualTo( FileHelper.readFile( "/uncompressed.js", StaticResourceHandlerTest.class ) );
  }

  @Test
  public void testDoesNotCompressesCssWithCache() throws IOException {
    mockConfiguration( true, false );
    handler.setAssetsFinder( new TestAssetsFinder() );

    Response response = handler.get( "uncompressed.css" );

    String content = readResponseString( response );
    assertThat( content ).isEqualTo( FileHelper.readFile( "/uncompressed.css", StaticResourceHandlerTest.class ) );
  }

  @Test
  public void testUsesFinderToLoadResourceUsingCache() {
    TestAssetsFinder finder = spy( new TestAssetsFinder() );
    handler.setAssetsFinder( finder );

    handler.get( "test.mustache" );
    handler.get( "test.mustache" );

    verify( finder, times( 1 ) ).find( "/test.mustache" );
  }

  @Test
  public void testUsesFinderToLoadResourceUsingNoCache() {
    mockConfiguration( false, false );
    TestAssetsFinder finder = spy( new TestAssetsFinder() );
    handler.setAssetsFinder( finder );

    handler.get( "test.mustache" );
    handler.get( "test.mustache" );

    verify( finder, times( 2 ) ).find( "/test.mustache" );
  }

  @Test
  public void testUsesFinderToLoadResource() {
    TestAssetsFinder finder = spy( new TestAssetsFinder() );
    handler.setAssetsFinder( finder );

    handler.get( "test.mustache" );

    verify( finder ).find( "/test.mustache" );
  }

  @Test
  public void testReturnsOkForFoundResource() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new TestAssetsFinder() );

    Response response = handler.get( "test.mustache" );

    assertThat( response.getStatus() ).isEqualTo( 200 );
  }

  @Test
  public void testUsesContentType_HTML() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.html" );

    assertThat( response.getMediaType().toString() ).isEqualTo( MediaType.TEXT_HTML );
  }

  @Test
  public void testUsesContentType_TXT() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.txt" );

    assertThat( response.getMediaType().toString() ).isEqualTo( MediaType.TEXT_PLAIN );
  }

  @Test
  public void testUsesContentType_JPEG() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.jpeg" );

    assertThat( response.getMediaType().toString() ).isEqualTo( "image/jpeg" );
  }

  @Test
  public void testUsesContentType_JPG() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.jpg" );

    assertThat( response.getMediaType().toString() ).isEqualTo( "image/jpeg" );
  }

  @Test
  public void testUsesContentType_PNG() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.png" );

    assertThat( response.getMediaType().toString() ).isEqualTo( "image/png" );
  }

  @Test
  public void testUsesContentType_GIF() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.gif" );

    assertThat( response.getMediaType().toString() ).isEqualTo( "image/gif" );
  }

  @Test
  public void testUsesContentType_PDF() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.pdf" );

    assertThat( response.getMediaType().toString() ).isEqualTo( "application/pdf" );
  }

  @Test
  public void testUsesContentType_CSS() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.css" );

    assertThat( response.getMediaType().toString() ).isEqualTo( "text/css" );
  }

  @Test
  public void testUsesContentType_JSON() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.json" );

    assertThat( response.getMediaType().toString() ).isEqualTo( "application/json" );
  }

  @Test
  public void testUsesContentType_JS() {
    mockConfiguration( false, false );
    handler.setAssetsFinder( new AllwaysSuccessFulResourceFinder() );

    Response response = handler.get( "test.js" );

    assertThat( response.getMediaType().toString() ).isEqualTo( "application/javascript" );
  }

  private String readResponseString( Response response ) throws IOException {
    return new CharSource() {

      @Override
      public Reader openStream() throws IOException {
        return new InputStreamReader( ( InputStream )response.getEntity(), UTF_8 );
      }
    }.read();
  }

  private static class AllwaysSuccessFulResourceFinder implements AssetsFinder {

    @Override
    public AssetsResult find( String name ) {
      return new AssetsResult( StaticResourceHandlerTest.class.getResourceAsStream( "/test.mustache" ), getClass().getClassLoader() );
    }

  }

  private void mockConfiguration( boolean useCache, boolean useCompress  ) {
    StaticResourceConfiguration configuration = mock( StaticResourceConfiguration.class );
    when( configuration.useCache() ).thenReturn( useCache );
    when( configuration.useCompress() ).thenReturn( useCompress );
    handler.setStaticResourceConfiguration( configuration );
  }
}
