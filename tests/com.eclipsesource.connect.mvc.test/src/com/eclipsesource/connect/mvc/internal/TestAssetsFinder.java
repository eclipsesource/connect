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
package com.eclipsesource.connect.mvc.internal;

import java.io.InputStream;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;

public class TestAssetsFinder implements AssetsFinder {

  @Override
  public AssetsResult find( String name ) {
    InputStream stream = MustacheTemplateProcessorTest.class.getResourceAsStream( name );
    if( stream != null ) {
      return new AssetsResult( stream, TestAssetsFinder.class.getClassLoader() );
    }
    return null;
  }

}