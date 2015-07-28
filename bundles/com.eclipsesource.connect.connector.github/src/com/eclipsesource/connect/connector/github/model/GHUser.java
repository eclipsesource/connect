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


public class GHUser {

  private String login;
  private int id;
  private String avatar_url;
  private String gravatar_id;
  private String url;
  private String html_url;
  private String followers_url;
  private String following_url;
  private String gists_url;
  private String starred_url;
  private String subscriptions_url;
  private String organizations_url;
  private String repos_url;
  private String events_url;
  private String received_events_url;
  private String type;
  private boolean site_admin;
  private String name;
  private String company;
  private String blog;
  private String location;
  private boolean hireable;
  private String bio;
  private int public_repos;
  private int public_gists;
  private int followers;
  private int following;
  private String created_at;
  private String updated_at;
  private GHPlan plan;

  public String getLogin() {
    return login;
  }

  public int getId() {
    return id;
  }

  public String getAvatarUrl() {
    return avatar_url;
  }

  public String getGravatarId() {
    return gravatar_id;
  }

  public String getUrl() {
    return url;
  }

  public String getHtmlUrl() {
    return html_url;
  }

  public String getFollowersUrl() {
    return followers_url;
  }

  public String getFollowingUrl() {
    return following_url;
  }

  public String getGistsUrl() {
    return gists_url;
  }

  public String getStarredUrl() {
    return starred_url;
  }

  public String getSubscriptionsUrl() {
    return subscriptions_url;
  }

  public String getOrganizationsUrl() {
    return organizations_url;
  }

  public String getReposUrl() {
    return repos_url;
  }

  public String getEventsUrl() {
    return events_url;
  }

  public String getReceivedEventsUrl() {
    return received_events_url;
  }

  public String getType() {
    return type;
  }

  public boolean isSiteAdmin() {
    return site_admin;
  }

  public String getName() {
    return name;
  }

  public String getCompany() {
    return company;
  }

  public String getBlog() {
    return blog;
  }

  public String getLocation() {
    return location;
  }

  public boolean isHireable() {
    return hireable;
  }

  public String getBio() {
    return bio;
  }

  public int getPublicRepos() {
    return public_repos;
  }

  public int getPublicGists() {
    return public_gists;
  }

  public int getFollowers() {
    return followers;
  }

  public int getFollowing() {
    return following;
  }

  public String getCreatedAt() {
    return created_at;
  }

  public String getUpdatedAt() {
    return updated_at;
  }

  public GHPlan getPlan() {
    return plan;
  }

}
