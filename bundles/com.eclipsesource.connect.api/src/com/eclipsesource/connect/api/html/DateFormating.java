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
package com.eclipsesource.connect.api.html;

import static com.google.common.base.Preconditions.checkArgument;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateFormating {

  private final Date date;

  public DateFormating( Date date ) {
    checkArgument( date != null, "Date must not be null" );
    this.date = new Date( date.getTime() );
  }

  public String toIso8601() {
    SimpleDateFormat isoFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
    isoFormat.setTimeZone( TimeZone.getTimeZone("UTC") );
    return isoFormat.format( date );
  }

  public String toSimple() {
    SimpleDateFormat simpleFormat = new SimpleDateFormat( "yyyy-MM-dd" );
    simpleFormat.setTimeZone( TimeZone.getTimeZone("UTC") );
    return simpleFormat.format( date );
  }
}
