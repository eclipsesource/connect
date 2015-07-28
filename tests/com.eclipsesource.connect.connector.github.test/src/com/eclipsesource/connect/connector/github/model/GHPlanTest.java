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


public class GHPlanTest {

  private GHPlan plan;

  @Before
  public void setUp() {
    plan = new Gson().fromJson( new InputStreamReader( GHUserTest.class.getResourceAsStream( "/test-plan.json" ) ),
                                GHPlan.class );
  }

  @Test
  public void testHasName() {
    String name = plan.getName();

    assertThat( name ).isEqualTo( "Medium" );
  }

  @Test
  public void testHasSpace() {
    int space = plan.getSpace();

    assertThat( space ).isEqualTo( 400 );
  }

  @Test
  public void testHasPrivateRepos() {
    int privateRepos = plan.getPrivateRepos();

    assertThat( privateRepos ).isEqualTo( 20 );
  }

  @Test
  public void testHasCollaborators() {
    int collaborators = plan.getCollaborators();

    assertThat( collaborators ).isEqualTo( 0 );
  }
}
