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
package com.eclipsesource.connect.persistence.integrationtest;

import org.junit.Test;


public class SelfDefenseTest extends PersistenceTest {

  public SelfDefenseTest( StorageProvider storageProvider ) {
    super( storageProvider );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullPlace() {
    getStorage().store( null, new Object() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyPlace() {
    getStorage().store( "", new Object() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhiteSpacePlace() {
    getStorage().store( " ", new Object() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNulObject() {
    getStorage().store( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToDeleteObjectsFromCollectionWithNullQuery() {
    getStorage().delete( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToFindSingleWithNullQuery() {
    getStorage().find( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToFindAllWithNullQuery() {
    getStorage().findAll( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToCountWithNullQuery() {
    getStorage().count( null );
  }
}
