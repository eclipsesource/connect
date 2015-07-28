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


public class GHPullRequest {

  private String url;
  private String html_url;
  private String diff_url;
  private String patch_url;
  private String issue_url;
  private String commits_url;
  private String review_comments_url;
  private String review_comment_url;
  private String comments_url;
  private String statuses_url;
  private String number;
  private String state;
  private String title;
  private String body;
  private String created_at;
  private String updated_at;
  private String closed_at;
  private String merged_at;
  private GHUser user;
  private String merge_commit_sha;
  private boolean merged;
  private boolean mergeable;
  private GHUser merged_by;
  private int comments;
  private int commits;
  private int additions;
  private int deletions;
  private int changed_files;

  public String getUrl() {
    return url;
  }

  public String getHtmlUrl() {
    return html_url;
  }

  public String getDiffUrl() {
    return diff_url;
  }

  public String getPatchUrl() {
    return patch_url;
  }

  public String getIssueUrl() {
    return issue_url;
  }

  public String getCommitsUrl() {
    return commits_url;
  }

  public String getReviewCommentsUrl() {
    return review_comments_url;
  }

  public String getReviewCommentUrl() {
    return review_comment_url;
  }

  public String getCommentsUrl() {
    return comments_url;
  }

  public String getStatusesUrl() {
    return statuses_url;
  }

  public String getNumber() {
    return number;
  }

  public String getState() {
    return state;
  }

  public String getTitle() {
    return title;
  }

  public String getBody() {
    return body;
  }

  public String getCreatedAt() {
    return created_at;
  }

  public String getUpdatedAt() {
    return updated_at;
  }

  public String getClosedAt() {
    return closed_at;
  }

  public String getMergedAt() {
    return merged_at;
  }

  public GHUser getUser() {
    return user;
  }

  public String getMergeCommitSha() {
    return merge_commit_sha;
  }

  public boolean isMerged() {
    return merged;
  }

  public boolean isMergeable() {
    return mergeable;
  }

  public GHUser getMergedBy() {
    return merged_by;
  }

  public int getComments() {
    return comments;
  }

  public int getCommits() {
    return commits;
  }

  public int getAdditions() {
    return additions;
  }

  public int getDeletions() {
    return deletions;
  }

  public int getChangedFiles() {
    return changed_files;
  }

}
