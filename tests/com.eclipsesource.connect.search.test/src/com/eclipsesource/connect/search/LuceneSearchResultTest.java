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
package com.eclipsesource.connect.search;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.lucene.document.Document;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.search.SearchResultItem;


public class LuceneSearchResultTest {

  private LuceneSearchResult result;

  @Before
  public void setUp() {
    result = new LuceneSearchResult( "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullQuery() {
    new LuceneSearchResult( null );
  }

  @Test
  public void testHasQuery() {
    String query = result.getQuery();

    assertThat( query ).isEqualTo( "foo" );
  }

  @Test
  public void testSavesTotalHits() {
    result.setTotalHits( 23 );

    int totalHits = result.getTotalHits();

    assertThat( totalHits ).isEqualTo( 23 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNegativeTotalHits() {
    result.setTotalHits( -1 );
  }

  @Test
  public void testAddsDocumentAsItem() {
    result.addDocument( new Document() );

    List<SearchResultItem> items = result.getItems();

    assertThat( items ).hasSize( 1 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddNullDocument() {
    result.addDocument( null );
  }
}
