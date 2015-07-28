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

import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.connect.api.inject.ConnectProvider;


public class ConnectProviderContainer {

  private final List<ConnectProvider<?>> providers;

  public ConnectProviderContainer() {
    this.providers = new ArrayList<>();
  }

  void addConnectProvider( ConnectProvider<?> provider ) {
    checkArgument( provider != null, "ConnectProvider must not be null" );
    providers.add( provider );
  }

  void removeConnectProvider( ConnectProvider<?> provider ) {
    checkArgument( provider != null, "ConnectProvider must not be null" );
    providers.remove( provider );
  }

  @SuppressWarnings("unchecked")
  public <T> ConnectProvider<T> getProvider( Class<T> type ) {
    checkArgument( type != null, "Type must not be null" );
    for( ConnectProvider<?> provider : providers ) {
      if( provider.getType() == type ) {
        return ( ConnectProvider<T> )provider;
      }
    }
    throw new IllegalStateException( "No ConnectProvider registered for type " + type.getName() );
  }
}
