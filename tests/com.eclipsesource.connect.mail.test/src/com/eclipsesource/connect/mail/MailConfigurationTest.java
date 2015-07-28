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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Hashtable;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;


public class MailConfigurationTest {

  private MailConfiguration configuration;
  private Hashtable<String, String> properties;

  @Before
  public void setUp() {
    properties = createValidProperties();
    configuration = new MailConfiguration();
  }

  private Hashtable<String, String> createValidProperties() {
    Hashtable<String, String> properties = new Hashtable<>();
    properties.put( MailConfiguration.AUTH_PASSWORD, "password" );
    properties.put( MailConfiguration.AUTH_USER, "user" );
    properties.put( MailConfiguration.FILEINSTALL_FILENAME, "file:/foo/bar/test.cfg" );
    properties.put( MailConfiguration.SENDER, "test@test.com" );
    properties.put( MailConfiguration.TEMPLATE_FOLDER, "../templates" );
    return properties;
  }

  @Test( expected = ConfigurationException.class )
  public void testFailsWithoutFileinstallFilename() throws ConfigurationException {
    properties.remove( MailConfiguration.FILEINSTALL_FILENAME );

    configuration.updated( properties );
  }

  @Test( expected = ConfigurationException.class )
  public void testFailsWithoutSender() throws ConfigurationException {
    properties.remove( MailConfiguration.SENDER );

    configuration.updated( properties );
  }

  @Test( expected = ConfigurationException.class )
  public void testFailsWithoutTemplateFolder() throws ConfigurationException {
    properties.remove( MailConfiguration.TEMPLATE_FOLDER );

    configuration.updated( properties );
  }

  @Test
  public void testHasUser() throws ConfigurationException {
    configuration.updated( properties );

    String user = configuration.getUser();

    assertThat( user ).isEqualTo( "user" );
  }

  @Test
  public void testHasNullUserForNoUser() throws ConfigurationException {
    properties.remove( MailConfiguration.AUTH_USER );
    configuration.updated( properties );

    String user = configuration.getUser();

    assertThat( user ).isNull();
  }

  @Test
  public void testHasPassword() throws ConfigurationException {
    configuration.updated( properties );

    String password = configuration.getPassword();

    assertThat( password ).isEqualTo( "password" );
  }

  @Test
  public void testHasNullPasswordForNoPassword() throws ConfigurationException {
    properties.remove( MailConfiguration.AUTH_PASSWORD );
    configuration.updated( properties );

    String password = configuration.getPassword();

    assertThat( password ).isNull();
  }

  @Test
  public void testHasSender() throws ConfigurationException {
    configuration.updated( properties );

    String sender = configuration.getSender();

    assertThat( sender ).isEqualTo( "test@test.com" );
  }

  @Test
  public void testHasTemplatePath() throws ConfigurationException {
    configuration.updated( properties );

    String templateBasePath = configuration.getTemplateBasePath();

    assertThat( templateBasePath ).isEqualTo( "/foo/bar/../templates" );
  }

  @Test
  public void testStoresAllPropertiesInMailProperties() throws ConfigurationException {
    configuration.updated( properties );

    Properties mailProperties = configuration.getMailProperties();

    assertThat( mailProperties.get( MailConfiguration.AUTH_PASSWORD ) ).isEqualTo( "password" );
    assertThat( mailProperties.get( MailConfiguration.AUTH_USER ) ).isEqualTo( "user" );
  }
}
