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


public class GHIssueLabel {

  private final String url;
  private final String name;
  private final String color;

  public GHIssueLabel( String url, String name, String color ) {
    this.url = url;
    this.name = name;
    this.color = color;
  }

  public String getUrl() {
    return url;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

}
