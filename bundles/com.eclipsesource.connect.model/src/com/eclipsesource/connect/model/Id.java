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

import com.eclipsesource.connect.model.internal.ObjectId;


public final class Id {

  private final String id;

  public Id() {
    this( new ObjectId().toString() );
  }

  public Id( String id ) {
    checkArgument( id != null, "Id must not be null" );
    checkArgument( !id.trim().isEmpty(), "Id must not be empty" );
    this.id = id;
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
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
    Id other = ( Id )obj;
    if( id == null ) {
      if( other.id != null ) {
        return false;
      }
    } else if( !id.equals( other.id ) ) {
      return false;
    }
    return true;
  }

}
