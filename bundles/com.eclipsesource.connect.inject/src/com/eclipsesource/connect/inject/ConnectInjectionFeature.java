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


import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import com.eclipsesource.connect.api.inject.ConnectProvider;


public class ConnectInjectionFeature implements Feature {

  private final ConnectProviderContainer container;

  public ConnectInjectionFeature() {
    container = new ConnectProviderContainer();
  }

  @Override
  public boolean configure( FeatureContext context ) {
    context.register( new ConnectResolver.Binder( container ) );
    return true;
  }

  void addConnectProvider( ConnectProvider<?> provider ) {
    container.addConnectProvider( provider );
  }

  void removeConnectProvider( ConnectProvider<?> provider ) {
    container.removeConnectProvider( provider );
  }
}
