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
package com.eclipsesource.connect.search;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.eclipsesource.connect.api.search.Search.DirectoryStatus;


public class DirectoryStatusHolder {

  private final Map<String, DirectoryStatus> status;

  public DirectoryStatusHolder() {
    this.status = new ConcurrentHashMap<>();
  }

  public DirectoryStatus getStatus( String directory ) {
    checkArgument( directory != null, "Directory must not be null" );
    checkArgument( !directory.trim().isEmpty(), "Directory must not be empty" );
    DirectoryStatus directoryStatus = status.get( directory );
    if( directoryStatus != null ) {
      return directoryStatus;
    }
    return DirectoryStatus.NOT_AVAILABLE;
  }

  public void changeStatus( String directory, DirectoryStatus status ) {
    checkArgument( directory != null, "Directory must not be null" );
    checkArgument( !directory.trim().isEmpty(), "Directory must not be empty" );
    checkArgument( status != null, "DirectoryStatus must not be null" );
    this.status.put( directory, status );
  }
}
