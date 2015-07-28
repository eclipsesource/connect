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
package com.eclipsesource.connect.api.search;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;


public class IndexItemTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullContent() {
    new IndexItem( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyContent() {
    new IndexItem( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceContent() {
    new IndexItem( " " );
  }

  @Test
  public void testHasContent() {
    IndexItem item = new IndexItem( "foo" );

    String content = item.getContent();

    assertThat( content ).isEqualTo( "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDataKey() {
    IndexItem item = new IndexItem( "foo" );

    item.addData( null, "bar2" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyDataKey() {
    IndexItem item = new IndexItem( "foo" );

    item.addData( "", "bar2" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceDataKey() {
    IndexItem item = new IndexItem( "foo" );

    item.addData( " ", "bar2" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDataValue() {
    IndexItem item = new IndexItem( "foo" );

    item.addData( "foo", null );
  }

  @Test
  public void testHasData() {
    IndexItem item = new IndexItem( "foo" );
    item.addData( "bar", "bar2" );

    String value = item.getData().get( "bar" );

    assertThat( value ).isEqualTo( "bar2" );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testDataIsImmutable() {
    IndexItem item = new IndexItem( "foo" );
    item.addData( "bar", "bar2" );

    Map<String, String> data = item.getData();

    data.put( "foo", "bar" );
  }
}
