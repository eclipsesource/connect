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


public class GHOrganization {

  private String login;
  private String id;
  private String url;
  private String avatar_url;
  private String name;
  private String company;
  private String blog;
  private String location;
  private String email;
  private int public_repos;
  private int public_gists;
  private int followers;
  private int following;
  private String html_url;
  private String created_at;
  private String type;
  private int total_private_repos;
  private int owned_private_repos;
  private int private_gists;
  private int disk_usage;
  private int collaborators;
  private String billing_email;
  private GHPlan plan;

  public String getLogin() {
    return login;
  }

  public void setLogin( String login ) {
    this.login = login;
  }

  public String getId() {
    return id;
  }

  public void setId( String id ) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public String getAvatarUrl() {
    return avatar_url;
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

  public String getEmail() {
    return email;
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

  public String getHtmlUrl() {
    return html_url;
  }

  public String getCreatedAt() {
    return created_at;
  }

  public String getType() {
    return type;
  }

  public int getTotalPrivateRepos() {
    return total_private_repos;
  }

  public int getOwnedPrivateRepos() {
    return owned_private_repos;
  }

  public int getPrivateGists() {
    return private_gists;
  }

  public int getDiskUsage() {
    return disk_usage;
  }

  public int getCollaborators() {
    return collaborators;
  }

  public String getBillingEmail() {
    return billing_email;
  }

  public GHPlan getPlan() {
    return plan;
  }

}
