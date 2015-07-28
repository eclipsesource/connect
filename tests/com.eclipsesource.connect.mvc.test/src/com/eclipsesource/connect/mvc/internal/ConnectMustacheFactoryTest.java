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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import com.eclipsesource.connect.api.asset.AssetsFinder;


public class ConnectMustacheFactoryTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullProcessor() {
    new ConnectMustacheFactory( null );
  }

  @Test
  public void testLoadsFromAssetsFinderFirst() throws IOException {
    MustacheTemplateProcessor processor = new MustacheTemplateProcessor();
    AssetsFinder finder = spy( new TestAssetsFinder() );
    processor.setAssetsFinder( finder );
    ConnectMustacheFactory factory = new ConnectMustacheFactory( processor );

    Reader reader = factory.getReader( "/test.mustache" );

    String readContent = read( reader );
    assertThat( readContent ).isEqualTo( "test {{foo}}" );
    verify( finder ).find( "/test.mustache" );
  }

  @Test
  public void testUsesParentAsFallback() throws IOException {
    MustacheTemplateProcessor processor = new MustacheTemplateProcessor();
    AssetsFinder finder = mock( AssetsFinder.class );
    processor.setAssetsFinder( finder );
    ConnectMustacheFactory factory = new ConnectMustacheFactory( processor );

    Reader reader = factory.getReader( "test.mustache" );

    String readContent = read( reader );
    assertThat( readContent ).isEqualTo( "test {{foo}}" );
    verify( finder ).find( "test.mustache" );
  }

  private String read( Reader reader ) throws IOException {
    StringBuilder builder = new StringBuilder();
    BufferedReader bufferedReader = new BufferedReader( reader );
    String read = bufferedReader.readLine();
    while( read != null ) {
      builder.append( read );
      read = bufferedReader.readLine();
    }
    return builder.toString();
  }
}
