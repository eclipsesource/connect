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
import static com.google.common.base.Strings.isNullOrEmpty;


public class Token {

  public static final String INVALIDATE_TOKEN = "";

  private final String userId;
  private final String token;

  public Token( String userId, String token ) {
    validateArguments( userId, token );
    this.userId = userId;
    this.token = token;
  }

  private void validateArguments( String userId, String token ) {
    checkArgument( !isNullOrEmpty( userId ), "UserId must not be null or empty" );
    checkArgument( !userId.trim().isEmpty(), "UserId must not be empty" );
    checkArgument( !isNullOrEmpty( token ), "Token must not be null or empty" );
    checkArgument( !token.trim().isEmpty(), "Token must not be empty" );
  }

  public String getUserId() {
    return userId;
  }

  public String getToken() {
    return token;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( token == null ) ? 0 : token.hashCode() );
    result = prime * result + ( ( userId == null ) ? 0 : userId.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    Token other = ( Token )obj;
    if( token == null ) {
      if( other.token != null ) {
        return false;
      }
    } else if( !token.equals( other.token ) ) {
      return false;
    }
    if( userId == null ) {
      if( other.userId != null ) {
        return false;
      }
    } else if( !userId.equals( other.userId ) ) {
      return false;
    }
    return true;
  }

}
