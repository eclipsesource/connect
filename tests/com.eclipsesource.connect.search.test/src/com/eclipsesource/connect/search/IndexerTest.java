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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.eclipsesource.connect.api.search.Index;
import com.eclipsesource.connect.api.search.IndexItem;
import com.eclipsesource.connect.api.search.Search.DirectoryStatus;
import com.eclipsesource.connect.api.search.SearchParticipant;


public class IndexerTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDirectory() {
    new Indexer( mock( SearchParticipant.class ), new DirectoryStatusHolder(), null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullParticipant() {
    new Indexer( null, new DirectoryStatusHolder(), mock( Directory.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullStatusHolder() {
    new Indexer( mock( SearchParticipant.class ), null, mock( Directory.class ) );
  }

  @Test
  public void testUsesParticipantForIndexing() {
    SearchParticipant participant = mock( SearchParticipant.class );
    when( participant.getDirectory() ).thenReturn( "foo" );
    Indexer indexer = new Indexer( participant, new DirectoryStatusHolder(), new RAMDirectory() );

    indexer.run();

    verify( participant ).index( any( Index.class ) );
  }

  @Test
  public void testFillsIndex() {
    Map<String, String> data = new HashMap<>();
    data.put( "bar", "bar1" );
    RAMDirectory directory = new RAMDirectory();
    Indexer indexer = new Indexer( createParticipant( "foo", data, "foo1", "foo2" ), new DirectoryStatusHolder(), directory );

    indexer.run();

    assertThat( directory.listAll() ).isNotEmpty();
  }

  @Test
  public void testRunChangesStatus() {
    Map<String, String> data = new HashMap<>();
    RAMDirectory directory = new RAMDirectory();
    DirectoryStatusHolder statusHolder = mock( DirectoryStatusHolder.class );
    Indexer indexer = new Indexer( createParticipant( "foo", data, "foo1", "foo2" ), statusHolder, directory );

    indexer.run();

    verify( statusHolder ).changeStatus( "foo", DirectoryStatus.INDEXING );
    verify( statusHolder ).changeStatus( "foo", DirectoryStatus.READY );
  }

  @Test
  public void testRunFailsAndChangesStatus() {
    RAMDirectory directory = new RAMDirectory();
    DirectoryStatusHolder statusHolder = mock( DirectoryStatusHolder.class );
    Indexer indexer = new Indexer( createFailingParticipant( "foo" ), statusHolder, directory );

    try {
      indexer.run();
    } catch( IllegalStateException expected ) {}

    verify( statusHolder ).changeStatus( "foo", DirectoryStatus.ERROR );
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

  private SearchParticipant createFailingParticipant( String directory ) {
    return new SearchParticipant() {

      @Override
      public void index( Index index ) {
        throw new IllegalStateException();
      }

      @Override
      public String getDirectory() {
        return directory;
      }
    };
  }
}
