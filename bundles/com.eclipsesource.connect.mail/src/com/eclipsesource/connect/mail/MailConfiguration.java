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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;


public class MailConfiguration implements ManagedService {

  static final String TEMPLATE_FOLDER = "template.folder";
  static final String FILEINSTALL_FILENAME = "felix.fileinstall.filename";
  static final String SENDER = "sender";
  static final String AUTH_USER = "auth.user";
  static final String AUTH_PASSWORD = "auth.password";

  private String user;
  private String password;
  private Properties mailProperties;
  private String sender;
  private String templateBasePath;

  @Override
  public void updated( Dictionary<String, ?> properties ) throws ConfigurationException {
    validateProperties( properties );
    createMailProperties( properties );
    user = ( String )properties.get( AUTH_USER );
    password = ( String )properties.get( AUTH_PASSWORD );
    sender = properties.get( SENDER ).toString();
    createTemplatePath( properties );
  }

  private void validateProperties( Dictionary<String, ?> properties ) throws ConfigurationException {
    if( properties.get( FILEINSTALL_FILENAME ) == null ) {
      throw new ConfigurationException( FILEINSTALL_FILENAME, "Fileinstall Filename must be set" );
    }
    if( properties.get( SENDER ) == null ) {
      throw new ConfigurationException( SENDER, "Sender must be set" );
    }
    if( properties.get( TEMPLATE_FOLDER ) == null ) {
      throw new ConfigurationException( TEMPLATE_FOLDER, "Template Folder must be set" );
    }
  }

  private void createMailProperties( Dictionary<String, ?> properties ) {
    mailProperties = new Properties();
    Enumeration<String> keys = properties.keys();
    while( keys.hasMoreElements() ) {
      String key = keys.nextElement();
      Object value = properties.get( key );
      mailProperties.put( key, value );
    }
  }

  private void createTemplatePath( Dictionary<String, ?> properties ) {
    String cfgFile = properties.get( FILEINSTALL_FILENAME ).toString();
    String ftemplateFolder = properties.get( TEMPLATE_FOLDER ).toString();
    String cfgFolder = cfgFile.substring( 0, cfgFile.lastIndexOf( File.separator ) );
    try {
      cfgFolder = new URI( cfgFolder ).getPath();
      File templateFolder = new File( cfgFolder + File.separator + ftemplateFolder );
      templateBasePath = templateFolder.getAbsolutePath();
    } catch( URISyntaxException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public Properties getMailProperties() {
    return mailProperties;
  }

  public String getSender() {
    return sender;
  }

  public String getTemplateBasePath() {
    return templateBasePath;
  }

}
