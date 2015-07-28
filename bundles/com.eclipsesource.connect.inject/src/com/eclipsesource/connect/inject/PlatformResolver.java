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
package com.eclipsesource.connect.inject;

import static com.google.common.base.Preconditions.checkArgument;

import org.glassfish.hk2.api.ServiceLocator;

import com.eclipsesource.connect.api.inject.Resolver;


public class PlatformResolver implements Resolver {

  private final ServiceLocator locator;

  public PlatformResolver( ServiceLocator locator ) {
    checkArgument( locator != null, "ServiceLocator must not be null" );
    this.locator = locator;
  }

  @Override
  public <T> T resolve( Class<T> type ) {
    return locator.getService( type );
  }
}
