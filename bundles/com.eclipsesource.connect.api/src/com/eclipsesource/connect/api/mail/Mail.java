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

import static com.google.common.base.Preconditions.checkArgument;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


public class Mail {

  private final List<String> tos;
  private final List<String> ccs;
  private final List<String> bccs;
  private final List<Attachement> attachements;
  private final Map<String, String> templateValues;
  private String content;
  private String template;
  private String subject;
  private boolean isHtml;

  public Mail( String to ) {
    this( to, new String[]{} );
  }

  public Mail( String to, String... others ) {
    checkArgument( to != null, "To must not be null" );
    checkArgument( !to.trim().isEmpty(), "To must not be empty" );
    tos = Lists.newArrayList( to );
    Stream.of( others ).forEach( other -> tos.add( other ) );
    ccs = new ArrayList<>();
    bccs = new ArrayList<>();
    attachements = new ArrayList<>();
    templateValues = new HashMap<>();
  }

  public List<String> getTos() {
    return ImmutableList.copyOf( tos );
  }

  public Mail cc( String cc ) {
    cc( cc, new String[]{} );
    return this;
  }

  public Mail cc( String cc, String... others ) {
    ccs.add( cc );
    Stream.of( others ).forEach( other -> ccs.add( other ) );
    return this;
  }

  public List<String> getCCs() {
    return ImmutableList.copyOf( ccs );
  }

  public Mail bcc( String bcc ) {
    bcc( bcc, new String[]{} );
    return this;
  }

  public Mail bcc( String bcc, String... others ) {
    bccs.add( bcc );
    Stream.of( others ).forEach( other -> bccs.add( other ) );
    return this;
  }

  public List<String> getBCCs() {
    return ImmutableList.copyOf( bccs );
  }

  public Mail content( String content ) {
    this.content = content;
    return this;
  }

  public String getContent() {
    return content;
  }

  public Mail content( String template, Map<String, String> templateValues ) {
    this.template = template;
    this.templateValues.putAll( templateValues );
    return this;
  }

  public String getTemplate() {
    return template;
  }

  public Map<String, String> getTemplateValues() {
    return ImmutableMap.copyOf( templateValues );
  }

  public Mail attachement( InputStream content, String name, String type ) {
    attachements.add( new Attachement( content, name, type ) );
    return this;
  }

  public List<Attachement> getAttachements() {
    return ImmutableList.copyOf( attachements );
  }

  public Mail subject( String subject ) {
    this.subject = subject;
    return this;
  }

  public String getSubject() {
    return subject;
  }

  public Mail html( boolean isHtml ) {
    this.isHtml = isHtml;
    return this;
  }

  public boolean isHtml() {
    return isHtml;
  }

  public static class Attachement {

    private final InputStream content;
    private final String name;
    private final String type;

    private Attachement( InputStream content, String name, String type ) {
      this.content = content;
      this.name = name;
      this.type = type;
    }

    public InputStream getContent() {
      return content;
    }

    public String getName() {
      return name;
    }

    public String getType() {
      return type;
    }

  }
}
