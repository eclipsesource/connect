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
package com.eclipsesource.connect.markdown;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class MarkdownPostProcessorTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithZeroDepth() {
    new MarkdownPostProcessor( 0 );
  }

  @Test
  public void testAddsIdToH1() {
    MarkdownPostProcessor processor = new MarkdownPostProcessor( 3 );

    String content = processor.process( "<h1>foo</h1>" );

    assertThat( content ).isEqualTo( "<h1 id=\"foo\">foo</h1>\n" );
  }

  @Test
  public void testAddsIdToHDepth() {
    MarkdownPostProcessor processor = new MarkdownPostProcessor( 2 );

    String content = processor.process( "<h1>foo</h1>\n<h2>foo</h2>\n<h3>foo</h3>" );

    assertThat( content ).isEqualTo( "<h1 id=\"foo\">foo</h1>\n<h2 id=\"foo-1\">foo</h2>\n<h3>foo</h3>\n" );
  }

  @Test
  public void testAddsIdToHFullDepth() {
    MarkdownPostProcessor processor = new MarkdownPostProcessor( 3 );

    String content = processor.process( "<h1>foo</h1>\n<h2>foo</h2>\n<h3>foo</h3>" );

    assertThat( content ).isEqualTo( "<h1 id=\"foo\">foo</h1>\n<h2 id=\"foo-1\">foo</h2>\n<h3 id=\"foo-2\">foo</h3>\n" );
  }
}
