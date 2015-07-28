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
package com.eclipsesource.connect.api.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.eclipsesource.connect.api.mail.Mail.Attachement;


public class MailTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullTo() {
    new Mail( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyTo() {
    new Mail( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceTo() {
    new Mail( " " );
  }

  @Test
  public void testHasTos() {
    Mail mail = new Mail( "foo" );

    List<String> tos = mail.getTos();

    assertThat( tos ).hasSize( 1 ).contains( "foo" );
  }

  @Test
  public void testHasAllTos() {
    Mail mail = new Mail( "foo", "bar", "boo" );

    List<String> tos = mail.getTos();

    assertThat( tos ).hasSize( 3 ).contains( "foo", "bar", "boo" );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testTosAreImmutable() {
    Mail mail = new Mail( "foo" );

    List<String> tos = mail.getTos();

    tos.add( "bar" );
  }

  @Test
  public void testHasCCs() {
    Mail mail = new Mail( "foo" );
    mail.cc( "bar" );

    List<String> ccs = mail.getCCs();

    assertThat( ccs ).hasSize( 1 ).contains( "bar" );
  }

  @Test
  public void testHasAllCCs() {
    Mail mail = new Mail( "foo" );
    mail.cc( "bar", "boo" );

    List<String> ccs = mail.getCCs();

    assertThat( ccs ).hasSize( 2 ).contains( "bar", "boo" );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testCCsAreImmutable() {
    Mail mail = new Mail( "foo" );
    mail.cc( "bar", "boo" );

    List<String> ccs = mail.getCCs();

    ccs.add( "42" );
  }

  @Test
  public void testHasBCCs() {
    Mail mail = new Mail( "foo" );
    mail.bcc( "bar" );

    List<String> bccs = mail.getBCCs();

    assertThat( bccs ).hasSize( 1 ).contains( "bar" );
  }

  @Test
  public void testHasAllBCCs() {
    Mail mail = new Mail( "foo" );
    mail.bcc( "bar", "boo" );

    List<String> bccs = mail.getBCCs();

    assertThat( bccs ).hasSize( 2 ).contains( "bar", "boo" );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testBCCsAreImmutable() {
    Mail mail = new Mail( "foo" );
    mail.bcc( "bar", "boo" );

    List<String> bccs = mail.getBCCs();

    bccs.add( "42" );
  }

  @Test
  public void testHasContent() {
    Mail mail = new Mail( "foo" );
    mail.content( "foobar" );

    String content = mail.getContent();

    assertThat( content ).isEqualTo( "foobar" );
  }

  @Test
  public void testHasTemplate() {
    Mail mail = new Mail( "foo" );
    mail.content( "bar", new HashMap<String, String>() );

    String template = mail.getTemplate();

    assertThat( template ).isEqualTo( "bar" );
  }

  @Test
  public void testHasTemplateValues() {
    Mail mail = new Mail( "foo" );
    Map<String, String> values = new HashMap<>();
    values.put( "foo", "bar" );
    mail.content( "bar", values );

    Map<String, String> templateValues = mail.getTemplateValues();

    assertThat( templateValues ).hasSize( 1 ).contains( entry( "foo", "bar" ) );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testTemplateValuesAreImmutable() {
    Mail mail = new Mail( "foo" );
    mail.content( "bar", new HashMap<>() );

    Map<String, String> templateValues = mail.getTemplateValues();

    templateValues.put( "a", "b" );
  }

  @Test
  public void testHasAttachements() {
    Mail mail = new Mail( "foo" );
    mail.attachement( new ByteArrayInputStream( "foo".getBytes() ), "foo.pdf", "application/pdf" );

    List<Attachement> attachements = mail.getAttachements();

    assertThat( attachements ).hasSize( 1 );
    assertThat( attachements.get( 0 ).getName() ).isEqualTo( "foo.pdf" );
    assertThat( attachements.get( 0 ).getType() ).isEqualTo( "application/pdf" );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testAttachementsAreImmutable() {
    Mail mail = new Mail( "foo" );
    mail.attachement( new ByteArrayInputStream( "foo".getBytes() ), "foo.pdf", "application/pdf" );

    List<Attachement> attachements = mail.getAttachements();

    attachements.add( null );
  }

  @Test
  public void testHasSubject() {
    Mail mail = new Mail( "foo" );
    mail.subject( "bar" );

    String subject = mail.getSubject();

    assertThat( subject ).isEqualTo( "bar" );
  }

  @Test
  public void testIsHtml() {
    Mail mail = new Mail( "foo" );
    mail.html( true );

    boolean isHtml = mail.isHtml();

    assertThat( isHtml ).isTrue();
  }

  @Test
  public void testIsHtmlIsFalseByDefault() {
    Mail mail = new Mail( "foo" );

    boolean isHtml = mail.isHtml();

    assertThat( isHtml ).isFalse();
  }
}
