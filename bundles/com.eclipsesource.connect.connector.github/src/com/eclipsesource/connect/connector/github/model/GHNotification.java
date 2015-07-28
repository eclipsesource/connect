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


public class GHNotification {

  private String ref;
  private String after;
  private String before;
  private boolean created;
  private boolean deleted;
  private boolean forced;
  private String compare;
  private GHNotificationRepository repository;
  private GHUser pusher;
  private List<GHNotificationCommit> commits;
  private GHNotificationCommit head_commit;

  public String getRef() {
    return ref;
  }

  public String getAfter() {
    return after;
  }

  public String getBefore() {
    return before;
  }

  public boolean isCreated() {
    return created;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public boolean isForced() {
    return forced;
  }

  public String getCompare() {
    return compare;
  }

  public GHNotificationRepository getRepository() {
    return repository;
  }

  public GHUser getPusher() {
    return pusher;
  }

  public List<GHNotificationCommit> getCommits() {
    return commits;
  }

  public GHNotificationCommit getHeadCommit() {
    return head_commit;
  }

}
