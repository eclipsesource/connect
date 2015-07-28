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

import java.util.Date;

import org.junit.Test;


public class DateFormatingTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDate() {
    new DateFormating( null );
  }

  @Test
  public void testFormatsIso8601() {
    DateFormating formating = new DateFormating( new Date( 23L ) );

    String formated = formating.toIso8601();

    assertThat( formated ).contains( "1970-01-01T0" ).contains( ":00:00Z" );
  }

  @Test
  public void testSimple() {
    DateFormating formating = new DateFormating( new Date( 23L ) );

    String formated = formating.toSimple();

    assertThat( formated ).isEqualTo( "1970-01-01" );
  }
}
