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

import java.io.IOException;
import java.io.StringWriter;

import com.eclipsesource.connect.api.mail.Mail;
import com.github.mustachejava.Mustache;


public class BodyProcessor {

  private final MailMustacheFactory mailMustacheFactory;

  public BodyProcessor( SendMailImpl sendMail ) {
    checkArgument( sendMail != null, "SendMail must not be null" );
    mailMustacheFactory = new MailMustacheFactory( sendMail );
  }

  public String getBody( Mail mail ) {
    if( mail.getTemplate() != null ) {
      return processTemplate( mail );
    }
    return mail.getContent();
  }

  private String processTemplate( Mail mail ) {
    Mustache mustache = mailMustacheFactory.compile( mail.getTemplate() );
    try( StringWriter writer = new StringWriter() ) {
      mustache.execute( writer, mail.getTemplateValues() );
      return writer.toString();
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }
}
