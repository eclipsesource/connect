/*******************************************import static com.google.common.base.Preconditions.checkArgument;

import org.apache.lucene.document.Document;

import com.eclipsesource.connect.api.search.SearchResultItem;
 under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.search;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.lucene.document.Document;

import com.eclipsesource.connect.api.search.SearchResultItem;


public class LuceneSearchResultItem implements SearchResultItem {

  private final Document document;
  private final String query;

  public LuceneSearchResultItem( Document document, String query ) {
    checkArgument( document != null, "Document must not be null" );
    checkArgument( query != null, "Query must not be null" );
    this.document = document;
    this.query = query;
  }

  @Override
  public String getContent() {
    return document.get( Indexer.CONTENT_KEY );
  }

  @Override
  public String getData( String key ) {
    checkArgument( key != null, "key must not be null" );
    checkArgument( !key.trim().isEmpty(), "key must not be empty" );
    return document.get( key );
  }

  @Override
  public String getQuery() {
    return query;
  }
}
