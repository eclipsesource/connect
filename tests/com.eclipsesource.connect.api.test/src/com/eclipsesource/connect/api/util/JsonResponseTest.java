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
package com.eclipsesource.connect.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class JsonResponseTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullReason() {
    new JsonResponse.Error( null );
  }

  @Test
  public void testHasReason() {
    JsonResponse.Error error = JsonResponse.error( "foo" );

    String reason = error.getReason();

    assertThat( reason ).isEqualTo( "foo" );
  }

  @Test
  public void testCreatesErrorWithFactoryMethod() {
    JsonResponse.Error error = JsonResponse.error( "foo" );

    assertThat( error.getReason() ).isEqualTo( "foo" );
  }
}
