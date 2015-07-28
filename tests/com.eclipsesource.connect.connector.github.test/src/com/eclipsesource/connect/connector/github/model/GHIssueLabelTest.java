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


public class GHIssueLabelTest {

  private GHIssueLabel label;

  @Before
  public void setUp() {
    label = new Gson().fromJson( new InputStreamReader( GHIssueLabelTest.class.getResourceAsStream( "/test-label.json" ) ),
                                 GHIssueLabel.class );
  }

  @Test
  public void testHasUrl() {
    String value = label.getUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/labels/bug" );
  }

  @Test
  public void testHasName() {
    String value = label.getName();

    assertThat( value ).isEqualTo( "bug" );
  }

  @Test
  public void testHasColor() {
    String value = label.getColor();

    assertThat( value ).isEqualTo( "f29513" );
  }
}
