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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.FeatureContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.eclipsesource.connect.inject.ConnectResolver.Binder;


public class ConnectInjectionFeatureTest {

  private ConnectInjectionFeature feature;

  @Before
  public void setUp() {
    feature = new ConnectInjectionFeature();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddNullProvider() {
    feature.addConnectProvider( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToRemoveNullProvider() {
    feature.removeConnectProvider( null );
  }

  @Test
  public void testRegistersBinder() {
    ArgumentCaptor<Object> captor = ArgumentCaptor.forClass( Object.class );
    FeatureContext context = mock( FeatureContext.class );
    when( context.register( captor.capture() ) ).thenReturn( context );

    feature.configure( context );

    assertThat( captor.getValue() ).isInstanceOf( Binder.class );
  }
}
