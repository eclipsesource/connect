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


public class GHGitCommiter {

  private final String name;
  private final String email;
  private final String date;

  public GHGitCommiter( String name, String email, String date ) {
    this.name = name;
    this.email = email;
    this.date = date;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getDate() {
    return date;
  }

}
