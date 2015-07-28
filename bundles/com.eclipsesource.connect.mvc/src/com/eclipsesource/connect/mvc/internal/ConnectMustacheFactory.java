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

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkArgument;

import java.io.InputStreamReader;
import java.io.Reader;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;
import com.github.mustachejava.DefaultMustacheFactory;


public class ConnectMustacheFactory extends DefaultMustacheFactory {

  private final MustacheTemplateProcessor templateProcessor;

  public ConnectMustacheFactory( MustacheTemplateProcessor templateProcessor ) {
    checkArgument( templateProcessor != null, "template processor must not be null" );
    this.templateProcessor = templateProcessor;
  }

  @Override
  public Reader getReader( String resourceName ) {
    AssetsFinder assetsHandler = templateProcessor.getAssetsFinder();
    AssetsResult result = assetsHandler.find( resourceName );
    if( result != null ) {
      return new InputStreamReader( result.getStream(), UTF_8 );
    }
    return super.getReader( resourceName );
  }
}
