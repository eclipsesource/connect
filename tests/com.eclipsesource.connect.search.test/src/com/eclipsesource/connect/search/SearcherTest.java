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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.search.Index;
import com.eclipsesource.connect.api.search.IndexItem;
import com.eclipsesource.connect.api.search.SearchParticipant;
import com.eclipsesource.connect.api.search.SearchResult;


public class SearcherTest {

  private Searcher searcher;

  @Before
  public void setUp() {
    Map<String, String> data = new HashMap<>();
    data.put( "bar", "bar1" );
    RAMDirectory directory = new RAMDirectory();
    Indexer indexer = new Indexer( createParticipant( "foo", data, "foo1", "foo2" ), new DirectoryStatusHolder(), directory );
    indexer.run();
    searcher = new Searcher( directory );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDirectory() {
    new Searcher( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullQuery() {
    searcher.search( null );
  }

  @Test
  public void testSearchesDirectory() {
    SearchResult result = searcher.search( "foo" );

    assertThat( result.getTotalHits() ).isEqualTo( 2 );
    assertThat( result.getItems().get( 0 ).getContent() ).isEqualTo( "foo1" );
    assertThat( result.getItems().get( 1 ).getContent() ).isEqualTo( "foo2" );
  }

  private SearchParticipant createParticipant( String directory, Map<String, String> data, String... contents ) {
    return new SearchParticipant() {

      @Override
      public void index( Index index ) {
        Stream.of( contents ).forEach( content -> {
          IndexItem item = new IndexItem( content );
          data.entrySet().forEach( entry -> item.addData( entry.getKey(), entry.getValue() ) );
          index.addItem( item );
        } );
      }

      @Override
      public String getDirectory() {
        return directory;
      }
    };
  }
}
