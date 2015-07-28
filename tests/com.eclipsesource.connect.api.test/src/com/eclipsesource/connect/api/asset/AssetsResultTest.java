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
package com.eclipsesource.connect.api.asset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.InputStream;

import org.junit.Test;


public class AssetsResultTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullStream() {
    new AssetsResult( null, mock( ClassLoader.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullClassLoader() {
    new AssetsResult( mock( InputStream.class ), null );
  }

  @Test
  public void testHasStream() {
    InputStream stream = mock( InputStream.class );
    ClassLoader classLoader = mock( ClassLoader.class );
    AssetsResult result = new AssetsResult( stream, classLoader );

    InputStream actualStream = result.getStream();

    assertThat( actualStream ).isSameAs( stream );
  }

  @Test
  public void testHasClassLoader() {
    InputStream stream = mock( InputStream.class );
    ClassLoader classLoader = mock( ClassLoader.class );
    AssetsResult result = new AssetsResult( stream, classLoader );

    ClassLoader actualClassLoader = result.getClassLoader();

    assertThat( actualClassLoader ).isSameAs( classLoader );
  }
}
