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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.connect.connector.github.model.GHIssue.State;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;


public class GHIssueFilter {

  public static enum Sort {

    CREATED( "created" ),
    UPDATED( "updated" ),
    COMMENTS( "comments" );

    private final String value;

    private Sort( String value ) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public static enum Direction {

    ASC( "asc" ),
    DESC( "desc" );

    private final String value;

    private Direction( String value ) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }

  }

  private final Map<String, String> filter;

  public GHIssueFilter() {
    filter = new HashMap<>();
  }

  public GHIssueFilter setMilestone( String milestone ) {
    verifyString( milestone, "Milestone" );
    filter.put( "milestone", milestone );
    return this;
  }

  public GHIssueFilter setState( State state ) {
    checkArgument( state != null, "State must not be null" );
    filter.put( "state", state.toString() );
    return this;
  }

  public GHIssueFilter setAssignee( String assignee ) {
    verifyString( assignee, "Assignee" );
    filter.put( "assignee", assignee );
    return this;
  }

  public GHIssueFilter setCreator( String creator ) {
    verifyString( creator, "Creator" );
    filter.put( "creator", creator );
    return this;
  }

  public GHIssueFilter setMentioned( String mentioned ) {
    verifyString( mentioned, "Mentioned" );
    filter.put( "mentioned", mentioned );
    return this;
  }

  public GHIssueFilter setLabels( String... labels ) {
    filter.put( "labels", Joiner.on( "," ).join( Arrays.asList( labels ) ) );
    return this;
  }

  public GHIssueFilter setSort( Sort sort ) {
    checkArgument( sort != null, "Sort must not be null" );
    filter.put( "sort", sort.toString() );
    return this;
  }

  public GHIssueFilter setDirection( Direction direction ) {
    checkArgument( direction != null, "Direction must not be null" );
    filter.put( "direction", direction.toString() );
    return this;
  }

  public GHIssueFilter setSince( String since ) {
    verifyString( since, "Since" );
    filter.put( "since", since );
    return this;
  }

  private void verifyString( String value, String name  ) {
    checkArgument( value != null, name + " must not be null" );
    checkArgument( !value.trim().isEmpty(), name + " must not be empty" );
  }

  public Map<String, String> getFilter() {
    return ImmutableMap.copyOf( filter );
  }
}
