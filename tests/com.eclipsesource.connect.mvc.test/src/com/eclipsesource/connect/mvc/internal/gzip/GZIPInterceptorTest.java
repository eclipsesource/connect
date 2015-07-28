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
package com.eclipsesource.connect.mvc.internal.gzip;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.junit.Before;
import org.junit.Test;


public class GZIPInterceptorTest {

  private GZIPInterceptor interceptor;

  @Before
  public void setUp() {
    interceptor = new GZIPInterceptor();
  }

  @Test
  public void testAddsContentEncodingHeader() throws WebApplicationException, IOException {
    WriterInterceptorContext context = createContext( "foo" );

    interceptor.aroundWriteTo( context );

    assertThat( context.getHeaders().get( "Content-Encoding" ).get( 0 ) ).isEqualTo( "gzip" );
  }

  @Test
  public void testAddsVaryHeader() throws WebApplicationException, IOException {
    WriterInterceptorContext context = createContext( "foo" );

    interceptor.aroundWriteTo( context );

    assertThat( context.getHeaders().get( "Vary" ).get( 0 ) ).isEqualTo( "Accept-Encoding" );
  }

  @Test
  public void testZipsContent() throws WebApplicationException, IOException {
    WriterInterceptorContext context = createContext( "foo" );

    interceptor.aroundWriteTo( context );

    verify( context ).setOutputStream( any( GZIPOutputStream.class ) );
  }

  private WriterInterceptorContext createContext( String content ) throws IOException {
    WriterInterceptorContext context = mock( WriterInterceptorContext.class );
    MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
    when( context.getHeaders() ).thenReturn( headers );
    when( context.getOutputStream() ).thenReturn( convertStringtoStream( content ) );
    return context;
  }

  private static OutputStream convertStringtoStream( String string ) throws IOException {
    byte[] stringByte = string.getBytes();
    ByteArrayOutputStream bos = new ByteArrayOutputStream( string.length() );
    bos.write( stringByte );
    return bos;
  }
}
