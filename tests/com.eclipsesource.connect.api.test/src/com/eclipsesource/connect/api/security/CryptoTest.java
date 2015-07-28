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
package com.eclipsesource.connect.api.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;



public class CryptoTest {

  private Crypto crypto;

  @Before
  public void setUp() {
    crypto = new Crypto( "myKey" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullKey() {
    new Crypto( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyKey() {
    new Crypto( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceKey() {
    new Crypto( " " );
  }

  @Test
  public void testEncryptsValue() {
    String encrypted = crypto.encrypt( "foo" );

    assertThat( encrypted ).isNotEqualTo( "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToEncryptNullValue() {
    crypto.encrypt( null );
  }

  @Test
  public void testDecryptsValue() {
    String encrypted = crypto.encrypt( "foo" );

    String decrypted = crypto.decrypt( encrypted );

    assertThat( decrypted ).isEqualTo( "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToDecryptNullValue() {
    crypto.decrypt( null );
  }

}
