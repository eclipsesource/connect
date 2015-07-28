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
package com.eclipsesource.connect.connector.stackexchange;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class SiteTest {

  @Test
  public void testHasStackoverflowName() {
    assertThat( Site.STACKOVERFLOW.getName() ).isEqualTo( "stackoverflow" );
  }

  @Test
  public void testHasServerFaultName() {
    assertThat( Site.SERVERFAULT.getName() ).isEqualTo( "serverfault" );
  }

  @Test
  public void testHasStackAppsName() {
    assertThat( Site.STACKAPPS.getName() ).isEqualTo( "stackapps" );
  }
}
