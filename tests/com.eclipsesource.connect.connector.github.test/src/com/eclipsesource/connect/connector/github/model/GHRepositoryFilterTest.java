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
package com.eclipsesource.connect.connector.github.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.Map;

import org.junit.Test;

import com.eclipsesource.connect.connector.github.model.GHRepositoryFilter.Direction;
import com.eclipsesource.connect.connector.github.model.GHRepositoryFilter.Sort;
import com.eclipsesource.connect.connector.github.model.GHRepositoryFilter.Type;


public class GHRepositoryFilterTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullDirection() {
    new GHRepositoryFilter().setDirection( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullSort() {
    new GHRepositoryFilter().setSort( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullType() {
    new GHRepositoryFilter().setType( null );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testFailsIsImmutable() {
    new GHRepositoryFilter().getFilter().put( "foo", "bar" );
  }

  @Test
  public void testSetsType() {
    GHRepositoryFilter filter = new GHRepositoryFilter().setType( Type.ALL );

    Map<String, String> rawFilter = filter.getFilter();

    assertThat( rawFilter ).contains( entry( "type", "all" ) );
  }

  @Test
  public void testSetsSort() {
    GHRepositoryFilter filter = new GHRepositoryFilter().setSort( Sort.UPDATED );

    Map<String, String> rawFilter = filter.getFilter();

    assertThat( rawFilter ).contains( entry( "sort", "updated" ) );
  }

  @Test
  public void testSetsDirection() {
    GHRepositoryFilter filter = new GHRepositoryFilter().setDirection( Direction.ASC );

    Map<String, String> rawFilter = filter.getFilter();

    assertThat( rawFilter ).contains( entry( "direction", "asc" ) );
  }
}
