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

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Provider
@GZIP
public class GZIPInterceptor implements WriterInterceptor {

  @Override
  public void aroundWriteTo( WriterInterceptorContext context ) throws IOException, WebApplicationException {
    MultivaluedMap<String, Object> headers = context.getHeaders();
    headers.add( "Content-Encoding", "gzip" );
    headers.add( "Vary", "Accept-Encoding" );
    final OutputStream outputStream = context.getOutputStream();
    context.setOutputStream( new GZIPOutputStream( outputStream ) );
    context.proceed();
  }

}