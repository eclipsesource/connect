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

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;

import com.eclipsesource.connect.api.mail.Mail;
import com.eclipsesource.connect.api.mail.Mail.Attachement;
import com.google.common.io.ByteSource;


public class MessageBuilder {

  private final BodyProcessor bodyProcessor;
  private final Session session;
  private final String sender;

  public MessageBuilder( Session session, String sender, BodyProcessor bodyProcessor ) {
    validateArguments( session, sender, bodyProcessor );
    this.session = session;
    this.sender = sender;
    this.bodyProcessor = bodyProcessor;
  }

  public Message build( Mail mail ) {
    checkArgument( mail != null, "Mail must not be null" );
    MimeMessage message = new MimeMessage( session );
    try {
      message.setFrom( new InternetAddress( sender ) );
      addRecipients( message, mail );
      message.setSubject( firstNonNull( mail.getSubject(), "" ) );
      handleBody( mail, message );
      return message;
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private void validateArguments( Session session, String sender, BodyProcessor bodyProcessor ) {
    checkArgument( session != null, "Session must not be null" );
    checkArgument( sender != null, "Sender must not be null" );
    checkArgument( !sender.trim().isEmpty(), "Sender must not be empty" );
    checkArgument( bodyProcessor != null, "BodyProcessor must not be null" );
  }

  private void addRecipients( MimeMessage message, Mail mail ) {
    addTos( message, mail );
    addCCs( message, mail );
    addBCCs( message, mail );
  }

  private void addTos( MimeMessage message, Mail mail ) {
    mail.getTos().forEach( to -> {
      try {
        message.addRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
      } catch( Exception shouldNotHappen ) {
        throw new IllegalStateException( shouldNotHappen );
      }
    } );
  }

  private void addCCs( MimeMessage message, Mail mail ) {
    mail.getCCs().forEach( cc -> {
      try {
        message.addRecipient( Message.RecipientType.CC, new InternetAddress( cc ) );
      } catch( Exception shouldNotHappen ) {
        throw new IllegalStateException( shouldNotHappen );
      }
    } );
  }

  private void addBCCs( MimeMessage message, Mail mail ) {
    mail.getBCCs().forEach( bcc -> {
      try {
        message.addRecipient( Message.RecipientType.BCC, new InternetAddress( bcc ) );
      } catch( Exception shouldNotHappen ) {
        throw new IllegalStateException( shouldNotHappen );
      }
    } );
  }

  private void handleBody( Mail mail, MimeMessage message ) throws Exception {
    String body = bodyProcessor.getBody( mail );
    List<Attachement> attachements = mail.getAttachements();
    if( attachements.isEmpty() ) {
      addBody( message, mail, body );
    } else {
      addBodyWithAttachements( message, mail, body, attachements );
    }
  }

  private void addBodyWithAttachements( MimeMessage message, Mail mail, String body , List<Attachement> attachements )
    throws Exception
  {
    Multipart multipart = new MimeMultipart();
    addMultiPartBody( mail, body, multipart );
    addAttachments( multipart, attachements );
    message.setContent( multipart );
  }

  private void addMultiPartBody( Mail mail, String body, Multipart multipart ) throws Exception {
    MimeBodyPart bodyPart = new MimeBodyPart();
    addBody( bodyPart, mail, body );
    multipart.addBodyPart( bodyPart );
  }

  private void addBody( MimePart part, Mail mail, String body ) throws MessagingException {
    if( mail.isHtml() ) {
      part.setContent( firstNonNull( body, "" ), "text/html; charset=utf-8" );
    } else {
      part.setText( firstNonNull( body, "" ) );
    }
  }

  private void addAttachments( Multipart multipart, List<Attachement> attachements ) throws Exception {
    for( Attachement attachement : attachements ) {
      MimeBodyPart attachementPart = new MimeBodyPart();
      attachementPart.setFileName( attachement.getName() );
      attachementPart.setContent( createAttachementContent( attachement ), attachement.getType() );
      multipart.addBodyPart( attachementPart );
    }
  }

  private byte[] createAttachementContent( Attachement attachement ) throws IOException {
    return new ByteSource() {

      @Override
      public InputStream openStream() throws IOException {
        return attachement.getContent();
      }
    }.read();
  }
}
