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

import com.google.gson.annotations.SerializedName;


public class GHRepository {

  private String id;
  private GHUser owner;
  private String name;
  private String full_name;
  private String description;
  @SerializedName( "private" )
  private boolean isPrivate;
  private boolean fork;
  private String url;
  private String html_url;
  private String clone_url;
  private String git_url;
  private String ssh_url;
  private String svn_url;
  private String mirror_url;
  private String homepage;
  private String language;
  private int forks_count;
  private int stargazers_count;
  private int watchers_count;
  private int size;
  private String default_branch;
  private int open_issues_count;
  private boolean has_issues;
  private boolean has_wiki;
  private boolean has_downloads;
  private String pushed_at;
  private String created_at;
  private String updated_at;
  private GHPermission permissions;
  private int subscribers_count;
  private GHOrganization organization;
  private GHRepository parent;
  private GHRepository source;

  public String getId() {
    return id;
  }

  public void setId( String id ) {
    this.id = id;
  }

  public GHUser getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public String getFullName() {
    return full_name;
  }

  public String getDescription() {
    return description;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public boolean isFork() {
    return fork;
  }

  public String getUrl() {
    return url;
  }

  public String getHtmlUrl() {
    return html_url;
  }

  public String getCloneUrl() {
    return clone_url;
  }

  public String getGitUrl() {
    return git_url;
  }

  public String getSshUrl() {
    return ssh_url;
  }

  public String getSvnUrl() {
    return svn_url;
  }

  public String getMirrorUrl() {
    return mirror_url;
  }

  public String getHomepage() {
    return homepage;
  }

  public String getLanguage() {
    return language;
  }

  public int getForksCount() {
    return forks_count;
  }

  public int getStargazersCount() {
    return stargazers_count;
  }

  public int getWatchersCount() {
    return watchers_count;
  }

  public int getSize() {
    return size;
  }

  public String getDefaultBranch() {
    return default_branch;
  }

  public int getOpenIssuesCount() {
    return open_issues_count;
  }

  public boolean hasIssues() {
    return has_issues;
  }

  public boolean hasWiki() {
    return has_wiki;
  }

  public boolean hasDownloads() {
    return has_downloads;
  }

  public String getPushedAt() {
    return pushed_at;
  }

  public String getCreatedAt() {
    return created_at;
  }

  public String getUpdatedAt() {
    return updated_at;
  }

  public GHPermission getPermissions() {
    return permissions;
  }

  public int getSubscribersCount() {
    return subscribers_count;
  }

  public GHOrganization getOrganization() {
    return organization;
  }

  public GHRepository getParent() {
    return parent;
  }

  public GHRepository getSource() {
    return source;
  }

}
