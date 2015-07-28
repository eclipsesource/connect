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
package com.eclipsesource.connect.connector.stackexchange.internal;

import static com.github.restdriver.clientdriver.RestClientDriver.giveResponseAsBytes;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.connect.connector.stackexchange.Site;
import com.eclipsesource.connect.connector.stackexchange.StackExchangeTest;
import com.eclipsesource.connect.connector.stackexchange.model.SEUser;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;


public class StackExchangeFactoryTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullBaseUrl() {
    new StackExchangeFactory( null, "key", "token" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyBaseUrl() {
    new StackExchangeFactory( "", "key", "token" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceBaseUrl() {
    new StackExchangeFactory( " ", "key", "token" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullKey() {
    new StackExchangeFactory( "url", null, "token" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyKey() {
    new StackExchangeFactory( "url", "", "token" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceKey() {
    new StackExchangeFactory( "url", " ", "token" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullToken() {
    new StackExchangeFactory( "url", "key", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyToken() {
    new StackExchangeFactory( "url", "key", "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceToken() {
    new StackExchangeFactory( "url", "key", " " );
  }

  @Test
  public void testSendsGetRequest() {
    driver.addExpectation( onRequestTo( "/me" )
                           .withMethod( Method.GET )
                           .withParam( "access_token", "foo" )
                           .withParam( "key", "key" )
                           .withParam( "site", "stackoverflow" )
                           .withHeader( "Content-Encoding", "gzip" )
                           .withHeader( "Accept", "application/json" ),
                         giveResponseAsBytes( StackExchangeTest.class.getResourceAsStream( "/test-user.json.gz" ), APPLICATION_JSON ) );
    StackExchangeFactory factory = new StackExchangeFactory( driver.getBaseUrl(), "key", "foo" );

    SEUser user = factory.get( "/me", Site.STACKOVERFLOW, SEUser.class );

    assertThat( user.getDisplayName() ).isEqualTo( "FooBar" );
    assertThat( user.getUserId() ).isEqualTo( "2131212" );
  }

  @Test
  public void testSendsGetRequestWithoutSite() {
    driver.addExpectation( onRequestTo( "/me" )
                           .withMethod( Method.GET )
                           .withParam( "access_token", "foo" )
                           .withParam( "key", "key" )
                           .withHeader( "Content-Encoding", "gzip" )
                           .withHeader( "Accept", "application/json" ),
                           giveResponseAsBytes( StackExchangeTest.class.getResourceAsStream( "/test-user.json.gz" ), APPLICATION_JSON ) );
    StackExchangeFactory factory = new StackExchangeFactory( driver.getBaseUrl(), "key", "foo" );

    SEUser user = factory.get( "/me", SEUser.class );

    assertThat( user.getDisplayName() ).isEqualTo( "FooBar" );
    assertThat( user.getUserId() ).isEqualTo( "2131212" );
  }
}
