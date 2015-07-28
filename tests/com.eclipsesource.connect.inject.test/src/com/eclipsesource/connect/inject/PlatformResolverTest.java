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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.connect.inject.ConnectResolver.Binder;
import com.eclipsesource.connect.inject.ConnectResolver.DynamicContextInjectionResolver;


public class PlatformResolverTest {

  @Test( expected = IllegalArgumentException.class )
  public void testBinderFailsWithNullContainer() {
    new ConnectResolver.Binder( null );
  }

  @Test
  public void testBindsServices() {
    Binder binder = new ConnectResolver.Binder( new ConnectProviderContainer() );
    DynamicConfiguration configuration = mock( DynamicConfiguration.class );
    binder.bind( configuration );

    ArgumentCaptor<Descriptor> captor = ArgumentCaptor.forClass( Descriptor.class );
    verify( configuration, times( 3 ) ).bind( captor.capture(), eq( false ) );

    assertThat( captor.getAllValues().get( 0 ).getImplementation() ).contains( ConnectProviderContainer.class.getName() );
    assertThat( captor.getAllValues().get( 1 ).getImplementation() ).isEqualTo( ConnectValueFactoryProvider.class.getName() );
    assertThat( captor.getAllValues().get( 2 ).getImplementation() ).isEqualTo( DynamicContextInjectionResolver.class.getName() );
  }
}
