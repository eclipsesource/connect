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
package com.eclipsesource.connect.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class IdTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullId() {
    new Id( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetEmptyId() {
    new Id( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetEmptyWhiteSpaceId() {
    new Id( " " );
  }

  @Test
  public void testCreatesADefaultId() {
    Id id = new Id();
    Id id2 = new Id();

    assertThat( id.toString() ).isNotNull().isNotSameAs( id2.toString() );
  }

  @Test
  public void testUsesIdForString() {
    Id id = new Id( "foo" );

    assertThat( id.toString() ).isEqualTo( "foo" );
  }

  @Test
  public void testImplementsEqual() {
    Id id = new Id( "foo" );
    Id id2 = new Id( "foo" );

    assertThat( id ).isEqualTo( id2 );
  }
}
