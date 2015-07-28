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

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import com.eclipsesource.connect.api.search.SearchResult;


public class Searcher {

  private final Directory directory;

  public Searcher( Directory directory ) {
    checkArgument( directory != null, "Directory must not be null" );
    this.directory = directory;
  }

  public SearchResult search( String query ) {
    checkArgument( query != null, "Query must not be null" );
    LuceneSearchResult searchResult = new LuceneSearchResult( query );
    performSearch( searchResult, query );
    return searchResult;
  }

  private void performSearch( LuceneSearchResult searchResult, String query ) {
    try( DirectoryReader reader = DirectoryReader.open( directory ) ) {
      IndexSearcher searcher = new IndexSearcher( reader );
      FuzzyQuery luceneQuery = new FuzzyQuery( new Term( Indexer.CONTENT_KEY, query ) );
      TopDocs topDocs = searcher.search( luceneQuery, Integer.MAX_VALUE );
      populateSearchResult( searchResult, reader, topDocs );
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private void populateSearchResult( LuceneSearchResult searchResult, DirectoryReader reader, TopDocs topDocs ) throws IOException {
    searchResult.setTotalHits( topDocs.totalHits );
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    for( ScoreDoc scoreDoc : scoreDocs ) {
      Document foundDocument = reader.document( scoreDoc.doc );
      searchResult.addDocument( foundDocument );
    }
  }
}
