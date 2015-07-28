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

import javax.inject.Singleton;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import com.eclipsesource.connect.api.inject.Connect;


public class ConnectResolver {

  // InjectionResolver implementation
  public static class DynamicContextInjectionResolver extends ParamInjectionResolver<Connect> {

    public DynamicContextInjectionResolver() {
      super( ConnectValueFactoryProvider.class );
    }
  }

  public static class Binder extends AbstractBinder {

    private final ConnectProviderContainer container;

    public Binder( ConnectProviderContainer container ) {
      checkArgument( container != null, "ConnectProviderContainer must not be null" );
      this.container = container;
    }

    @Override
    protected void configure() {
      bind( container ).to( ConnectProviderContainer.class );
      bind( ConnectValueFactoryProvider.class ).to( ValueFactoryProvider.class ).in( Singleton.class );
      bind( DynamicContextInjectionResolver.class ).to( new TypeLiteral<InjectionResolver<Connect>>() { } ).in( Singleton.class );
    }
  }
}