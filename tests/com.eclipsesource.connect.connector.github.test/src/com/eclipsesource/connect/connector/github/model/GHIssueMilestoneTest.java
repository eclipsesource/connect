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

import com.eclipsesource.connect.connector.github.model.GHIssue.State;
import com.google.gson.Gson;


public class GHIssueMilestoneTest {

  private GHIssueMilestone milestone;

  @Before
  public void setUp() {
    milestone = new Gson().fromJson( new InputStreamReader( GHIssueMilestoneTest.class.getResourceAsStream( "/test-milestone.json" ) ),
                                     GHIssueMilestone.class );
  }

  @Test
  public void testHasUrl() {
    String value = milestone.getUrl();

    assertThat( value ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/milestones/1" );
  }

  @Test
  public void testHasNumber() {
    String value = milestone.getNumber();

    assertThat( value ).isEqualTo( "1" );
  }

  @Test
  public void testHasState() {
    State value = milestone.getState();

    assertThat( value ).isSameAs( State.OPEN );
  }

  @Test
  public void testHasTitle() {
    String value = milestone.getTitle();

    assertThat( value ).isEqualTo( "v1.0" );
  }

  @Test
  public void testHasDescription() {
    String value = milestone.getDescription();

    assertThat( value ).isEqualTo( "" );
  }

  @Test
  public void testHasCreator() {
    GHUser value = milestone.getCreator();

    assertThat( value.getLogin() ).isEqualTo( "octocat" );
  }

  @Test
  public void testHasOpenIssues() {
    int value = milestone.getOpenIssues();

    assertThat( value ).isEqualTo( 4 );
  }

  @Test
  public void testHasClosedIssues() {
    int value = milestone.getClosedIssues();

    assertThat( value ).isEqualTo( 8 );
  }

  @Test
  public void testHasCreatedAt() {
    String value = milestone.getCreatedAt();

    assertThat( value ).isEqualTo( "2011-04-10T20:09:31Z" );
  }

  @Test
  public void testHasUpdatedAt() {
    String value = milestone.getUpdatedAt();

    assertThat( value ).isEqualTo( "2014-03-03T18:58:10Z" );
  }

  @Test
  public void testHasDueOn() {
    String value = milestone.getDueOn();

    assertThat( value ).isNull();
  }
}
