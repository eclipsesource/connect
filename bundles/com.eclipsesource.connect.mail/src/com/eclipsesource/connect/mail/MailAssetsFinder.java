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
package com.eclipsesource.connect.mail;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;


public class MailAssetsFinder implements AssetsFinder {

  private final MailConfiguration configuration;

  public MailAssetsFinder( MailConfiguration configuration) {
    checkArgument( configuration != null, "MailConfiguration must not be null" );
    this.configuration = configuration;
  }

  @Override
  public AssetsResult find( String name ) {
    checkState( configuration != null, "Configuration not set" );
    File file = new File( getPath( name ) );
    if( file.exists() ) {
      return new AssetsResult( getFileStream( file ), MailConfiguration.class.getClassLoader() );
    }
    return null;
  }

  private String getPath( String name ) {
    String templateBasePath = configuration.getTemplateBasePath();
    if( templateBasePath.endsWith( File.separator ) && name.startsWith( File.separator ) ) {
      return templateBasePath + name.substring( 1, name.length() );
    } else if( !templateBasePath.endsWith( File.separator ) && !name.startsWith( File.separator ) ) {
      return templateBasePath + File.separator + name;
    }
    return templateBasePath + name;
  }

  private InputStream getFileStream( File file ) {
    try {
      return Files.newInputStream( Paths.get( file.toURI() ) );
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

}
