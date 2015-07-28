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

import com.eclipsesource.connect.connector.github.model.GHIssue.State;


public class GHIssueMilestone {

  private String url;
  private String number;
  private String state;
  private String title;
  private String description;
  private GHUser creator;
  private int open_issues;
  private int closed_issues;
  private String created_at;
  private String updated_at;
  private String due_on;

  public String getUrl() {
    return url;
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

  public String getDescription() {
    return description;
  }

  public GHUser getCreator() {
    return creator;
  }

  public int getOpenIssues() {
    return open_issues;
  }

  public int getClosedIssues() {
    return closed_issues;
  }

  public String getCreatedAt() {
    return created_at;
  }

  public String getUpdatedAt() {
    return updated_at;
  }

  public String getDueOn() {
    return due_on;
  }

}
