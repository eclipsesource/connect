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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.inject.Singleton;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;
import org.junit.Test;

import com.eclipsesource.connect.api.inject.Connect;
import com.eclipsesource.connect.api.inject.ConnectProvider;
import com.eclipsesource.connect.api.inject.Resolver;


public class ConnectValueFactoryProviderTest {

  @Test
  public void testHasSingletonAnnotation() {
    Singleton annotation = ConnectValueFactoryProvider.class.getAnnotation( Singleton.class );

    assertThat( annotation ).isNotNull();
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutContainer() {
    MultivaluedParameterExtractorProvider provider = mock( MultivaluedParameterExtractorProvider.class );
    ServiceLocator injector = mock( ServiceLocator.class );
    ConnectValueFactoryProvider valueFactoryProvider = new ConnectValueFactoryProvider( provider, injector );
    Parameter parameter = mockParameter( true );

    valueFactoryProvider.createValueFactory( parameter ).provide();
  }

  @Test
  public void testResolvesWithFactory() {
    Object object = new Object();
    MultivaluedParameterExtractorProvider provider = mock( MultivaluedParameterExtractorProvider.class );
    ServiceLocator injector = mockServiceLocator( object );
    ConnectValueFactoryProvider valueFactoryProvider = new ConnectValueFactoryProvider( provider, injector );
    Parameter parameter = mockParameter( true );

    Object provided = valueFactoryProvider.createValueFactory( parameter ).provide();

    assertThat( provided ).isSameAs( object );
  }

  @Test
  public void testCreatesNullFactoryForNonConnectParam() {
    Object object = new Object();
    MultivaluedParameterExtractorProvider provider = mock( MultivaluedParameterExtractorProvider.class );
    ServiceLocator injector = mockServiceLocator( object );
    ConnectValueFactoryProvider valueFactoryProvider = new ConnectValueFactoryProvider( provider, injector );
    Parameter parameter = mockParameter( false );

    Factory<?> factory = valueFactoryProvider.createValueFactory( parameter );

    assertThat( factory ).isNull();
  }

  private Parameter mockParameter( boolean isConnectParam ) {
    Parameter parameter = mock( Parameter.class );
    doReturn( Object.class ).when( parameter ).getRawType();
    when( parameter.isAnnotationPresent( Connect.class ) ).thenReturn( isConnectParam );
    return parameter;
  }

  private ServiceLocator mockServiceLocator( Object object ) {
    ConnectProviderContainer container = new ConnectProviderContainer();
    container.addConnectProvider( createProvider( object ) );

    ServiceLocator injector = mock( ServiceLocator.class );
    when( injector.getService( ConnectProviderContainer.class ) ).thenReturn( container );
    return injector;
  }

  @SuppressWarnings("unchecked")
  private ConnectProvider<?> createProvider( Object object ) {
    ConnectProvider<Object> provider = mock( ConnectProvider.class );
    when( provider.getType() ).thenReturn( Object.class );
    when( provider.provide( any( Resolver.class ) ) ).thenReturn( object );
    return provider;
  }
}
