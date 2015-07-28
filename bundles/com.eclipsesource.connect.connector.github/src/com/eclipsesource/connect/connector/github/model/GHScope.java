/***********************************************************import java.util.Arrays;
opyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.connector.github.model;

import java.util.Arrays;


public enum GHScope {

  USER( "user" ),
  USER_EMAIL( "user:email" ),
  USER_FOLLOW( "user:follow" ),
  PUBLIC_REPO( "public_repo" ),
  REPO( "repo" ),
  REPO_DEPLOYMENT( "repo_deployment" ),
  REPO_STATUS( "repo:status" ),
  REPO_DELETE( "delete_repo" ),
  NOTIFICATIONS( "notifications" ),
  GIST( "gist" ),
  REPO_HOOK_READ( "read:repo_hook" ),
  REPO_HOOK_WRITE( "write:repo_hook" ),
  REPO_HOOK_ADMIN( "admin:repo_hook" ),
  ORG_READ( "read:org" ),
  ORG_WRITE( "write:org" ),
  ORG_ADMIN( "admin:org" ),
  PUBLIC_KEY_READ( "read:public_key" ),
  PUBLIC_KEY_WRITE( "write:public_key" ),
  PUBLIC_KEY_ADMIN( "admin:public_key" );

  private final String scope;

  private GHScope( String scope ) {
    this.scope = scope;
  }

  public String getScope() {
    return scope;
  }

  public static GHScope fromString( String scope ) {
    return Arrays.asList( GHScope.values() ).stream().filter( ghScope -> ghScope.getScope().equals( scope ) )
                                                     .findFirst()
                                                     .orElse( null );
  }
}
