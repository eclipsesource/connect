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
import static org.assertj.core.api.Assertions.entry;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.connector.github.model.GHIssue.State;
import com.eclipsesource.connect.connector.github.model.GHIssueFilter.Direction;
import com.eclipsesource.connect.connector.github.model.GHIssueFilter.Sort;


public class GHIssueFilterTest {

  private GHIssueFilter issueFilter;

  @Before
  public void setUp() {
    issueFilter = new GHIssueFilter();
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testFilterIsImmutable() {
    Map<String, String> filter = issueFilter.getFilter();

    filter.put( "foo", "bar" );
  }

  @Test
  public void testSetsMilestone() {
    issueFilter.setMilestone( "m1" );

    Map<String, String> filter = issueFilter.getFilter();

    assertThat( filter ).contains( entry( "milestone", "m1" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullMilestone() {
    issueFilter.setMilestone( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyMilestone() {
    issueFilter.setMilestone( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceMilestone() {
    issueFilter.setMilestone( " " );
  }

  @Test
  public void testSetsState() {
    issueFilter.setState( State.CLOSED );

    Map<String, String> filter = issueFilter.getFilter();

    assertThat( filter ).contains( entry( "state", "closed" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullState() {
    issueFilter.setState( null );
  }

  @Test
  public void testSetsAssignee() {
    issueFilter.setAssignee( "foo" );

    Map<String, String> filter = issueFilter.getFilter();

    assertThat( filter ).contains( entry( "assignee", "foo" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullAssignee() {
    issueFilter.setAssignee( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyAssignee() {
    issueFilter.setAssignee( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceAssignee() {
    issueFilter.setAssignee( " " );
  }

  @Test
  public void testSetsCreator() {
    issueFilter.setCreator( "foo" );

    Map<String, String> filter = issueFilter.getFilter();

    assertThat( filter ).contains( entry( "creator", "foo" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullCreator() {
    issueFilter.setCreator( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyCreator() {
    issueFilter.setCreator( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceCreator() {
    issueFilter.setCreator( " " );
  }

  @Test
  public void testSetsMentioned() {
    issueFilter.setMentioned( "bar" );

    Map<String, String> filter = issueFilter.getFilter();

    assertThat( filter ).contains( entry( "mentioned", "bar" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullMentioned() {
    issueFilter.setMentioned( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyMentioned() {
    issueFilter.setMentioned( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceMentioned() {
    issueFilter.setMentioned( " " );
  }

  @Test
  public void testSetsLabels() {
    issueFilter.setLabels( "foo", "bar" );

    Map<String, String> filter = issueFilter.getFilter();

    assertThat( filter ).contains( entry( "labels", "foo,bar" ) );
  }

  @Test
  public void testSetsSort() {
    issueFilter.setSort( Sort.COMMENTS );

    Map<String, String> filter = issueFilter.getFilter();

    assertThat( filter ).contains( entry( "sort", "comments" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSort() {
    issueFilter.setSort( null );
  }

  @Test
  public void testSetsDirection() {
    issueFilter.setDirection( Direction.ASC );

    Map<String, String> filter = issueFilter.getFilter();

    assertThat( filter ).contains( entry( "direction", "asc" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDirection() {
    issueFilter.setDirection( null );
  }

  @Test
  public void testSetsSince() {
    issueFilter.setSince( "foo" );

    Map<String, String> filter = issueFilter.getFilter();

    assertThat( filter ).contains( entry( "since", "foo" ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSince() {
    issueFilter.setSince( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptySince() {
    issueFilter.setSince( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceSince() {
    issueFilter.setSince( " " );
  }
}
