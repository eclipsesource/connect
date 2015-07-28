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

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkArgument;

import java.io.InputStreamReader;
import java.io.Reader;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;
import com.github.mustachejava.DefaultMustacheFactory;


public class MailMustacheFactory extends DefaultMustacheFactory {

  private final SendMailImpl sendMail;

  public MailMustacheFactory( SendMailImpl sendMail ) {
    checkArgument( sendMail != null, "SendMail must not be null" );
    this.sendMail = sendMail;
  }

  @Override
  public Reader getReader( String resourceName ) {
    InputStreamReader result = find( sendMail.getAssetsFinder(), resourceName );
    if( result != null ) {
      return result;
    }
    return super.getReader( resourceName );
  }

  private InputStreamReader find( AssetsFinder finder, String resourceName ) {
    AssetsResult result = finder.find( resourceName );
    if( result != null ) {
      return new InputStreamReader( result.getStream(), UTF_8 );
    }
    return null;
  }
}
