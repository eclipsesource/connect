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
package com.eclipsesource.connect.persistence.integrationtest;

import com.eclipsesource.connect.model.Id;


public class TestTypeWithId {

  private String name;
  private Id id;

  public TestTypeWithId( String name ) {
    id = new Id();
    this.name = name;
  }

  public Id getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}
