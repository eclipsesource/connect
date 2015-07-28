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
package com.eclipsesource.connect.mvc.internal;

import static com.google.common.base.Charsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.google.common.io.CharSource;
import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;


public class Compressors {

  public static boolean isCompressable( String path ) {
    return !path.contains( ".min." ) && ( path.endsWith( ".js" ) || path.endsWith( ".css" ) );
  }

  public static String compress( String path, InputStream stream ) {
    if( path.endsWith( ".js" ) && !path.contains( ".min." ) ) {
      return compressJs( stream );
    } else if( path.endsWith( ".css" ) && !path.contains( ".min." ) ) {
      return compressCss( stream );
    }
    return uncompressed( stream );
  }

  private static String compressJs( InputStream stream ) {
    try( InputStreamReader reader = new InputStreamReader( stream, UTF_8 ) ) {
      JavaScriptCompressor compressor = new JavaScriptCompressor( reader, new CompressionErrorHandler() );
      try( StringWriter writer = new StringWriter() ) {
        compressor.compress( writer, -1, true, false, false, false );
        return writer.toString();
      }
    } catch( EvaluatorException | IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static String compressCss( InputStream stream ) {
    try( InputStreamReader reader = new InputStreamReader( stream, UTF_8 ) ) {
      CssCompressor cssCompressor = new CssCompressor( reader );
      StringWriter writer = new StringWriter();
      cssCompressor.compress( writer, -1 );
      writer.close();
      return writer.toString();
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static String uncompressed( InputStream stream ) {
    try {
      return new CharSource() {

        @Override
        public Reader openStream() throws IOException {
          return new InputStreamReader( stream, UTF_8 );
        }
      }.read();
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static class CompressionErrorHandler implements ErrorReporter {

      @Override
      public void error( String arg0, String arg1, int arg2, String arg3, int arg4 ) {
        throw new IllegalStateException( "Error during JavaScript compression" );
      }

      @Override
      public EvaluatorException runtimeError( String arg0, String arg1, int arg2, String arg3, int arg4 ) {
        throw new IllegalStateException( "Error during JavaScript compression" );
      }

      @Override
      public void warning( String arg0, String arg1, int arg2, String arg3, int arg4 ) {
        throw new IllegalStateException( "Error during JavaScript compression" );
      }

  }

  private Compressors() {
    // prevent instantiations
  }
}
