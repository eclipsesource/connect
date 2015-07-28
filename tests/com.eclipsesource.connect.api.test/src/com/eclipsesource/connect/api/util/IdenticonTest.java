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

import static com.google.common.base.Charsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.util.Identicon;
import com.google.common.hash.Hashing;


public class IdenticonTest {

  private Identicon identicon;

  @Before
  public void setUp() {
    identicon = new Identicon( 10, 10, 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeWidth() {
    new Identicon( -1, 1, 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithZeroWidth() {
    new Identicon( 0, 1, 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeHeight() {
    new Identicon( 1, -1, 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithZeroHeight() {
    new Identicon( 1, 0, 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNegativeBorder() {
    new Identicon( 1, 1, -1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullHash() {
    identicon.generate( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyHash() {
    identicon.generate( new byte[] {} );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithToShortHash() {
    identicon.generate( new byte[] { 1, 2 } );
  }

  @Test
  public void testGeneratesIdenticon() {
    byte[] hash = Hashing.sha256().hashString( "test", UTF_8 ).asBytes();

    BufferedImage image = identicon.generate( hash );

    assertThat( image ).isNotNull();
  }

  @Test
  public void testGeneratesSameIdenticonForSameHash() {
    byte[] hash = Hashing.sha256().hashString( "test", UTF_8 ).asBytes();

    BufferedImage image = identicon.generate( hash );
    BufferedImage image2 = identicon.generate( hash );

    assertThat( getImageBytes( image ) ).isEqualTo( getImageBytes( image2 ) );
  }

  @Test
  public void testGeneratesDifferentIdenticonForDifferentHash() {
    byte[] hash = Hashing.sha256().hashString( "test", UTF_8 ).asBytes();
    byte[] hash2 = Hashing.sha256().hashString( "test2", UTF_8 ).asBytes();

    BufferedImage image = identicon.generate( hash );
    BufferedImage image2 = identicon.generate( hash2 );

    assertThat( getImageBytes( image ) ).isNotEqualTo( getImageBytes( image2 ) );
  }

  private byte[] getImageBytes( BufferedImage image ) {
    try( ByteArrayOutputStream stream = new ByteArrayOutputStream() ) {
      ImageIO.write( image, "png", stream );
      return stream.toByteArray();
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

}
