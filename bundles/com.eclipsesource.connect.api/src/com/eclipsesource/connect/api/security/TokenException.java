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
package com.eclipsesource.connect.api.security;

import static com.google.common.base.Preconditions.checkArgument;


public class TokenException extends IllegalStateException {

  private final String token;

  public TokenException( String token ) {
    this( token, null );
  }

  public TokenException( String token, Throwable cause ) {
    super( "Token not found: " + token, cause );
    checkArgument( token != null, "Token must not be null or empty" );
    this.token = token;
  }

  public String getToken() {
    return token;
  }
}
