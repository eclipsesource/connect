/***********************************************************import java.util.List;
 Copyright (c) 2015 EclipseSource and others.
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


public class GHNotificationCommit {

  private String id;
  private boolean distinct;
  private String message;
  private String timestamp;
  private String url;
  private GHNotificationUser author;
  private GHNotificationUser committer;
  private List<String> added;
  private List<String> removed;
  private List<String> modified;

  public String getId() {
    return id;
  }

  public boolean isDistinct() {
    return distinct;
  }

  public String getMessage() {
    return message;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getUrl() {
    return url;
  }

  public GHNotificationUser getAuthor() {
    return author;
  }

  public GHNotificationUser getCommitter() {
    return committer;
  }

  public List<String> getAdded() {
    return added;
  }

  public List<String> getRemoved() {
    return removed;
  }

  public List<String> getModified() {
    return modified;
  }

}
