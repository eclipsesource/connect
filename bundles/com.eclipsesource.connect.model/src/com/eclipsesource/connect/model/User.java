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
package com.eclipsesource.connect.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.eclipsesource.connect.model.internal.JsonUtil;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;


public class User {

  private final Id id;
  private final String name;
  private final String email;
  private final List<String> roles;
  private final Map<String, JsonElement> data;
  private UserInfo info;
  private final Date createdAt;
  private Date updatedAt;
  private Date validTo;

  public User( String name, String email ) {
    validateArguments( name, email );
    this.name = name;
    this.email = email;
    this.roles = new ArrayList<>();
    this.data = new HashMap<>();
    this.id = new Id();
    this.info = new UserInfo();
    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  private void validateArguments( String name, String email ) {
    checkArgument( name != null, "Name must not be null" );
    checkArgument( !name.trim().isEmpty(), "Name must not be empty" );
    checkArgument( email != null, "Email must not be null" );
    checkArgument( !email.trim().isEmpty(), "Email must not be empty" );
  }

  public Id getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public void setData( String serviceName, Object data ) {
    checkArgument( serviceName != null, "ServiceName must not be null" );
    checkArgument( !serviceName.trim().isEmpty(), "ServiceName must not be empty" );
    checkArgument( data != null, "Data must not be null" );
    this.data.put( serviceName, JsonUtil.toJson( data ) );
  }

  public <T> T getData( String serviceName, Class<T> type ) {
    checkArgument( serviceName != null, "ServiceName must not be null" );
    checkArgument( !serviceName.trim().isEmpty(), "ServiceName must not be empty" );
    checkArgument( type != null, "Type must not be null" );
    return JsonUtil.fromJson( data.get( serviceName ), type );
  }

  public void addRoles( String... roles ) {
    this.updatedAt = new Date();
    Stream.of( roles ).filter( role -> !this.roles.contains( role ) ).forEach( role -> this.roles.add( role ) );
  }

  public void removeRoles( String... roles ) {
    this.updatedAt = new Date();
    Stream.of( roles ).forEach( role -> this.roles.remove( role ) );
  }

  public List<String> getRoles() {
    return ImmutableList.copyOf( roles );
  }

  public UserInfo getInfo() {
    return info;
  }

  public void setInfo( UserInfo userInfo ) {
    checkArgument( userInfo != null, "UserInfo must not be null" );
    this.updatedAt = new Date();
    this.info = userInfo;
  }

  public Date getCreatedAt() {
    return new Date( createdAt.getTime() );
  }

  public Date getUpdatedAt() {
    return new Date( updatedAt.getTime() );
  }

  public void setValidTo( Date validTo ) {
    this.updatedAt = new Date();
    if( validTo != null ) {
      this.validTo = new Date( validTo.getTime() );
    } else {
      this.validTo = null;
    }
  }

  public Optional<Date> getValidTo() {
    return Optional.ofNullable( validTo );
  }

}
