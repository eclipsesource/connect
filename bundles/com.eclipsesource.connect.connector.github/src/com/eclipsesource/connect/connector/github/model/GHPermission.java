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


public class GHPermission {

  private boolean admin;
  private boolean push;
  private boolean pull;

  public boolean hasAdmin() {
    return admin;
  }

  public boolean hasPush() {
    return push;
  }

  public boolean hasPull() {
    return pull;
  }

}
