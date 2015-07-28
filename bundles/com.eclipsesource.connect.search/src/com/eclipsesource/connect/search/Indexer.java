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
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import com.eclipsesource.connect.api.search.Index;
import com.eclipsesource.connect.api.search.IndexItem;
import com.eclipsesource.connect.api.search.Search.DirectoryStatus;
import com.eclipsesource.connect.api.search.SearchParticipant;


public class Indexer implements Runnable {

  static final String CONTENT_KEY = "_content";

  private final Directory directory;
  private final SearchParticipant participant;

  private final DirectoryStatusHolder statusHolder;

  public Indexer( SearchParticipant participant, DirectoryStatusHolder statusHolder, Directory directory ) {
    checkArgument( participant != null, "SearchParticipant must not be null" );
    checkArgument( statusHolder != null, "DirectoryStatusHolder must not be null" );
    checkArgument( directory != null, "Directory must not be null" );
    this.participant = participant;
    this.statusHolder = statusHolder;
    this.directory = directory;
  }

  @Override
  public void run() {
    statusHolder.changeStatus( participant.getDirectory(), DirectoryStatus.INDEXING );
    IndexWriterConfig conf = new IndexWriterConfig( Version.LUCENE_4_10_4, new StandardAnalyzer() );
    try( IndexWriter indexWriter = new IndexWriter( directory, conf ) ) {
      participant.index( createIndex( indexWriter ) );
      indexWriter.commit();
    } catch( Throwable shouldNotHappen ) {
      statusHolder.changeStatus( participant.getDirectory(), DirectoryStatus.ERROR );
      throw new IllegalStateException( shouldNotHappen );
    }
    statusHolder.changeStatus( participant.getDirectory(), DirectoryStatus.READY );
  }

  private Index createIndex( IndexWriter indexWriter ) {
    return new Index() {

      @Override
      public void addItem( IndexItem item ) {
        Document document = new Document();
        addContent( document, item );
        addData( document, item );
        addDocument( indexWriter, document );
      }

    };
  }

  private void addContent( Document document, IndexItem item ) {
    TextField stringField = new TextField( CONTENT_KEY, item.getContent(), Store.YES );
    document.add( stringField );
  }

  private void addData( Document document, IndexItem item ) {
    Map<String, String> data = item.getData();
    data.entrySet().forEach( entry -> document.add( new TextField( entry.getKey(), entry.getValue(), Store.YES ) ) );
  }

  private void addDocument( IndexWriter indexWriter, Document document ) {
    try {
      indexWriter.addDocument( document );
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }
}
