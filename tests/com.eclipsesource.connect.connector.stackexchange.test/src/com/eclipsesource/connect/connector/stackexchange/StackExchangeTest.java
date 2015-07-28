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

import static com.github.restdriver.clientdriver.RestClientDriver.giveResponseAsBytes;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.connect.connector.stackexchange.Site;
import com.eclipsesource.connect.connector.stackexchange.StackExchange;
import com.eclipsesource.connect.connector.stackexchange.model.SEUser;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;


public class StackExchangeTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutAccessToken() {
    new StackExchange( null, "key", "url" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsEmptyAccessToken() {
    new StackExchange( "", "key", "url" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsEmptyWhitespaceAccessToken() {
    new StackExchange( " ", "key", "url" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBaseUrl() {
    new StackExchange( "foo", "key", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyBaseUrl() {
    new StackExchange( "foo", "key", "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceBaseUrl() {
    new StackExchange( "foo", "key", " " );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullKey() {
    new StackExchange( "foo", null, "url" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyKey() {
    new StackExchange( "foo", "", "url" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceKey() {
    new StackExchange( "foo", " ", "url" );
  }

  @Test
  public void testHasBaseUrl() {
    assertThat( StackExchange.API_URL ).isEqualTo( "https://api.stackexchange.com/2.2" );
  }

  @Test
  public void testFetchesUser() {
    driver.addExpectation( onRequestTo( "/me" )
                             .withMethod( Method.GET )
                             .withParam( "access_token", "foo" )
                             .withParam( "key", "key" )
                             .withParam( "site", "stackoverflow" )
                             .withHeader( "Content-Encoding", "gzip" )
                             .withHeader( "Accept", "application/json" ),
                           giveResponseAsBytes( StackExchangeTest.class.getResourceAsStream( "/test-user.json.gz" ), "application/json" ) );
    StackExchange stackoverflow = new StackExchange( "foo", "key", driver.getBaseUrl() );

    SEUser user = stackoverflow.getUser( Site.STACKOVERFLOW );

    assertThat( user.getDisplayName() ).isEqualTo( "FooBar" );
    assertThat( user.getUserId() ).isEqualTo( "2131212" );
  }


}
