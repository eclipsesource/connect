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
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Charsets;


@Provider
public class MustacheTemplateProcessor implements TemplateProcessor<Mustache> {

  private final MustacheFactory mustacheFactory;
  private AssetsFinder assetsFinder;

  public MustacheTemplateProcessor() {
    mustacheFactory = new ConnectMustacheFactory( this );
  }

  @Override
  public Mustache resolve( String name, MediaType mediaType ) {
    checkState( assetsFinder != null, "AssetsFinder not set" );
    AssetsResult result = assetsFinder.find( name );
    if( result != null ) {
      return compileTemplate( name, result );
    }
    throw new IllegalStateException( "Could not load template with name: " + name );
  }

  private Mustache compileTemplate( String name, AssetsResult result ) {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader( result.getClassLoader() );
      return mustacheFactory.compile( new InputStreamReader( result.getStream(), UTF_8 ), name );
    } finally {
      Thread.currentThread().setContextClassLoader( original );
    }
  }

  @Override
  public void writeTo( Mustache mustache,
                       Viewable viewable,
                       MediaType mediaType,
                       MultivaluedMap<String, Object> httpHeaders,
                       OutputStream out ) throws IOException
  {
    Charset encoding = createContentType( mediaType, httpHeaders );
    mustache.execute( new OutputStreamWriter( out, encoding ), viewable.getModel() ).flush();
  }

  private Charset createContentType( MediaType mediaType, MultivaluedMap<String, Object> httpHeaders ) {
    Charset encoding;
    String charset = mediaType.getParameters().get( MediaType.CHARSET_PARAMETER );
    MediaType finalMediaType;
    if( charset == null ) {
      encoding = Charsets.UTF_8;
      Map<String, String> params = new HashMap<String, String>( mediaType.getParameters() );
      params.put( MediaType.CHARSET_PARAMETER, encoding.name() );
      finalMediaType = new MediaType( mediaType.getType(), mediaType.getSubtype(), params );
    } else {
      encoding = Charset.forName( charset );
      finalMediaType = mediaType;
    }
    List<Object> typeList = new ArrayList<Object>( 1 );
    typeList.add( finalMediaType.toString() );
    httpHeaders.put( HttpHeaders.CONTENT_TYPE, typeList );
    return encoding;
  }

  void setAssetsFinder( AssetsFinder assetsFinder ) {
    checkArgument( assetsFinder != null, "AssetsFinder must not be null" );
    this.assetsFinder = assetsFinder;
  }

  void unsetAssetsFinder( AssetsFinder assetsFinder ) {
    this.assetsFinder = null;
  }

  public AssetsFinder getAssetsFinder() {
    return assetsFinder;
  }
}
