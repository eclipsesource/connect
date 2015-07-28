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

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;


public class BundleAssetsFinder implements AssetsFinder {

  private final Bundle bundle;
  private final String assetsPath;

  public BundleAssetsFinder( Bundle bundle, String assetsPath ) {
    checkArgument( bundle != null, "Bundle must not be null" );
    checkArgument( assetsPath != null, "AssetsPath must not be null" );
    this.bundle = bundle;
    this.assetsPath = assetsPath;
  }

  public Bundle getBundle() {
    return bundle;
  }

  @Override
  public AssetsResult find( String name ) {
    checkArgument( name != null, "Name must not be null" );
    URL resource = bundle.getResource( assetsPath + name );
    if( resource != null ) {
      try {
        InputStream stream = resource.openStream();
        BundleWiring bundleWiring = bundle.adapt( BundleWiring.class );
        ClassLoader classLoader = bundleWiring.getClassLoader();
        return new AssetsResult( stream, classLoader );
      } catch( IOException shouldNotHappen ) {
        throw new IllegalStateException( shouldNotHappen );
      }
    }
    return null;
  }
}
