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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.eclipsesource.connect.api.asset.ETag;


/**
 * The {@link ETagResponseFilter} is responsible to write an etag to a already processed response.
 */
@Provider
public class ETagResponseFilter implements ContainerResponseFilter {

  private static final String ENTITY_TAG = "ETag";

  private ETagCache eTagCache;

  @Context
  ResourceInfo resourceInfo;

  @Override
  public void filter( ContainerRequestContext requestContext, ContainerResponseContext responseContext ) throws IOException {
    checkState( eTagCache != null, "ETagCache not set" );
    checkState( resourceInfo != null, "ResourceInfo not set" );
    checkArgument( requestContext != null, "ContainerRequestContext must not be null" );
    checkArgument( responseContext != null, "ContainerResponseContext must not be null" );
    Method resourceMethod = resourceInfo.getResourceMethod();
    if( resourceMethod != null && resourceMethod.getAnnotation( ETag.class ) != null ) {
      if( responseContext.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL ) {
        processETag( requestContext, responseContext );
      }
    }
  }

  private void processETag( ContainerRequestContext requestContext, ContainerResponseContext responseContext ) {
    for( Annotation annotation : resourceInfo.getResourceMethod().getDeclaredAnnotations() ) {
      if( annotation instanceof GET ) {
        addETagIfAbsent( requestContext, responseContext );
        break;
      } else if( annotation instanceof PUT || annotation instanceof POST ) {
        updateETag( requestContext, responseContext );
        break;
      } else if( annotation instanceof DELETE ) {
        removeETag( requestContext );
        break;
      }
    }
  }

  private void addETagIfAbsent( ContainerRequestContext requestContext, ContainerResponseContext responseContext ) {
    Object entity = responseContext.getEntity();
    if( entity != null ) {
      String eTag = eTagCache.getETag( requestContext );
      if( eTag == null ) {
        eTag = UUID.randomUUID().toString();
        eTagCache.putETag( requestContext, eTag );
      }
      responseContext.getHeaders().add( ENTITY_TAG, eTag );
    }
  }

  private void updateETag( ContainerRequestContext requestContext, ContainerResponseContext responseContext ) {
    Object entity = responseContext.getEntity();
    if( entity != null ) {
      String eTag = String.valueOf( entity.hashCode() );
      responseContext.getHeaders().add( ENTITY_TAG, eTag );
      eTagCache.updateETag( requestContext, eTag );
    }
  }

  private void removeETag( ContainerRequestContext requestContext ) {
    eTagCache.removeETag( requestContext );
  }

  void setETagCache( ETagCache eTagCache ) {
    checkArgument( eTagCache != null, "ETagCache must not be null" );
    this.eTagCache = eTagCache;
  }

  void unsetETagCache( ETagCache eTagCache ) {
    this.eTagCache = null;
  }

}