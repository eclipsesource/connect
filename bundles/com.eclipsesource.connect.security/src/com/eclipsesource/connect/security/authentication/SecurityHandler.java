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
package com.eclipsesource.connect.security.authentication;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.security.Principal;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.eclipsesource.connect.api.security.TokenAdmin;
import com.eclipsesource.connect.model.User;
import com.eclipsesource.connect.security.TokenUtil;
import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;


public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler {

  private TokenAdmin tokenAdmin;

  @Override
  public boolean isUserInRole( Principal user, String role ) {
    checkArgument( user instanceof PrincipalUser, "user must be of type " + PrincipalUser.class.getName() );
    User logedInUser = ( ( PrincipalUser )user ).getUser();
    List<String> roles = logedInUser.getRoles();
    return roles.contains( role );
  }

  @Override
  public Principal authenticate( ContainerRequestContext requestContext ) {
    checkState( tokenAdmin != null, "TokenAdmin not set" );
    String token = TokenUtil.getToken( requestContext );
    if( token != null ) {
      User user = tokenAdmin.get( token );
      if( user != null ) {
        return new PrincipalUser( user );
      }
    }
    return null;
  }

  @Override
  public String getAuthenticationScheme() {
    return null;
  }

  void setTokenAdmin( TokenAdmin tokenAdmin ) {
    checkArgument( tokenAdmin != null, "TokenAdmin must not be null" );
    this.tokenAdmin = tokenAdmin;
  }

  void unsetTokenAdmin( TokenAdmin tokenAdmin ) {
    this.tokenAdmin = null;
  }

}
