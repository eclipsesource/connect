/*****************************************************import static com.google.common.base.Preconditions.checkArgument;

import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.container.ContainerRequestContext;
r the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.mvc.internal.etag;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.container.ContainerRequestContext;

public class ETagCache {

  private final ConcurrentHashMap<String, String> eTagCache;

  public ETagCache() {
    eTagCache = new ConcurrentHashMap<>();
  }

  public String getETag( ContainerRequestContext requestContext ) {
    String key = getETagCacheKey( requestContext );
    return eTagCache.get( key );
  }

  public void putETag( ContainerRequestContext requestContext, String eTag ) {
    checkArgument( requestContext != null, "ContainerRequestContext must no be null" );
    String key = getETagCacheKey( requestContext );
    eTagCache.put( key, eTag );
  }

  public void updateETag( ContainerRequestContext requestContext, String eTag ) {
    checkArgument( requestContext != null, "ContainerRequestContext must no be null" );
    String key = getETagCacheKey( requestContext );
    eTagCache.replace( key, eTag );
  }

  public void removeETag( ContainerRequestContext requestContext ) {
    checkArgument( requestContext != null, "ContainerRequestContext must no be null" );
    String key = getETagCacheKey( requestContext );
    eTagCache.remove( key );
  }

  private String getETagCacheKey( ContainerRequestContext requestContext ) {
    return requestContext.getUriInfo().getPath();
  }

}