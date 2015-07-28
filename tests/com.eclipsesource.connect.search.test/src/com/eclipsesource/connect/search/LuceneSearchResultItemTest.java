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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.junit.Test;


public class LuceneSearchResultItemTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDocument() {
    new LuceneSearchResultItem( null, "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullQuery() {
    new LuceneSearchResultItem( new Document(), null );
  }

  @Test
  public void testReadsContentFromDocument() {
    Document document = new Document();
    document.add( new TextField( Indexer.CONTENT_KEY, "foo", Store.YES ) );
    LuceneSearchResultItem item = new LuceneSearchResultItem( document, "foo" );

    String content = item.getContent();

    assertThat( content ).isEqualTo( "foo" );
  }

  @Test
  public void testReadsDataFromDocument() {
    Document document = new Document();
    document.add( new TextField( "bar", "foo", Store.YES ) );
    LuceneSearchResultItem item = new LuceneSearchResultItem( document, "foo" );

    String data = item.getData( "bar" );

    assertThat( data ).isEqualTo( "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDataKey() {
    Document document = new Document();
    LuceneSearchResultItem item = new LuceneSearchResultItem( document, "foo" );

    item.getData( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyDataKey() {
    Document document = new Document();
    LuceneSearchResultItem item = new LuceneSearchResultItem( document, "foo" );

    item.getData( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceDataKey() {
    Document document = new Document();
    LuceneSearchResultItem item = new LuceneSearchResultItem( document, "foo" );

    item.getData( " " );
  }
}
