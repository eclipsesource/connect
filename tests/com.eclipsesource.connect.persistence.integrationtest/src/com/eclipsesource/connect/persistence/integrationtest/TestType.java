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
package com.eclipsesource.connect.persistence.integrationtest;


public class TestType {

  private String name;

  public TestType( String name ) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
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
    TestType other = ( TestType )obj;
    if( name == null ) {
      if( other.name != null ) {
        return false;
      }
    } else if( !name.equals( other.name ) ) {
      return false;
    }
    return true;
  }

}
