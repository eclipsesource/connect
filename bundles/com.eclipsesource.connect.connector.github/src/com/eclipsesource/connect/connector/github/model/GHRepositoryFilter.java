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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;


public class GHRepositoryFilter {

  public static enum Type {

    ALL( "all" ),
    OWNER( "owner" ),
    PUBLIC( "public" ),
    PRIVATE( "private" ),
    MEMBER( "member" );

    private final String type;

    private Type( String type ) {
      this.type = type;
    }

    @Override
    public String toString() {
      return type;
    }
  }

  public static enum Sort {

    CREATED( "created" ),
    UPDATED( "updated" ),
    PUSHED( "pushed" ),
    FULLNAME( "full_name" );

    private final String sort;

    private Sort( String sort ) {
      this.sort = sort;
    }

    @Override
    public String toString() {
      return sort;
    }
  }

  public static enum Direction {

    ASC( "asc" ),
    DESC( "desc" );

    private final String direction;

    private Direction( String direction ) {
      this.direction = direction;
    }

    @Override
    public String toString() {
      return direction;
    }
  }

  private final Map<String, String> filter;

  public GHRepositoryFilter() {
    filter = new HashMap<>();
  }

  public GHRepositoryFilter setType( Type type ) {
    checkArgument( type != null, "Type must not be null" );
    filter.put( "type", type.toString() );
    return this;
  }

  public GHRepositoryFilter setSort( Sort sort ) {
    checkArgument( sort != null, "Sort must not be null" );
    filter.put( "sort", sort.toString() );
    return this;
  }

  public GHRepositoryFilter setDirection( Direction direction ) {
    checkArgument( direction != null, "Direction must not be null" );
    filter.put( "direction", direction.toString() );
    return this;
  }

  public Map<String, String> getFilter() {
    return ImmutableMap.copyOf( filter );
  }

}
