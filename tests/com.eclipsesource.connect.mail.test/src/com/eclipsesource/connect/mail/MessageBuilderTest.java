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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.connect.api.mail.Mail;


public class MessageBuilderTest {

  private Mail mail;
  private MessageBuilder messageBuilder;

  @Before
  public void setUp() {
    mail = new Mail( "foo@bar.com" );
    BodyProcessor bodyProcessor = mock( BodyProcessor.class );
    when( bodyProcessor.getBody( mail ) ).thenReturn( "body" );
    messageBuilder = new MessageBuilder( Session.getDefaultInstance( new Properties() ), "send@test.com", bodyProcessor );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSession() {
    new MessageBuilder( null, "foo", mock( BodyProcessor.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBodyProcessor() {
    new MessageBuilder( Session.getDefaultInstance( new Properties() ), "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSender() {
    new MessageBuilder( Session.getDefaultInstance( new Properties() ), null, mock( BodyProcessor.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptySender() {
    new MessageBuilder( Session.getDefaultInstance( new Properties() ), "", mock( BodyProcessor.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceSender() {
    new MessageBuilder( Session.getDefaultInstance( new Properties() ), " ", mock( BodyProcessor.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullMail() {
    messageBuilder.build( null );
  }

  @Test
  public void testAddsReceipient() throws MessagingException {
    Message message = messageBuilder.build( mail );

    Address[] recipients = message.getRecipients( RecipientType.TO );

    assertThat( recipients[ 0 ] ).isEqualTo( new InternetAddress( "foo@bar.com" ) );
  }

  @Test
  public void testAddsCCs() throws MessagingException {
    mail.cc( "a@a.com", "b@b.com" );
    Message message = messageBuilder.build( mail );

    Address[] recipients = message.getRecipients( RecipientType.CC );

    assertThat( recipients[ 0 ] ).isEqualTo( new InternetAddress( "a@a.com" ) );
    assertThat( recipients[ 1 ] ).isEqualTo( new InternetAddress( "b@b.com" ) );
  }

  @Test
  public void testAddsBCCs() throws MessagingException {
    mail.bcc( "a@a.com", "b@b.com" );
    Message message = messageBuilder.build( mail );

    Address[] recipients = message.getRecipients( RecipientType.BCC );

    assertThat( recipients[ 0 ] ).isEqualTo( new InternetAddress( "a@a.com" ) );
    assertThat( recipients[ 1 ] ).isEqualTo( new InternetAddress( "b@b.com" ) );
  }

  @Test
  public void testAddsBody() throws MessagingException, IOException {
    Message message = messageBuilder.build( mail );

    Object content = message.getContent();

    assertThat( content ).isEqualTo( "body" );
  }

  @Test
  public void testAddsFrom() throws MessagingException, IOException {
    Message message = messageBuilder.build( mail );

    Address[] from = message.getFrom();

    assertThat( from[ 0 ] ).isEqualTo( new InternetAddress( "send@test.com" ) );
  }

  @Test
  public void testAddsHtmlContent() throws MessagingException, IOException {
    mail.html( true );
    Message message = messageBuilder.build( mail );

    String contentType = message.getDataHandler().getContentType();

    assertThat( contentType ).isEqualTo( "text/html; charset=utf-8" );
  }

  @Test
  public void testAddsSubject() throws MessagingException, IOException {
    mail.subject( "subject" );
    Message message = messageBuilder.build( mail );

    String subject = message.getSubject();

    assertThat( subject ).isEqualTo( "subject" );
  }

  @Test
  public void testAddsEmptySubjectForNullSubject() throws MessagingException, IOException {
    Message message = messageBuilder.build( mail );

    String subject = message.getSubject();

    assertThat( subject ).isEqualTo( "" );
  }

  @Test
  public void testAddsAttachment() throws MessagingException, IOException {
    mail.attachement( new ByteArrayInputStream( "foo".getBytes() ), "template.txt", "text/plain" );
    Message message = messageBuilder.build( mail );

    MimeMultipart content = ( MimeMultipart )message.getContent();

    assertThat( content.getBodyPart( 0 ).getContent() ).isEqualTo( "body" );
    assertThat( content.getBodyPart( 1 ).getFileName() ).isEqualTo( "template.txt" );
    Object attachemntContent = content.getBodyPart( 1 ).getContent();
    assertThat( new String( ( byte[] )attachemntContent ) ).isEqualTo( "foo" );
    assertThat( content.getBodyPart( 1 ).getContentType() ).isEqualTo( "text/plain" );
  }
}
