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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;


public class PaginationTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullNextPages() {
    new Pagination( new ArrayList<>(), null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullPreviousPages() {
    new Pagination( null, new ArrayList<>() );
  }

  @Test
  public void testHasNoPagesWithEmptyPages() {
    Pagination pagination = new Pagination( new ArrayList<>(), new ArrayList<>() );

    boolean hasPages = pagination.getHasPages();

    assertThat( hasPages ).isFalse();
  }

  @Test
  public void testHasPagesWithNextPages() {
    Pagination pagination = new Pagination( new ArrayList<>(), Lists.newArrayList( 2, 3 ) );

    boolean hasPages = pagination.getHasPages();

    assertThat( hasPages ).isTrue();
  }

  @Test
  public void testHasPagesWithPreviousPages() {
    Pagination pagination = new Pagination( Lists.newArrayList( 2, 3 ), new ArrayList<>() );

    boolean hasPages = pagination.getHasPages();

    assertThat( hasPages ).isTrue();
  }

  @Test
  public void testHasDefaultCurrentPage() {
    Pagination pagination = new Pagination( Lists.newArrayList(), Lists.newArrayList() );

    int currentPage = pagination.getCurrentPage();

    assertThat( currentPage ).isEqualTo( 1 );
  }

  @Test
  public void testHasCurrentPage() {
    Pagination pagination = new Pagination( Lists.newArrayList( 2, 3 ), Lists.newArrayList( 5, 6 ) );

    int currentPage = pagination.getCurrentPage();

    assertThat( currentPage ).isEqualTo( 4 );
  }

  @Test
  public void testHasCurrentPageWhenPreviousIsEmpty() {
    Pagination pagination = new Pagination( Lists.newArrayList(), Lists.newArrayList( 2, 3 ) );

    int currentPage = pagination.getCurrentPage();

    assertThat( currentPage ).isEqualTo( 1 );
  }

  @Test
  public void testHasCurrentPageWhenNextIsEmpty() {
    Pagination pagination = new Pagination( Lists.newArrayList( 2, 3 ), Lists.newArrayList() );

    int currentPage = pagination.getCurrentPage();

    assertThat( currentPage ).isEqualTo( 4 );
  }

  @Test
  public void testHasPreviousPage() {
    Pagination pagination = new Pagination( Lists.newArrayList( 2, 3 ), Lists.newArrayList() );

    boolean hasPrevious = pagination.getHasPrevious();

    assertThat( hasPrevious ).isTrue();
  }

  @Test
  public void testHasNoPreviousPage() {
    Pagination pagination = new Pagination( Lists.newArrayList(), Lists.newArrayList() );

    boolean hasPrevious = pagination.getHasPrevious();

    assertThat( hasPrevious ).isFalse();
  }

  @Test
  public void testHasNextPage() {
    Pagination pagination = new Pagination( Lists.newArrayList(), Lists.newArrayList( 2 ) );

    boolean hasNext = pagination.getHasNext();

    assertThat( hasNext ).isTrue();
  }

  @Test
  public void testHasNoNextPage() {
    Pagination pagination = new Pagination( Lists.newArrayList(), Lists.newArrayList() );

    boolean hasNext = pagination.getHasNext();

    assertThat( hasNext ).isFalse();
  }

  @Test
  public void testCanGetPreviousPage() {
    Pagination pagination = new Pagination( Lists.newArrayList( 2, 3 ), Lists.newArrayList() );

    int previousPage = pagination.getPreviousPage();

    assertThat( previousPage ).isEqualTo( 3 );
  }

  @Test
  public void testCanGetNextPage() {
    Pagination pagination = new Pagination( Lists.newArrayList( 2, 3 ), Lists.newArrayList( 5, 6 ) );

    int nextPage = pagination.getNextPage();

    assertThat( nextPage ).isEqualTo( 5 );
  }

  @Test
  public void testCanGetNextPages() {
    Pagination pagination = new Pagination( Lists.newArrayList( 2, 3 ), Lists.newArrayList( 5, 6 ) );

    List<Integer> nextPages = pagination.getNextPages();

    assertThat( nextPages ).hasSize( 2 ).contains( 5, 6 );
  }

  @Test
  public void testCanGetPreviousPages() {
    Pagination pagination = new Pagination( Lists.newArrayList( 2, 3 ), Lists.newArrayList( 5, 6 ) );

    List<Integer> previousPages = pagination.getPreviousPages();

    assertThat( previousPages ).hasSize( 2 ).contains( 2, 3 );
  }
}
