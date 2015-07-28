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
package com.eclipsesource.connect.api.search;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;


public class IndexItem {

  private final String content;
  private final Map<String, String> data;

  public IndexItem( String content ) {
    checkArgument( content != null, "Content must not be null" );
    checkArgument( !content.trim().isEmpty(), "Content must not be empty" );
    this.content = content;
    this.data = new HashMap<>();
  }

  public String getContent() {
    return content;
  }

  public void addData( String key, String value ) {
    checkArgument( key != null, "key must not be null" );
    checkArgument( !key.trim().isEmpty(), "key must not be empty" );
    checkArgument( value != null, "value must not be null" );
    data.put( key, value );
  }

  public Map<String, String> getData() {
    return ImmutableMap.copyOf( data );
  }
}
