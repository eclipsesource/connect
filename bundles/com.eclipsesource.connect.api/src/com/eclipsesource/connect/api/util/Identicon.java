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

import static com.google.common.base.Preconditions.checkArgument;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.math.BigDecimal.valueOf;
import static java.math.MathContext.DECIMAL64;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

public class Identicon {

  private static final int SIZE = 5;

  private final int width;
  private final int height;
  private final BigDecimal border;

  public Identicon( int width, int height, double border ) {
    checkArgument( width > 0, "Width must be > 0 but was " + width );
    checkArgument( height > 0, "Height must be > 0 but was " + height );
    checkArgument( border >= 0, "Border must be >= 0 but was " + border );
    this.width = width;
    this.height = height;
    this.border = valueOf( border );
  }

  public BufferedImage generate( byte[] hash ) {
    validateHash( hash );
    BufferedImage identicon = new BufferedImage( width, height, TYPE_INT_ARGB );
    Graphics2D graphics = identicon.createGraphics();
    drawIdenticon( graphics, hash, width, height );
    graphics.dispose();
    return identicon;
  }

  private void validateHash( byte[] hash ) {
    checkArgument( hash != null, "Hash must not be null" );
    checkArgument( hash.length > 0, "Hash must not be empty" );
    checkArgument( hash.length > 2, "Hash must be > 2" );
  }

  private void drawIdenticon( Graphics2D graphics, byte[] hash, int width, int height ) {
    Point tile = computeTile( width, height );
    drawBackground( graphics, width, height );
    setForeground( graphics, hash );
    drawTiles( graphics, tile, hash );
  }

  private Point computeTile( int width, int height ) {
    BigDecimal offset = border.multiply( valueOf( 2 ) );
    BigDecimal x = valueOf( width ).divide( offset.add( valueOf( SIZE ) ), DECIMAL64 );
    BigDecimal y = valueOf( width ).divide( offset.add( valueOf( SIZE ) ), DECIMAL64 );
    return new Point( x.intValue(), y.intValue() );
  }

  private void drawBackground( Graphics2D graphics, int width, int height ) {
    graphics.setColor( new Color( 255, 255, 255, 0 ) );
    graphics.fillRect( 0, 0, width, height );
  }

  private void setForeground( Graphics2D graphics, byte[] hash ) {
    Color foreground = new Color( hash[ 0 ] & 255, hash[ 1 ] & 255, hash[ 2 ] & 255, 255 );
    graphics.setColor( foreground );
  }

  private void drawTiles( Graphics2D graphics, Point tile, byte[] hash ) {
    int xOffset = border.multiply( valueOf( tile.x ) ).intValue();
    int yOffset = border.multiply( valueOf( tile.y ) ).intValue();
    for( int x = 0; x < SIZE; x++ ) {
      int selector = x < 3 ? x : 4 - x;
      for( int y = 0; y < SIZE; y++ ) {
        if( ( hash[ selector ] >> y & 1 ) == 1 ) {
          graphics.fillRect( x * tile.x + xOffset, y * tile.y + yOffset, tile.x, tile.y );
        }
      }
    }
  }

}