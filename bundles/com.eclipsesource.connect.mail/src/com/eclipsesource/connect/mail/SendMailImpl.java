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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.mail.Mail;
import com.eclipsesource.connect.api.mail.SendMail;
import com.google.common.util.concurrent.ThreadFactoryBuilder;


public class SendMailImpl implements SendMail {

  private final BodyProcessor bodyProcessor;
  private final ExecutorService executor;
  private MailConfiguration configuration;
  private Session session;
  private AssetsFinder assetsFinder;

  public SendMailImpl() {
    this( Executors.newFixedThreadPool( 2, new ThreadFactoryBuilder().setNameFormat( "send-mail-pool-%d" ).build() ) );
  }

  SendMailImpl( ExecutorService executor ) {
    this.bodyProcessor = new BodyProcessor( this );
    this.executor = executor;
  }

  @Override
  public void send( Mail mail ) {
    checkState( configuration != null, "MailConfiguration not set" );
    executor.submit( new Runnable() {

      @Override
      public void run() {
        try {
          MessageBuilder messageBuilder = new MessageBuilder( getSession(), configuration.getSender(), bodyProcessor );
          sendMessage( messageBuilder.build( mail ) );
        } catch( Exception shouldNotHappen ) {
          Thread thread = Thread.currentThread();
          thread.getUncaughtExceptionHandler().uncaughtException( thread, new IllegalStateException( shouldNotHappen ) );
        }
      }
    } );
  }

  private void sendMessage( Message message ) throws MessagingException {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader( Message.class.getClassLoader() );
      Transport.send( message );
    } finally {
      Thread.currentThread().setContextClassLoader( original );
    }
  }

  private Session getSession() {
    if( session == null ) {
      if( configuration.getUser() != null ) {
        session = Session.getDefaultInstance( configuration.getMailProperties(), createAuthenticator( configuration ) );
      } else {
        session = Session.getInstance( configuration.getMailProperties() );
      }
    }
    return session;
  }

  private Authenticator createAuthenticator( MailConfiguration configuration ) {
    return new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication( configuration.getUser(), configuration.getPassword() );
      }
    };
  }

  void setConfiguration( MailConfiguration configuration ) {
    checkArgument( configuration != null, "MailConfiguration must not be null" );
    this.configuration = configuration;
    this.assetsFinder = new MailAssetsFinder( configuration );
  }

  void unsetConfiguration( MailConfiguration configuration ) {
    session = null;
  }

  AssetsFinder getAssetsFinder() {
    return assetsFinder;
  }

  static {
    MailcapCommandMap mcap = new MailcapCommandMap();
    mcap.addMailcap( "text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain" );
    mcap.addMailcap( "text/html;; x-java-content-handler=com.sun.mail.handlers.text_html" );
    mcap.addMailcap( "text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml" );
    mcap.addMailcap( "multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed; x-java-fallback-entry=true" );
    mcap.addMailcap( "message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822" );
    CommandMap.setDefaultCommandMap( mcap );
  }
}
