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
import java.util.concurrent.ExecutorService;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.eclipsesource.connect.api.search.Search;
import com.eclipsesource.connect.api.search.SearchParticipant;
import com.eclipsesource.connect.api.search.SearchResult;
import com.eclipsesource.connect.api.util.CallbackExecutorBuilder;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


public class LuceneSearch implements Search {

  private final LoadingCache<String, Directory> directories;
  private final ExecutorService executor;
  private final DirectoryStatusHolder statusHolder;

  public LuceneSearch() {
    this( CallbackExecutorBuilder.newBuilder()
                                 .setName( "LuceneSearch" )
                                 .setFailureCallback( exception -> { exception.printStackTrace(); } )
                                 .createCachedThreadPool() );
  }

  LuceneSearch( ExecutorService executor ) {
    checkArgument( executor != null, "Executor must not be null" );
    this.executor = executor;
    this.directories = createDirectoryCache();
    this.statusHolder = new DirectoryStatusHolder();
  }

  private LoadingCache<String, Directory> createDirectoryCache() {
    return CacheBuilder.newBuilder().build( new CacheLoader<String, Directory>() {

      @Override
      public Directory load( String key ) throws Exception {
        return new RAMDirectory();
      }
    } );
  }

  @Override
  public SearchResult search( String directory, String query ) {
    checkArgument( directory != null, "Directory must not be null" );
    checkArgument( !directory.trim().isEmpty(), "Directory must not be empty" );
    checkArgument( query != null, "Query must not be null" );
    return doSearch( directory, query );
  }

  private SearchResult doSearch( String directory, String query ) {
    Directory searchDirectory = directories.getUnchecked( directory );
    Searcher searcher = new Searcher( searchDirectory );
    return searcher.search( query );
  }

  void addSearchParticipant( SearchParticipant participant ) {
    statusHolder.changeStatus( participant.getDirectory(), DirectoryStatus.NOT_READY );
    Indexer indexer = new Indexer( participant, statusHolder, directories.getUnchecked( participant.getDirectory() ) );
    executor.submit( indexer );
  }

  void removeSearchParticipant( SearchParticipant participant ) {
    Directory directory = directories.getUnchecked( participant.getDirectory() );
    try {
      directory.close();
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
    directories.invalidate( participant.getDirectory() );
    statusHolder.changeStatus( participant.getDirectory(), DirectoryStatus.REMOVED );
  }

  @Override
  public DirectoryStatus getStatus( String directory ) {
    checkArgument( directory != null, "Directory must not be null" );
    checkArgument( !directory.trim().isEmpty(), "Directory must not be empty" );
    return statusHolder.getStatus( directory );
  }
}
