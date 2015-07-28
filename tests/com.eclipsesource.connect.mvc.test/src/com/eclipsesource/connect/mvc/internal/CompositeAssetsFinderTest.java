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
import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.BundleWiring;

import com.eclipsesource.connect.api.asset.AssetsResult;


public class CompositeAssetsFinderTest {

  private CompositeAssetsFinder finder;

  @Before
  public void setUp() {
    BundleContext context = mock( BundleContext.class );
    finder = new CompositeAssetsFinder( context );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToFindNulLResource() {
    finder.find( null );
  }

  @Test
  public void testAddsAssetsOfBundles() {
    Dictionary<String, String> headers = new Hashtable<>();
    headers.put( "Assets", "/" );
    Bundle bundle = createBundle( headers );
    finder.addingBundle( bundle, new BundleEvent( BundleEvent.INSTALLED, bundle ) );

    AssetsResult result = finder.find( "test.mustache" );

    assertThat( result ).isNotNull();
  }

  @Test
  public void testAddsAndRemovesAssetsOfBundles() {
    Dictionary<String, String> headers = new Hashtable<>();
    headers.put( "Assets", "/" );
    Bundle bundle = createBundle( headers );
    finder.addingBundle( bundle, new BundleEvent( BundleEvent.INSTALLED, bundle ) );

    finder.removedBundle( bundle, new BundleEvent( BundleEvent.UNINSTALLED, bundle ), new Object() );

    AssetsResult result = finder.find( "test.mustache" );
    assertThat( result ).isNull();
  }

  @Test
  public void testDeactivateClearsFinders() {
    Dictionary<String, String> headers = new Hashtable<>();
    headers.put( "Assets", "/" );
    Bundle bundle = createBundle( headers );
    finder.addingBundle( bundle, new BundleEvent( BundleEvent.INSTALLED, bundle ) );

    finder.deactivate();

    AssetsResult result = finder.find( "test.mustache" );
    assertThat( result ).isNull();
  }

  @Test
  public void testAddsNoBundleIfNoAssets() {
    Dictionary<String, String> headers = new Hashtable<>();
    Bundle bundle = createBundle( headers );
    finder.addingBundle( bundle, new BundleEvent( BundleEvent.INSTALLED, bundle ) );

    AssetsResult result = finder.find( "test.mustache" );

    assertThat( result ).isNull();
  }

  private Bundle createBundle( Dictionary<String, String> headers ) {
    Bundle bundle = mock( Bundle.class );
    when( bundle.getBundleId() ).thenReturn( 23L );
    URL url = BundleAssetsFinderTest.class.getResource( "/test.mustache" );
    when( bundle.getResource( anyString() ) ).thenReturn( url );
    BundleWiring wiring = mock( BundleWiring.class );
    when( wiring.getClassLoader() ).thenReturn( getClass().getClassLoader() );
    when( bundle.adapt( BundleWiring.class ) ).thenReturn( wiring );
    when( bundle.getHeaders() ).thenReturn( headers );
    return bundle;
  }
}
