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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Hashtable;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;

import com.eclipsesource.connect.api.mail.Mail;
import com.google.common.util.concurrent.MoreExecutors;


public class SendMailImplTest {

  private SendMailImpl sendMail;

  @Before
  public void setUp() {
    sendMail = new SendMailImpl();
  }

  @Test
  public void testInitializesMailCommandMap() {
    new SendMailImpl();

    CommandMap commandMap = CommandMap.getDefaultCommandMap();

    assertThat( commandMap ).isInstanceOf( MailcapCommandMap.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullConfiguration() {
    sendMail.setConfiguration( null );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToSendMailWithoutConfiguration() {
    sendMail.unsetConfiguration( null );

    sendMail.send( new Mail( "foo" ) );
  }

  @Test
  public void testSendsMail() throws ConfigurationException {
    UncaughtExceptionHandler handler = mock( UncaughtExceptionHandler.class );
    Thread.currentThread().setUncaughtExceptionHandler( handler );
    SendMailImpl sendMail = new SendMailImpl( MoreExecutors.newDirectExecutorService() );
    sendMail.setConfiguration( getConfiguration() );

    sendMail.send( new Mail( "foo" ) );

    verify( handler ).uncaughtException( any( Thread.class ), any( IllegalStateException.class ) );
  }

  private MailConfiguration getConfiguration() throws ConfigurationException {
    MailConfiguration configuration = new MailConfiguration();
    configuration.updated( createValidProperties() );
    return configuration;
  }

  private Hashtable<String, String> createValidProperties() {
    Hashtable<String, String> properties = new Hashtable<>();
    properties.put( "mail.smtp.host", "not.localhost" );
    properties.put( MailConfiguration.AUTH_PASSWORD, "password" );
    properties.put( MailConfiguration.AUTH_USER, "user" );
    properties.put( MailConfiguration.FILEINSTALL_FILENAME, "file:/foo/bar/test.cfg" );
    properties.put( MailConfiguration.SENDER, "test@test.com" );
    properties.put( MailConfiguration.TEMPLATE_FOLDER, "../templates" );
    return properties;
  }
}
