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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriInfo;


public class ETagTestUtil {

  public static ContainerRequestContext createRequestContext( String path ) {
    ContainerRequestContext context = mock( ContainerRequestContext.class );
    UriInfo uriInfo = mock( UriInfo.class );
    when( uriInfo.getPath() ).thenReturn( path );
    when( context.getUriInfo() ).thenReturn( uriInfo );
    MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
    when( context.getHeaders() ).thenReturn( headers );
    return context;
  }

  public static ContainerResponseContext createResponseContext( Family family, Object entity ) {
    ContainerResponseContext responseContext = mock( ContainerResponseContext.class );
    StatusType statusInfo = mock( StatusType.class );
    when( statusInfo.getFamily() ).thenReturn( family );
    when( responseContext.getStatusInfo() ).thenReturn( statusInfo );
    MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
    when( responseContext.getHeaders() ).thenReturn( headers );
    when( responseContext.getEntity() ).thenReturn( entity );
    return responseContext;
  }

  public static ResourceInfo createResourceInfo( Class<?> resourceClass, String method ) {
    ResourceInfo resourceInfo = mock( ResourceInfo.class );
    doReturn( resourceClass ).when( resourceInfo ).getResourceClass();
    when( resourceInfo.getResourceMethod() ).thenReturn( getMethod( resourceClass, method ) );
    return resourceInfo;
  }

  private static Method getMethod( Class<?> resourceClass, String method ) {
    Method[] methods = resourceClass.getDeclaredMethods();
    for( Method declaredMethod : methods ) {
      if( declaredMethod.getName().equals( method ) ) {
        return declaredMethod;
      }
    }
    return null;
  }
}
