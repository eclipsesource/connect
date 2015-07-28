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
package com.eclipsesource.connect.connector.github.model;



public class GHEmail {

  private final String email;
  private final boolean verified;
  private final boolean primary;

  public GHEmail( String email, boolean verified, boolean primary ) {
    this.email = email;
    this.verified = verified;
    this.primary = primary;
  }

  public String getEmail() {
    return email;
  }

  public boolean isVerified() {
    return verified;
  }

  public boolean isPrimary() {
    return primary;
  }

}
