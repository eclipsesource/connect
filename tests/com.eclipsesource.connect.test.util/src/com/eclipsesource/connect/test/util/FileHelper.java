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
package com.eclipsesource.connect.test.util;

import static com.google.common.base.Charsets.UTF_8;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.common.io.CharSource;


public class FileHelper {

  public static String readFile( ReaderSupplier reader ) {
    try {
      return new CharSource() {

        @Override
        public Reader openStream() throws IOException {
          return reader.getReader();
        }
      }.read();
    } catch( IOException e ) {
      throw new IllegalStateException( "Could not read file from reader lambda" );
    }
  }

  public static String readFile( String name, Class<?> classPathProvider ) {
    try {
      return new CharSource() {

        @Override
        public Reader openStream() throws IOException {
          return new InputStreamReader( classPathProvider.getResourceAsStream( name ), UTF_8 );
        }
      }.read();
    } catch( IOException e ) {
      throw new IllegalStateException( "Could not read file " + name + " on classpath of " + classPathProvider.getName() );
    }
  }

  private FileHelper() {
    // prevent instantiation
  }
}
