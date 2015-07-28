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

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.inject.ConnectProvider;


public class ConnectProviderContainerTest {

  private ConnectProviderContainer container;

  @Before
  public void setUp() {
    container = new ConnectProviderContainer();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testAddFailsWithNullProvider() {
    container.addConnectProvider( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testRemoveFailsWithNullProvider() {
    container.removeConnectProvider( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetFailsWithNullType() {
    container.getProvider( null );
  }
  @Test
  @SuppressWarnings("unchecked")
  public void testFindsProviderForType() {
    ConnectProvider<Object> provider = mock( ConnectProvider.class );
    when( provider.getType() ).thenReturn( Object.class );
    container.addConnectProvider( provider );

    ConnectProvider<Object> actualprovider = container.getProvider( Object.class );

    assertThat( provider ).isSameAs( actualprovider );
  }

  @Test( expected = IllegalStateException.class )
  @SuppressWarnings("unchecked")
  public void testCanRemoveProvider() {
    ConnectProvider<Object> provider = mock( ConnectProvider.class );
    when( provider.getType() ).thenReturn( Object.class );
    container.addConnectProvider( provider );

    container.removeConnectProvider( provider );
    container.getProvider( Object.class );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsWithoutProvider() {
    container.getProvider( Object.class );
  }
}
