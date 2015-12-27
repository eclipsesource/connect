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


import static com.google.common.base.Preconditions.checkState;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;

import com.eclipsesource.connect.api.inject.Connect;
import com.eclipsesource.connect.api.inject.ConnectProvider;


@Singleton
public class ConnectValueFactoryProvider extends AbstractValueFactoryProvider {

  @Inject
  public ConnectValueFactoryProvider( MultivaluedParameterExtractorProvider mpep, ServiceLocator injector ) {
    super( mpep, injector, Parameter.Source.UNKNOWN );
  }

  @Override
  protected Factory<?> createValueFactory( Parameter parameter ) {
    if( parameter.isAnnotationPresent( Connect.class ) ) {
      return createConnectValueFactory( parameter );
    }
    return null;
  }

  private Factory<?> createConnectValueFactory( Parameter parameter ) {
    return new AbstractContainerRequestValueFactory<Object>() {

      @Override
      public Object provide() {
        ConnectProviderContainer container = getLocator().getService( ConnectProviderContainer.class );
        checkState( container != null, "ContextProviderContainer not registered" );
        ConnectProvider<?> provider = container.getProvider( parameter.getRawType() );
        return provider.provide( new PlatformResolver( getLocator() ) );
      }

    };
  }

}