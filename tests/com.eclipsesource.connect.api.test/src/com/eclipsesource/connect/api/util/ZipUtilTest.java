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
package com.eclipsesource.connect.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class ZipUtilTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullUrl() throws IOException {
    File target = folder.newFolder();

    ZipUtil.unpackArchive( null, target );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTarget() throws IOException {
    ZipUtil.unpackArchive( ZipUtilTest.class.getResource( "test.zip" ).toString(), null );
  }

  @Test
  public void testUnzipsArchive() throws IOException {
    File target = folder.newFolder();

    ZipUtil.unpackArchive( ZipUtilTest.class.getResource( "test.zip" ).toString(), target );

    assertThat( new File( target, "package.json" ) ).exists();
    assertThat( new File( target, "App.js" ) ).exists();
  }

}
