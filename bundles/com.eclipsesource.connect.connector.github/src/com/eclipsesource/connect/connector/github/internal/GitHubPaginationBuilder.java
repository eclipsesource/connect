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
package com.eclipsesource.connect.connector.github.internal;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.Link;

import com.eclipsesource.connect.api.html.Pagination;
import com.google.common.base.Splitter;


public class GitHubPaginationBuilder {

  private final List<Link> links;

  public GitHubPaginationBuilder( List<Link> links ) {
    checkArgument( links != null, "Links must not be null" );
    this.links = links;
  }

  public Pagination build() {
    return new Pagination( getPreviousPages(), getNextPages() );
  }

  private List<Integer> getNextPages() {
    List<Integer> nexts = new ArrayList<>();
    int next = getLink( "next" );
    int last = getLink( "last" );
    if( next != -1 ) {
      for( int i = next; i <= last; i++ ) {
        nexts.add( i );
      }
    }
    return nexts;
  }

  private List<Integer> getPreviousPages() {
    List<Integer> previous = new ArrayList<>();
    int currentPage = getCurrentPage();
    if( currentPage != -1 ) {
      for( int i = 1; i < currentPage; i++ ) {
        previous.add( i );
      }
    }
    return previous;
  }

  private int getCurrentPage() {
    int next = getLink( "next" );
    int prev = getLink( "prev" );
    if( next != -1 ) {
      return next - 1;
    } else if( prev != -1 ) {
      return prev + 1;
    }
    return 0;
  }

  private int getLink( String rel ) {
    Optional<Link> result = links.stream().filter( link -> link.getRel().equals( rel ) ).findFirst();
    if( result.isPresent() ) {
      return parsePage( result.get() );
    }
    return -1;
  }

  private int parsePage( Link link ) {
    Map<String, String> parameter = Splitter.on( '&' )
                                            .trimResults()
                                            .withKeyValueSeparator( '=' )
                                            .split( link.getUri().getQuery() );
    return Integer.parseInt( parameter.get( "page" ) );
  }

}
