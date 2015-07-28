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

import java.security.Principal;

import com.eclipsesource.connect.model.User;


public class PrincipalUser implements Principal {

  private final User user;

  public PrincipalUser( User user ) {
    checkArgument( user != null, "User must not be null" );
    this.user = user;
  }

  @Override
  public String getName() {
    return user.getName();
  }

  public User getUser() {
    return user;
  }
}
