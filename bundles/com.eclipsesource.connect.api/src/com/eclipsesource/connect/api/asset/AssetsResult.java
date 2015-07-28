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
package com.eclipsesource.connect.api.asset;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.InputStream;


public class AssetsResult {

  private final InputStream stream;
  private final ClassLoader classLoader;

  public AssetsResult( InputStream stream, ClassLoader classLoader ) {
    checkArgument( stream != null, "Stream must not be null" );
    checkArgument( classLoader != null, "ClassLoader must not be null" );
    this.stream = stream;
    this.classLoader = classLoader;
  }

  public InputStream getStream() {
    return stream;
  }

  public ClassLoader getClassLoader() {
    return classLoader;
  }

}
