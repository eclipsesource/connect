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


public class GHMarkdownConfiguration {

  private final String text;
  private final String mode;
  private final String context;

  public GHMarkdownConfiguration( String text ) {
    this.text = text;
    this.mode = "gfm";
    this.context = "github/gollum";
  }

  public String getText() {
    return text;
  }

  public String getMode() {
    return mode;
  }

  public String getContext() {
    return context;
  }

}
