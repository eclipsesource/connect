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

import static com.eclipsesource.connect.mvc.internal.Compressors.compress;
import static com.eclipsesource.connect.mvc.internal.Compressors.isCompressable;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;
import com.eclipsesource.connect.api.asset.ETag;
import com.eclipsesource.connect.mvc.internal.gzip.GZIP;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.ByteSource;
import com.google.common.util.concurrent.UncheckedExecutionException;


@Path( "/assets" )
public class StaticResourceHandler {

  private final LoadingCache<String, byte[]> cache;
  private StaticResourceConfiguration configuration;
  private AssetsFinder assetsFinder;

  public StaticResourceHandler() {
    cache = createCache();
  }

  private LoadingCache<String, byte[]> createCache() {
    return CacheBuilder.newBuilder().build( new CacheLoader<String, byte[]>() {

      @Override
      public byte[] load( String path ) throws Exception {
        AssetsResult result = assetsFinder.find( "/" + path );
        if( result != null ) {
          try( InputStream stream = result.getStream() ) {
            if( stream != null ) {
              if( configuration.useCompress() && isCompressable( path ) ) {
                return compress( path, stream ).getBytes( UTF_8 );
              }
              return new ByteSource() {

                @Override
                public InputStream openStream() throws IOException {
                  return stream;
                }
              }.read();
            }
          }
        }
        throw new IllegalStateException( path  + " not found" );
      }

    } );
  }

  @GET
  @Path( "{path:.*}" )
  @Produces( MediaType.WILDCARD )
  @ETag
  @GZIP
  public Response get( @PathParam( "path" ) String path ) {
    ensureServices();
    if( configuration.useCache() ) {
      return Response.ok( findInCache( path ) ).type( getMediaType( path ) ).build();
    }
    return Response.ok( find( path ) ).type( getMediaType( path ) ).build();
  }

  private void ensureServices() {
    checkState( configuration != null, "Configuration not set" );
    checkState( assetsFinder != null, "AssetsHandler not set" );
  }

  private String getMediaType( String path ) {
    String type = URLConnection.guessContentTypeFromName( path );
    if( type == null && path.endsWith( ".json" ) ) {
      type = MediaType.APPLICATION_JSON;
    } else if( type == null && path.endsWith( ".js" ) ) {
      type = "application/javascript";
    } else if( type == null && path.endsWith( ".css" ) ) {
      type = "text/css";
    } else if( type == null ) {
      type = MediaType.WILDCARD;
    }
    return type;
  }

  private InputStream findInCache( String path ) {
    try {
      return new ByteArrayInputStream( cache.get( path ) );
    } catch( UncheckedExecutionException | ExecutionException e ) {
      throw new NotFoundException();
    }
  }

  private InputStream find( String path ) {
    AssetsResult result = assetsFinder.find( "/" + path );
    if( result != null ) {
      if( configuration.useCompress() && isCompressable( path ) ) {
        return new ByteArrayInputStream( compress( path, result.getStream() ).getBytes( UTF_8 ) );
      }
      return result.getStream();
    }
    throw new NotFoundException();
  }

  void setStaticResourceConfiguration( StaticResourceConfiguration configuration ) {
    checkArgument( configuration != null, "Configuration must not be null" );
    this.configuration = configuration;
  }

  void unsetStaticResourceConfiguration( StaticResourceConfiguration configuration ) {
    this.configuration = configuration;
  }

  void setAssetsFinder( AssetsFinder assetsFinder ) {
    checkArgument( assetsFinder != null, "AssetsFinder must not be null" );
    this.assetsFinder = assetsFinder;
  }

  void unsetAssetsFinder( AssetsFinder assetsFinder ) {
    this.assetsFinder = null;
  }
}
