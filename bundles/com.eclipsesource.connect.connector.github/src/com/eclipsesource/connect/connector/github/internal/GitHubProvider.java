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
package com.eclipsesource.connect.connector.github.internal;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;
import com.google.gson.Gson;


@Provider
@Produces( APPLICATION_JSON )
@Consumes( APPLICATION_JSON )
public class GitHubProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {

  private final Gson gson;

  public GitHubProvider() {
    gson = new Gson();
  }

  @Override
  public long getSize( T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType  ) {
    return -1;
  }

  @Override
  public boolean isWriteable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType ) {
    return true;
  }

  @Override
  public void writeTo( T object,
                       Class<?> type,
                       Type genericType,
                       Annotation[] annotations,
                       MediaType mediaType,
                       MultivaluedMap<String, Object> httpHeaders,
                       OutputStream entityStream ) throws IOException, WebApplicationException
  {
    try( PrintWriter printWriter = new PrintWriter( new OutputStreamWriter( entityStream, Charsets.UTF_8 ) ) ) {
      String json = gson.toJson( object );
      printWriter.write( json );
      printWriter.flush();
    }
  }

  @Override
  public boolean isReadable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType ) {
    return true;
  }

  @Override
  public T readFrom( Class<T> type,
                     Type gnericType,
                     Annotation[] annotations,
                     MediaType mediaType,
                     MultivaluedMap<String, String> httpHeaders,
                     InputStream entityStream ) throws IOException, WebApplicationException
  {
    try( InputStreamReader reader = new InputStreamReader( entityStream, Charsets.UTF_8 ) ) {
      if( Collection.class.isAssignableFrom( type ) ) {
        return gson.fromJson( reader, gnericType );
      }
      return gson.fromJson( reader, type );
    }
  }

}