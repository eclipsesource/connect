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
package com.eclipsesource.connect.api.html;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;


public class Pagination {

  private final List<Integer> nextPages;
  private final List<Integer> previousPages;

  public Pagination( List<Integer> previousPages , List<Integer> nextPages  ) {
    checkArgument( nextPages != null, "Next Pages must not be null" );
    checkArgument( previousPages != null, "Previous Pages must not be null" );
    this.nextPages = new ArrayList<>( nextPages );
    this.previousPages = new ArrayList<>( previousPages );
  }

  public boolean getHasPages() {
    return getNextPage() != -1 || getPreviousPage() != -1;
  }

  public int getCurrentPage() {
    int next = getNextPage();
    int prev = getPreviousPage();
    if( next != -1 ) {
      return next - 1;
    } else if( prev != -1 ) {
      return prev + 1;
    }
    return 1;
  }

  public boolean getHasPrevious() {
    return getPreviousPage() != -1;
  }

  public int getPreviousPage() {
    List<Integer> previousPages = getPreviousPages();
    if( !previousPages.isEmpty() ) {
      return previousPages.get( previousPages.size() - 1 );
    }
    return -1;
  }

  public List<Integer> getPreviousPages() {
    return ImmutableList.copyOf( previousPages );
  }

  public boolean getHasNext() {
    return getNextPage() != -1;
  }

  public int getNextPage() {
    List<Integer> nextPages = getNextPages();
    if( !nextPages.isEmpty() ) {
      return nextPages.get( 0 );
    }
    return -1;
  }

  public List<Integer> getNextPages() {
    return ImmutableList.copyOf( nextPages );
  }
}
