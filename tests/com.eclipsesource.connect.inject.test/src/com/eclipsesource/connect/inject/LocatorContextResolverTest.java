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

import org.glassfish.hk2.api.ServiceLocator;
import org.junit.Test;


public class LocatorContextResolverTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullLocator() {
    new PlatformResolver( null );
  }

  @Test
  public void testUsesLoctorToResolve() {
    ServiceLocator locator = mock( ServiceLocator.class );
    Object object = new Object();
    when( locator.getService( Object.class ) ).thenReturn( object );
    PlatformResolver resolver = new PlatformResolver( locator );

    Object resolved = resolver.resolve( Object.class );

    assertThat( resolved ).isSameAs( object );
  }
}
