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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static javax.ws.rs.core.Response.Status.NOT_MODIFIED;
import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.eclipsesource.connect.api.asset.ETag;
import com.eclipsesource.connect.mvc.internal.StaticResourceConfiguration;
import com.google.common.base.Strings;


/**
 * The {@link ETagRequestFilter} is responsible to process send etags from the client and handle them as specified
 * in the etag specification: http://tools.ietf.org/html/rfc7232#section-2.3
 */
@Provider
public class ETagRequestFilter implements ContainerRequestFilter {

  private static final String IF_NONE_MATCH = "If-None-Match";
  private static final String IF_MATCH = "If-Match";

  private ETagCache eTagCache;

  private StaticResourceConfiguration configuration;

  @Context
  ResourceInfo resourceInfo;

  @Override
  public void filter( ContainerRequestContext requestContext ) throws IOException {
    checkState( eTagCache != null, "ETagCache not set" );
    checkState( configuration != null, "ETagCache not set" );
    checkState( resourceInfo != null, "StaticResourceConfiguration not set" );
    checkArgument( requestContext != null, "ContainerRequestContext must not be null" );
    if( configuration.useCache() ) {
      if( resourceInfo.getResourceMethod().getAnnotation( ETag.class ) != null ) {
        processETag( requestContext );
      }
    }
  }

  private void processETag( ContainerRequestContext requestContext ) {
    for( Annotation annotation : resourceInfo.getResourceMethod().getDeclaredAnnotations() ) {
      if( annotation instanceof GET ) {
        if( isNotModified( requestContext ) ) {
          requestContext.abortWith( Response.status( NOT_MODIFIED ).build() );
          break;
        }
      } else if( annotation instanceof PUT || annotation instanceof POST || annotation instanceof DELETE ) {
        if( isPreconditionFailed( requestContext ) ) {
          requestContext.abortWith( Response.status( PRECONDITION_FAILED ).build() );
          break;
        }
      }
    }
  }

  private boolean isNotModified( ContainerRequestContext requestContext ) {
    String eTag = getETagFromRequest( requestContext, IF_NONE_MATCH );
    String cachedETag = eTagCache.getETag( requestContext );
    if( !Strings.isNullOrEmpty( eTag ) && !Strings.isNullOrEmpty( cachedETag ) ) {
      if( eTag.equals( cachedETag ) ) {
        return true;
      }
    }
    return false;
  }

  private boolean isPreconditionFailed( ContainerRequestContext requestContext ) {
    String eTag = getETagFromRequest( requestContext, IF_MATCH );
    String cachedETag = eTagCache.getETag( requestContext );
    if( !Strings.isNullOrEmpty( eTag ) && !Strings.isNullOrEmpty( cachedETag ) ) {
      if( !eTag.equals( cachedETag ) ) {
        return true;
      }
    }
    return false;
  }

  private String getETagFromRequest( ContainerRequestContext requestContext, String header ) {
    List<String> eTags = requestContext.getHeaders().get( header );
    if( eTags != null && eTags.size() == 1 ) {
      return eTags.get( 0 );
    }
    return null;
  }

  void setETagCache( ETagCache eTagCache ) {
    checkArgument( eTagCache != null, "ETagCache must not be null" );
    this.eTagCache = eTagCache;
  }

  void unsetETagCache( ETagCache eTagCache ) {
    this.eTagCache = null;
  }

  void setStaticResourceConfiguration( StaticResourceConfiguration configuration ) {
    checkArgument( configuration != null, "Configuration must not be null" );
    this.configuration = configuration;
  }

  void unsetStaticResourceConfiguration( StaticResourceConfiguration configuration ) {
    this.configuration = configuration;
  }

}