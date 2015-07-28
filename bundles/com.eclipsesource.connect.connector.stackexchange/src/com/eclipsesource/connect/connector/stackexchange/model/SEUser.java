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
package com.eclipsesource.connect.connector.stackexchange.model;

import java.util.List;
import java.util.Map;


public class SEUser {

  private final List<Map<String, Object>> items;

  SEUser( List<Map<String, Object>> items ) {
    this.items = items;
  }

  public String getUserId() {
    return String.valueOf( ( ( Double )items.get( 0 ).get( "user_id" ) ).intValue() );
  }

  public String getDisplayName() {
    return items.get( 0 ).get( "display_name" ).toString();
  }

}
