/******************************************import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;

import com.eclipsesource.connect.api.search.SearchResult;
import com.eclipsesource.connect.api.search.SearchResultItem;
import com.google.common.collect.ImmutableList;
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.search;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;

import com.eclipsesource.connect.api.search.SearchResult;
import com.eclipsesource.connect.api.search.SearchResultItem;
import com.google.common.collect.ImmutableList;


public class LuceneSearchResult implements SearchResult {

  private int totalHits;
  private final List<SearchResultItem> items;
  private final String query;

  public LuceneSearchResult( String query ) {
    checkArgument( query != null, "Query must not be null" );
    this.query = query;
    this.totalHits = 0;
    this.items = new ArrayList<>();
  }

  @Override
  public String getQuery() {
    return query;
  }

  @Override
  public int getTotalHits() {
    return totalHits;
  }

  @Override
  public List<SearchResultItem> getItems() {
    return ImmutableList.copyOf( items );
  }

  public void setTotalHits( int totalHits ) {
    checkArgument( totalHits >= 0, "Total Hits must be >= 0 but was " + totalHits );
    this.totalHits = totalHits;
  }

  public void addDocument( Document document ) {
    checkArgument( document != null, "Document must not be null" );
    SearchResultItem item = new LuceneSearchResultItem( document, query );
    items.add( item );
  }
}
