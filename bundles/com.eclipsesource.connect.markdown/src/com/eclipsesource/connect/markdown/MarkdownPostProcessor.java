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
package com.eclipsesource.connect.markdown;


import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eclipsesource.connect.api.html.Sanitizer;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;


public class MarkdownPostProcessor {

  private static final Pattern HEADING = Pattern.compile( "<h[1,2,3]>(.*)</h[1,2,3]>" );

  private final int headingDepth;

  public MarkdownPostProcessor( int headingDepth ) {
    checkArgument( headingDepth > 0, "Heading depth mus tbe > 0 but was " + headingDepth );
    this.headingDepth = headingDepth;
  }

  public String process( String content ) {
    Sanitizer sanitizer = new Sanitizer();
    StringBuilder builder = new StringBuilder();
    readLines( content ).forEach( line -> builder.append( processLine( sanitizer, line ) +"\n" ) );
    return builder.toString();
  }

  private String processLine( Sanitizer sanitizer , String line ) {
    Matcher matcher = HEADING.matcher( line );
    if( matcher.matches() ) {
      String heading = matcher.group( 1 );
      for( int i = 1; i <= headingDepth; i++ ) {
        if( line.contains( "<h" + i + ">" ) ) {
          return line.replace( "<h" + i + ">", "<h" + i + " id=\"" + sanitizer.sanitize( heading ) + "\">" );
        }
      }
    }
    return line;
  }

  private ImmutableList<String> readLines( String content ) {
    try {
      return new CharSource() {

        @Override
        public Reader openStream() throws IOException {
          return new StringReader( content );
        }
      }.readLines();
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }
}
