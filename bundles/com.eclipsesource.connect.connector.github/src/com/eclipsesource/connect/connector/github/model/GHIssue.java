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

import java.util.List;
import java.util.stream.Stream;


public class GHIssue {

  public static enum State {

    OPEN( "open" ),
    CLOSED( "closed" ),
    ALL( "all" );

    private final String value;

    private State( String value ) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }

    public static State fromString( String value ) {
      return Stream.of( State.values() ).filter( state -> state.toString().equals( value ) ).findFirst().orElse( null );
    }

  }

  private String url;
  private String html_url;
  private String number;
  private String state;
  private String title;
  private String body;
  private GHUser user;
  private List<GHIssueLabel> labels;
  private GHUser assignee;
  private GHIssueMilestone milestone;
  private int comments;
  private GHPullRequest pull_request;
  private String closed_at;
  private String created_at;
  private String updated_at;

  public String getUrl() {
    return url;
  }

  public String getHtmlUrl() {
    return html_url;
  }

  public String getNumber() {
    return number;
  }

  public State getState() {
    return State.fromString( state );
  }

  public String getTitle() {
    return title;
  }

  public String getBody() {
    return body;
  }

  public GHUser getUser() {
    return user;
  }

  public List<GHIssueLabel> getLabels() {
    return labels;
  }

  public GHUser getAssignee() {
    return assignee;
  }

  public GHIssueMilestone getMilestone() {
    return milestone;
  }

  public int getComments() {
    return comments;
  }

  public GHPullRequest getPullRequest() {
    return pull_request;
  }

  public String getClosedAt() {
    return closed_at;
  }

  public String getCreatedAt() {
    return created_at;
  }

  public String getUpdatedAt() {
    return updated_at;
  }

}
