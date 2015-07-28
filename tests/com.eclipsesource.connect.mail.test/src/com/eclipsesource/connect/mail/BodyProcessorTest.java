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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.eclipsesource.connect.api.mail.Mail;
import com.google.common.io.CharSource;
import com.google.common.io.Files;


public class BodyProcessorTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutSendMail() {
    new BodyProcessor( null );
  }

  @Test
  public void testUsesContentIfNoTemplateIsPresent() throws IOException {
    Mail mail = new Mail( "test" ).content( "foo" );
    SendMailImpl sendMail = new SendMailImpl();
    sendMail.setConfiguration( createConfig() );
    BodyProcessor processor = new BodyProcessor( sendMail );

    String body = processor.getBody( mail );

    assertThat( body ).isEqualTo( "foo" );
  }

  @Test
  public void testProcessMailTemplates() throws IOException {
    Map<String, String> templateValues = new HashMap<>();
    templateValues.put( "bar", "foo" );
    Mail mail = new Mail( "test" ).content( "template.mustache", templateValues );
    SendMailImpl sendMail = new SendMailImpl();
    sendMail.setConfiguration( createConfig() );
    BodyProcessor processor = new BodyProcessor( sendMail );

    String body = processor.getBody( mail );

    assertThat( body ).isEqualTo( "foo foo" );
  }

  @Test
  public void testPrefersTemplatesBeforeContent() throws IOException {
    Map<String, String> templateValues = new HashMap<>();
    templateValues.put( "bar", "foo" );
    Mail mail = new Mail( "test" ).content( "template.mustache", templateValues );
    mail.content( "bar" );
    SendMailImpl sendMail = new SendMailImpl();
    sendMail.setConfiguration( createConfig() );
    BodyProcessor processor = new BodyProcessor( sendMail );

    String body = processor.getBody( mail );

    assertThat( body ).isEqualTo( "foo foo" );
  }

  private MailConfiguration createConfig() throws IOException {
    MailConfiguration configuration = mock( MailConfiguration.class );
    String template = new CharSource() {

      @Override
      public Reader openStream() throws IOException {
        return new InputStreamReader( BodyProcessorTest.class.getResourceAsStream( "template.mustache" ), UTF_8 );
      }
    }.read();
    Files.write( template.getBytes( UTF_8 ), new File( folder.getRoot(), "template.mustache" ) );
    when( configuration.getTemplateBasePath() ).thenReturn( folder.getRoot().getAbsolutePath() );
    return configuration;
  }

}
