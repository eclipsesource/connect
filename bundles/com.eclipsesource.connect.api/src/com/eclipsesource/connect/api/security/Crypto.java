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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


public class Crypto {

  private static final String UTF_8 = "UTF-8";
  private static final String PBE_WITH_MD5_AND_DES = "PBEWithMD5AndDES";
  private static final byte[] SALT = {
    ( byte )0xde,
    ( byte )0x33,
    ( byte )0x10,
    ( byte )0x23,
    ( byte )0xde,
    ( byte )0x08,
    ( byte )0x11,
    ( byte )0x12,
  };

  private final char[] cryptoKey;

  public Crypto( String cryptoKey ) {
    checkArgument( cryptoKey != null, "Key must not be null" );
    checkArgument( !cryptoKey.trim().isEmpty(), "Key must not be empty" );
    this.cryptoKey = cryptoKey.toCharArray();
  }

  public String encrypt( String toEncrypt ) {
    checkArgument( toEncrypt != null, "toEncrypt must not be null" );
    try {
      return doEncrypt( toEncrypt );
    } catch( RuntimeException re ) {
      throw re;
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  public String decrypt( String toDecrypt ) {
    checkArgument( toDecrypt != null, "toDecrypt must not be null" );
    try {
      return doDecrypt( toDecrypt );
    } catch( RuntimeException re ) {
      throw re;
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private String doEncrypt( String toEncrypt ) throws Exception {
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( PBE_WITH_MD5_AND_DES );
    SecretKey key = keyFactory.generateSecret( new PBEKeySpec( cryptoKey ) );
    Cipher pbeCipher = Cipher.getInstance( PBE_WITH_MD5_AND_DES );
    pbeCipher.init( Cipher.ENCRYPT_MODE, key, new PBEParameterSpec( SALT, 20 ) );
    return Base64.getEncoder().encodeToString( pbeCipher.doFinal( toEncrypt.getBytes( UTF_8 ) ) );
  }

  private String doDecrypt( String toDecrypt ) throws Exception {
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( PBE_WITH_MD5_AND_DES );
    SecretKey key = keyFactory.generateSecret( new PBEKeySpec( cryptoKey ) );
    Cipher pbeCipher = Cipher.getInstance( PBE_WITH_MD5_AND_DES );
    pbeCipher.init( Cipher.DECRYPT_MODE, key, new PBEParameterSpec( SALT, 20 ) );
    return new String( pbeCipher.doFinal( Base64.getDecoder().decode( toDecrypt ) ), UTF_8 );
  }
}
