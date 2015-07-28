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


public class GHHook {

  private String url;
  private String updated_at;
  private String created_at;
  private final String name;
  private final List<String> events;
  private final GHHookConfiguration config;
  private String id;
  private boolean active;

  public GHHook( String name, GHHookConfiguration config, List<String> events, boolean active ) {
    this.name = name;
    this.config = config;
    this.events = events;
    this.active = active;
  }

  public String getUrl() {
    return url;
  }

  public String getUpdatedAt() {
    return updated_at;
  }

  public String getCreatedAt() {
    return created_at;
  }

  public String getName() {
    return name;
  }

  public List<String> getEvents() {
    return events;
  }

  public void setActive( boolean active ) {
    this.active = active;
  }

  public boolean isActive() {
    return active;
  }

  public GHHookConfiguration getConfig() {
    return config;
  }

  public String getId() {
    return id;
  }

}
