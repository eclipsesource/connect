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
package com.eclipsesource.connect.security.injection;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.eclipsesource.connect.api.inject.ConnectProvider;
import com.eclipsesource.connect.api.inject.Resolver;
import com.eclipsesource.connect.model.User;
import com.eclipsesource.connect.security.authentication.PrincipalUser;


public class UserContextProvider implements ConnectProvider<User>{

  @Override
  public Class<User> getType() {
    return User.class;
  }

  @Override
  public User provide( Resolver resolver ) {
    SecurityContext context = resolver.resolve( SecurityContext.class );
    Principal principal = context.getUserPrincipal();
    if( principal != null ) {
      return ( ( PrincipalUser )principal ).getUser();
    }
    return null;
  }
}
