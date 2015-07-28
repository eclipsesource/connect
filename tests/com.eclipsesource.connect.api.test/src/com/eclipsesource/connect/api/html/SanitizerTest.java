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
package com.eclipsesource.connect.api.html;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;


public class SanitizerTest {

  private Sanitizer sanitizer;

  @Before
  public void setUp() {
    sanitizer = new Sanitizer();
  }

  @Test
  public void testSanitizesOneWords() {
    String toSanitize = "Native";

    String sanitized = sanitizer.sanitize( toSanitize );

    assertThat( sanitized ).isEqualTo( "native" );
  }

  @Test
  public void testSanitizesAvoidsDuplicate() {
    String toSanitize = "Native";

    String sanitized = sanitizer.sanitize( toSanitize );
    String sanitized2 = sanitizer.sanitize( toSanitize );

    assertThat( sanitized ).isEqualTo( "native" );
    assertThat( sanitized2 ).isEqualTo( "native-1" );
  }

  @Test
  public void testSanitizesAvoidsMoreDuplicate() {
    String toSanitize = "Native";

    String sanitized = sanitizer.sanitize( toSanitize );
    String sanitized2 = sanitizer.sanitize( toSanitize );
    String sanitized3 = sanitizer.sanitize( toSanitize );
    String sanitized4 = sanitizer.sanitize( toSanitize );

    assertThat( sanitized ).isEqualTo( "native" );
    assertThat( sanitized2 ).isEqualTo( "native-1" );
    assertThat( sanitized3 ).isEqualTo( "native-2" );
    assertThat( sanitized4 ).isEqualTo( "native-3" );
  }

  @Test
  public void testSanitizesTwoWords() {
    String toSanitize = "Native Widgets";

    String sanitized = sanitizer.sanitize( toSanitize );

    assertThat( sanitized ).isEqualTo( "native-widgets" );
  }

  @Test
  public void testSanitizesTwoWordsWithSlashes() {
    String toSanitize = "Native/Widgets";

    String sanitized = sanitizer.sanitize( toSanitize );

    assertThat( sanitized ).isEqualTo( "nativewidgets" );
  }

  @Test
  public void testSanitizesMethod() {
    String toSanitize = "get(name)";

    String sanitized = sanitizer.sanitize( toSanitize );

    assertThat( sanitized ).isEqualTo( "getname" );
  }

  @Test
  public void testSanitizesMethodWithParameter() {
    String toSanitize = "foo.create(type, properties*)";

    String sanitized = sanitizer.sanitize( toSanitize );

    assertThat( sanitized ).isEqualTo( "foocreatetype-properties" );
  }

  @Test
  public void testSanitizesCrazyMethodWithParameter() {
    String toSanitize = "append(child, child*, ...)";

    String sanitized = sanitizer.sanitize( toSanitize );

    assertThat( sanitized ).isEqualTo( "appendchild-child-" );
  }
}
