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
package com.eclipsesource.connect.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.eclipsesource.connect.api.asset.AssetsResult;


public class MailAssetsFinderTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Before
  public void setUp() throws IOException {
    folder.newFile( "foo.bar" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullConfiguration() {
    new MailAssetsFinder( null );
  }

  @Test
  public void testFindsResourcesInTemplateFolder() {
    MailConfiguration configuration = mock( MailConfiguration.class );
    when( configuration.getTemplateBasePath() ).thenReturn( folder.getRoot().getAbsolutePath() );
    MailAssetsFinder mailFinder = new MailAssetsFinder( configuration );

    AssetsResult result = mailFinder.find( "/foo.bar" );

    assertThat( result ).isNotNull();
  }

  @Test
  public void testFindsResourcesInTemplateFolderWithoutSeparator() {
    MailConfiguration configuration = mock( MailConfiguration.class );
    when( configuration.getTemplateBasePath() ).thenReturn( folder.getRoot().getAbsolutePath() );
    MailAssetsFinder mailFinder = new MailAssetsFinder( configuration );

    AssetsResult result = mailFinder.find( "foo.bar" );

    assertThat( result ).isNotNull();
  }

  @Test
  public void testReturnsNullIfNotFindResourcesInTemplateFolder() {
    MailConfiguration configuration = mock( MailConfiguration.class );
    when( configuration.getTemplateBasePath() ).thenReturn( folder.getRoot().getAbsolutePath() );
    MailAssetsFinder mailFinder = new MailAssetsFinder( configuration );

    AssetsResult result = mailFinder.find( "/foo2.bar" );

    assertThat( result ).isNull();
  }
}
