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
package com.eclipsesource.connect.api.util;

import static com.google.common.base.Preconditions.checkArgument;


public class JsonResponse {

  public static Error error( String reason ) {
    return new Error( reason );
  }

  public static class Error {

    private final String error;

    Error( String reason ) {
      checkArgument( reason != null, "Reason must not be null" );
      this.error = reason;
    }

    public String getReason() {
      return error;
    }

  }

  private JsonResponse() {
    // prevent instantiation
  }

}

