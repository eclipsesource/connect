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


public class GHCommit {

  private String url;
  private String sha;
  private String html_url;
  private String comments_url;
  private GHGitCommit commit;
  private GHUser author;
  private GHUser committer;
  private List<GHCommit> parents;
  private GHCommitStats stats;
  private List<GHCommitFile> files;

  public String getUrl() {
    return url;
  }

  public String getSha() {
    return sha;
  }

  public String getHtmlUrl() {
    return html_url;
  }

  public String getCommentsUrl() {
    return comments_url;
  }

  public GHGitCommit getCommit() {
    return commit;
  }

  public GHUser getAuthor() {
    return author;
  }

  public GHUser getCommitter() {
    return committer;
  }

  public List<GHCommit> getParents() {
    return parents;
  }

  public GHCommitStats getStats() {
    return stats;
  }

  public List<GHCommitFile> getFiles() {
    return files;
  }

}
