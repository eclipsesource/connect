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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.io.ByteStreams;

public class ZipUtil {

  public static void unpackArchive( String url, File targetFolder ) {
    checkArgument( url != null, "Url must not be null" );
    checkArgument( targetFolder != null, "Target Folder must not be null" );
    try {
      ensureTargetFolder( targetFolder );
      // make sure we get the actual file
      InputStream in = new BufferedInputStream( new URL( url ).openStream(), 1024 );
      File zip = File.createTempFile( "app", ".zip", targetFolder );
      OutputStream out = new BufferedOutputStream( new FileOutputStream( zip ) );
      copyInputStream( in, out );
      unpackArchive( zip, targetFolder );
    } catch( IOException exception ) {
      throw new IllegalStateException( exception );
    }
  }

  private static void ensureTargetFolder( File targetFolder ) {
    if( !targetFolder.exists() ) {
      if( !targetFolder.mkdirs() ) {
        throw new IllegalStateException( "Could not create folders " + targetFolder.getAbsolutePath() );
      }
    }
  }

  private static void unpackArchive( File archive, File targetFolder ) throws IOException {
    validateArguments( archive, targetFolder );
    try( ZipFile zipFile = new ZipFile( archive ) ) {
      for( Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
        ZipEntry entry = entries.nextElement();
        writeFile( entry, zipFile, new File( targetFolder, File.separator + entry.getName() ) );
      }
      if( !archive.delete() ) {
        throw new IllegalStateException( "Could not delete archive" );
      }
    }
  }

  private static void validateArguments( File theFile, File targetDir ) throws IOException {
    if( !theFile.exists() ) {
      throw new IOException( theFile.getAbsolutePath() + " does not exist" );
    }
    if( !buildDirectory( targetDir ) ) {
      throw new IOException( "Could not create directory: " + targetDir );
    }
  }

  private static void writeFile( ZipEntry entry, ZipFile zipFile, File file ) throws IOException {
    if( !buildDirectory( file.getParentFile() ) ) {
      throw new IOException( "Could not create directory: " + file.getParentFile() );
    }
    if( !entry.isDirectory() ) {
      copyInputStream( zipFile.getInputStream( entry ), new BufferedOutputStream( new FileOutputStream( file ) ) );
    } else {
      if( !buildDirectory( file ) ) {
        throw new IOException( "Could not create directory: " + file );
      }
    }
  }

  private static void copyInputStream( InputStream in, OutputStream out ) throws IOException {
    try {
      ByteStreams.copy( in, out );
    } finally {
      in.close();
      out.close();
    }
  }

  private static boolean buildDirectory( File file ) {
    return file.exists() || file.mkdirs();
  }

  private ZipUtil() {
    // prevent instantiation
  }
}