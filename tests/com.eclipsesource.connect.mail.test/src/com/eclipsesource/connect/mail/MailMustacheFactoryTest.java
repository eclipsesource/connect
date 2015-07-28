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
import java.io.Reader;

import org.junit.Test;

import com.eclipsesource.connect.api.asset.AssetsFinder;
import com.eclipsesource.connect.api.asset.AssetsResult;
import com.eclipsesource.connect.test.util.FileHelper;


public class MailMustacheFactoryTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSendMail() {
    new MailMustacheFactory( null );
  }

  @Test
  public void testReturnsReaderFromContent() throws IOException {
    SendMailImpl sendMail = mock( SendMailImpl.class );
    AssetsFinder finder = mock( AssetsFinder.class );
    when( finder.find( "test.foo" ) ).thenReturn( new AssetsResult( new ByteArrayInputStream( "foo".getBytes() ), MailMustacheFactoryTest.class.getClassLoader() ) );
    when( sendMail.getAssetsFinder() ).thenReturn( finder );
    MailMustacheFactory mustacheFactory = new MailMustacheFactory( sendMail );

    Reader reader = mustacheFactory.getReader( "test.foo" );

    String readContent = FileHelper.readFile( () -> { return reader; } );
    assertThat( readContent ).isEqualTo( "foo" );
  }
}
