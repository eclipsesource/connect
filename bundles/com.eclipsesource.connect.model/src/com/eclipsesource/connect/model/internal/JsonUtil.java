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
package com.eclipsesource.connect.model.internal;

import com.google.gson.Gson;
import com.google.gson.JsonElement;


public class JsonUtil {

  private static final Gson GSON = new Gson();

  public static JsonElement toJson( Object object ) {
    return GSON.toJsonTree( object );
  }

  public static <T> T fromJson( JsonElement element, Class<T> type ) {
    return GSON.fromJson( element, type );
  }
}
