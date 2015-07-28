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


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Sanitizer {

  private static final Pattern LETTER = Pattern.compile( "\\w" );

  private final List<String> dictionary;

  public Sanitizer() {
    dictionary = new ArrayList<>();
  }

  public String sanitize( String toSanitize ) {
    String key = createKey( toSanitize );
    int i = 1;
    while( dictionary.contains( key ) ) {
      key = createKey( toSanitize + " " + i );
      i++;
    }
    dictionary.add( key );
    return key;
  }

  private String createKey( String toSanitize ) {
    StringBuilder builder = new StringBuilder();
    char[] charArray = toSanitize.toCharArray();
    for( char c : charArray ) {
      processChar( builder, c );
    }
    return builder.toString().toLowerCase();
  }

  private void processChar( StringBuilder builder, char c ) {
    processSpace( builder, c );
    processLetter( builder, c );
  }

  private void processSpace( StringBuilder builder, char c ) {
    if( c == ' ' ) {
      builder.append( "-" );
    }
  }

  private void processLetter( StringBuilder builder, char c ) {
    Matcher matcher = LETTER.matcher( String.valueOf( c ) );
    if( !matcher.matches() ) {
      builder.append( "" );
    } else {
      builder.append( c );
    }
  }

}
