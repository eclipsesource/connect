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
package com.eclipsesource.connect.api.persistence;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;


public class Query<T> {

  public static enum SortDirection {
    ASC, DESC
  }

  public static final int UNDEFINED = -1;

  private final Map<String, Object> conditions;
  private final String place;
  private final Class<T> type;
  private int limit;
  private int skip;
  private String sortField;
  private SortDirection sortDirection;

  public Query( String place, Class<T> type ) {
    checkArgument( place != null, "Place must not be null" );
    checkArgument( !place.trim().isEmpty(), "Place must not be empty" );
    this.conditions = new HashMap<>();
    this.place = place;
    this.type = type;
    this.limit = UNDEFINED;
    this.skip = UNDEFINED;
    this.sortDirection = SortDirection.ASC;
  }

  @SuppressWarnings("unchecked")
  public Query( String place ) {
    this( place, ( Class<T> )Object.class );
  }

  public Class<T> getType() {
    return type;
  }

  public String getPlace() {
    return place;
  }

  public Query<T> where( String field, Object is ) {
    checkArgument( field != null, "Field must not be null" );
    checkArgument( !field.trim().isEmpty(), "Field must not be empty" );
    checkArgument( is != null, "Is must not be null" );
    conditions.put( field, is );
    return this;
  }

  public Map<String, Object> getConditions() {
    return ImmutableMap.copyOf( conditions );
  }

  public Query<T> limit( int limit ) {
    checkArgument( limit > 0, "Limit must be > 0 but was " + limit );
    this.limit = limit;
    return this;
  }

  public int getLimit() {
    return limit;
  }

  public Query<T> skip( int skip ) {
    checkArgument( skip > 0, "Skip must be > 0 but was " + skip );
    this.skip = skip;
    return this;
  }

  public int getSkip() {
    return skip;
  }

  public Query<T> sortBy( String field, SortDirection direction ) {
    checkArgument( field != null, "Sort Field must not be null" );
    checkArgument( !field.trim().isEmpty(), "Sort Field must not be empty" );
    checkArgument( direction != null, "Sort Direction must not be null" );
    this.sortField = field;
    this.sortDirection = direction;
    return this;
  }

  public String getSortField() {
    return sortField;
  }

  public SortDirection getSortDirection() {
    return sortDirection;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( conditions == null ) ? 0 : conditions.hashCode() );
    result = prime * result + limit;
    result = prime * result + ( ( place == null ) ? 0 : place.hashCode() );
    result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    Query<?> other = ( Query<?> )obj;
    if( conditions == null ) {
      if( other.conditions != null ) {
        return false;
      }
    } else if( !conditions.equals( other.conditions ) ) {
      return false;
    }
    if( limit != other.limit ) {
      return false;
    }
    if( place == null ) {
      if( other.place != null ) {
        return false;
      }
    } else if( !place.equals( other.place ) ) {
      return false;
    }
    if( type == null ) {
      if( other.type != null ) {
        return false;
      }
    } else if( !type.equals( other.type ) ) {
      return false;
    }
    return true;
  }

}
