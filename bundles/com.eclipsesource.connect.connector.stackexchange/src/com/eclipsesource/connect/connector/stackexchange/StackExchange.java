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
package com.eclipsesource.connect.connector.stackexchange;

import static com.google.common.base.Preconditions.checkArgument;

import com.eclipsesource.connect.connector.stackexchange.internal.StackExchangeFactory;
import com.eclipsesource.connect.connector.stackexchange.model.SEUser;


public class StackExchange {

  public static final String API_URL = "https://api.stackexchange.com/2.2";

  private final StackExchangeFactory stackExchangeFactory;

  public StackExchange( String accessToken, String key, String baseUrl ) {
    validateArguments( accessToken, key, baseUrl );
    this.stackExchangeFactory = new StackExchangeFactory( baseUrl, key, accessToken );
  }

  private void validateArguments( String accessToken, String key, String baseUrl ) {
    checkArgument( accessToken != null, "Access Token must not be null" );
    checkArgument( !accessToken.trim().isEmpty(), "Access Token must not be empty" );
    checkArgument( key != null, "Key must not be null" );
    checkArgument( !key.trim().isEmpty(), "Key must not be empty" );
    checkArgument( baseUrl != null, "Base URL must not be null" );
    checkArgument( !baseUrl.trim().isEmpty(), "Base URL must not be empty" );
  }

  public SEUser getUser( Site site ) {
    return stackExchangeFactory.get( "/me", site, SEUser.class );
  }

}
