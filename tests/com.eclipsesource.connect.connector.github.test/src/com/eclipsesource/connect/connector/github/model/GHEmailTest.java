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

import org.junit.Test;


public class GHEmailTest {

  @Test
  public void testHasEmail() {
    GHEmail gitHubEmail = new GHEmail( "foo", true, true );

    String email = gitHubEmail.getEmail();

    assertThat( email ).isEqualTo( "foo" );
  }

  @Test
  public void testHasVerfified() {
    GHEmail gitHubEmail = new GHEmail( "foo", true, true );

    boolean verified = gitHubEmail.isVerified();

    assertThat( verified ).isTrue();
  }

  @Test
  public void testHasPrimary() {
    GHEmail gitHubEmail = new GHEmail( "foo", true, true );

    boolean primary = gitHubEmail.isPrimary();

    assertThat( primary ).isTrue();
  }
}
