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
package com.eclipsesource.connect.mvc.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

import com.eclipsesource.connect.api.asset.AssetsResult;


public class BundleAssetsFinderTest {

  private BundleAssetsFinder finder;
  private Bundle bundle;

  @Before
  public void setUp() {
    bundle = mock( Bundle.class );
    finder = new BundleAssetsFinder( bundle, "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutBundle() {
    new BundleAssetsFinder( null, "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutPath() {
    new BundleAssetsFinder( mock( Bundle.class ), null );
  }

  @Test
  public void testHasBundle() {
    Bundle actualBundle = finder.getBundle();

    assertThat( actualBundle ).isSameAs( bundle );
  }

  @Test
  public void testFindsResource() {
    URL url = BundleAssetsFinderTest.class.getResource( "/test.mustache" );
    when( bundle.getResource( anyString() ) ).thenReturn( url );
    BundleWiring wiring = mock( BundleWiring.class );
    when( wiring.getClassLoader() ).thenReturn( getClass().getClassLoader() );
    when( bundle.adapt( BundleWiring.class ) ).thenReturn( wiring );

    AssetsResult result = finder.find( "test.mustache" );

    assertThat( result.getStream() ).isNotNull();
    assertThat( result.getClassLoader() ).isSameAs( getClass().getClassLoader() );
  }

  @Test
  public void testFindsNoNonExistingResource() {
    AssetsResult result = finder.find( "blub.mustache" );

    assertThat( result ).isNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToFindNullResource() {
    finder.find( null );
  }
}
