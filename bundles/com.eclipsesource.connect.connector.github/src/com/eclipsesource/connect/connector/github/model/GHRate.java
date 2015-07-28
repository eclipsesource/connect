/***********************************************************import java.util.Map;
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

import java.util.Map;


public class GHRate {

  private Map<String, Map<String, Integer>> resources;

  public int getCoreLimit() {
    return resources.get( "core" ).get( "limit" );
  }

  public int getCoreRemaining() {
    return resources.get( "core" ).get( "remaining" );
  }

  public int getCoreReset() {
    return resources.get( "core" ).get( "reset" );
  }

  public int getSearchLimit() {
    return resources.get( "search" ).get( "limit" );
  }

  public int getSearchRemaining() {
    return resources.get( "search" ).get( "remaining" );
  }

  public int getSearchReset() {
    return resources.get( "search" ).get( "reset" );
  }
}
