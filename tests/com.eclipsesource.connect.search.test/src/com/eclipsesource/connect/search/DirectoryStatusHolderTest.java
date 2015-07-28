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

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.search.Search.DirectoryStatus;


public class DirectoryStatusHolderTest {

  private DirectoryStatusHolder statusHolder;

  @Before
  public void setUp() {
    statusHolder = new DirectoryStatusHolder();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetStatusFailsWithNullDirectory() {
    statusHolder.getStatus( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetStatusFailsWithEmptyDirectory() {
    statusHolder.getStatus( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetStatusFailsWithEmptyWhitespaceDirectory() {
    statusHolder.getStatus( " " );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testChangeStatusFailsWithNullDirectory() {
    statusHolder.changeStatus( null, DirectoryStatus.ERROR );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testChangeStatusFailsWithEmptyDirectory() {
    statusHolder.changeStatus( "", DirectoryStatus.ERROR );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testChangeStatusFailsWithEmptyWhitespaceDirectory() {
    statusHolder.changeStatus( " ", DirectoryStatus.ERROR );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testChangeStatusFailsWithNullStatus() {
    statusHolder.changeStatus( "foo", null );
  }

  @Test
  public void testHasNoAvailableAsDefaultStatus() {
    DirectoryStatus status = statusHolder.getStatus( "foo" );

    assertThat( status ).isSameAs( DirectoryStatus.NOT_AVAILABLE );
  }

  @Test
  public void testCanChangeStatus() {
    statusHolder.changeStatus( "foo", DirectoryStatus.INDEXING );

    DirectoryStatus status = statusHolder.getStatus( "foo" );

    assertThat( status ).isSameAs( DirectoryStatus.INDEXING );
  }
}
