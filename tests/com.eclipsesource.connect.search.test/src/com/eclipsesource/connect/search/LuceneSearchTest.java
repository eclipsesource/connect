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
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.search.Index;
import com.eclipsesource.connect.api.search.IndexItem;
import com.eclipsesource.connect.api.search.Search.DirectoryStatus;
import com.eclipsesource.connect.api.search.SearchParticipant;
import com.eclipsesource.connect.api.search.SearchResult;
import com.google.common.util.concurrent.MoreExecutors;


public class LuceneSearchTest {

  private LuceneSearch search;

  @Before
  public void setUp() {
    search = new LuceneSearch( MoreExecutors.newDirectExecutorService() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullExecutor() {
    new LuceneSearch( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSearchFailsWithNullDirectory() {
    search.search( null, "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSearchFailsWithEmptyDirectory() {
    search.search( "", "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSearchFailsWithEmptyWhitespaceDirectory() {
    search.search( " ", "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSearchFailsWithNullQuery() {
    search.search( "foo", null );
  }

  @Test
  public void testSearchFindsExactContent() {
    search.addSearchParticipant( createParticipant( "foo", "foo1", "bar2" ) );

    SearchResult result = search.search( "foo", "foo1" );

    assertThat( result.getTotalHits() ).isEqualTo( 1 );
    assertThat( result.getItems().get( 0 ).getContent() ).isEqualTo( "foo1" );
  }

  @Test
  public void testSearchFindsNoContent() {
    search.addSearchParticipant( createParticipant( "foo", "foo1", "bar2" ) );

    SearchResult result = search.search( "foo", "blub" );

    assertThat( result.getTotalHits() ).isEqualTo( 0 );
    assertThat( result.getItems() ).isEmpty();
  }

  @Test
  public void testSearchFindsMatchingContent() {
    search.addSearchParticipant( createParticipant( "foo", "foo1", "foo2" ) );

    SearchResult result = search.search( "foo", "foo" );

    assertThat( result.getTotalHits() ).isEqualTo( 2 );
    assertThat( result.getItems().get( 0 ).getContent() ).isEqualTo( "foo1" );
    assertThat( result.getItems().get( 1 ).getContent() ).isEqualTo( "foo2" );
  }

  @Test
  public void testStoresDataDuringIndexing() {
    Map<String, String> data = new HashMap<>();
    data.put( "bar", "bar1" );
    search.addSearchParticipant( createParticipant( "foo", data, "foo1", "foo2" ) );

    SearchResult result = search.search( "foo", "foo" );

    assertThat( result.getTotalHits() ).isEqualTo( 2 );
    assertThat( result.getItems().get( 0 ).getContent() ).isEqualTo( "foo1" );
    assertThat( result.getItems().get( 0 ).getData( "bar" ) ).isEqualTo( "bar1" );
    assertThat( result.getItems().get( 1 ).getContent() ).isEqualTo( "foo2" );
    assertThat( result.getItems().get( 1 ).getData( "bar" ) ).isEqualTo( "bar1" );
  }

  @Test
  public void testRemoveParticipantDeletesDirectory() {
    SearchParticipant participant = createParticipant( "foo", "foo1", "bar2" );
    search.addSearchParticipant( participant );
    search.removeSearchParticipant( participant );
    search.addSearchParticipant( createParticipant( "foo", "bar1", "bar2" ) );

    SearchResult result = search.search( "foo", "foo1" );

    assertThat( result.getTotalHits() ).isEqualTo( 0 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToGetStatusWithNullDirectory() {
    search.getStatus( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToGetStatusWithEmptyDirectory() {
    search.getStatus( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToGetStatusWithEmptyWhitespaceDirectory() {
    search.getStatus( " " );
  }

  @Test
  public void testAddParticipantChangesStatus() {
    search = new LuceneSearch( mock( ExecutorService.class ) );
    SearchParticipant participant = createParticipant( "foo", "foo1", "bar2" );
    search.addSearchParticipant( participant );

    DirectoryStatus status = search.getStatus( "foo" );

    assertThat( status ).isSameAs( DirectoryStatus.NOT_READY );
  }

  @Test
  public void testRemoveParticipantChangesStatus() {
    SearchParticipant participant = createParticipant( "foo", "foo1", "bar2" );
    search.addSearchParticipant( participant );
    search.removeSearchParticipant( participant );

    DirectoryStatus status = search.getStatus( "foo" );

    assertThat( status ).isSameAs( DirectoryStatus.REMOVED );
  }

  private SearchParticipant createParticipant( String directory, String... contents ) {
    return new SearchParticipant() {

      @Override
      public void index( Index index ) {
        Stream.of( contents ).forEach( content -> index.addItem( new IndexItem( content ) ) );
      }

      @Override
      public String getDirectory() {
        return directory;
      }
    };
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
