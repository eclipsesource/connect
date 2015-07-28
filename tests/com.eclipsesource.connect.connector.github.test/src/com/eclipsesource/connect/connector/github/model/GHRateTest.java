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

import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;


public class GHRateTest {

  private GHRate rate;

  @Before
  public void setUp() {
    rate = new Gson().fromJson( new InputStreamReader( GHUserTest.class.getResourceAsStream( "/test-rate.json" ) ),
                                GHRate.class );
  }

  @Test
  public void testHasCoreLimit() {
    int coreLimit = rate.getCoreLimit();

    assertThat( coreLimit ).isEqualTo( 5000 );
  }

  @Test
  public void testHasCoreRemaining() {
    int value = rate.getCoreRemaining();

    assertThat( value ).isEqualTo( 4999 );
  }

  @Test
  public void testHasCoreReset() {
    int value = rate.getCoreReset();

    assertThat( value ).isEqualTo( 1372700873 );
  }

  @Test
  public void testHasSearchLimit() {
    int value = rate.getSearchLimit();

    assertThat( value ).isEqualTo( 20 );
  }

  @Test
  public void testHasSearchRemaining() {
    int value = rate.getSearchRemaining();

    assertThat( value ).isEqualTo( 18 );
  }

  @Test
  public void testHasSearchReset() {
    int value = rate.getSearchReset();

    assertThat( value ).isEqualTo( 1372697452 );
  }
}
