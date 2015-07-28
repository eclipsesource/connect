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
package com.eclipsesource.connect.mvc.internal;

import static com.github.restdriver.clientdriver.ClientDriverRequest.Method.GET;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.restdriver.clientdriver.ClientDriverRule;


public class ProxyResourceTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();

  private ProxyResource resource;

  @Before
  public void setUp() {
    resource = new ProxyResource();
  }

  @Test
  public void testHasPath() {
    String path = ProxyResource.class.getAnnotation( Path.class ).value();

    assertThat( path ).isEqualTo( "/proxy" );
  }

  @Test
  public void testProxiesRequest() {
    driver.addExpectation( onRequestTo( "/foo.js" )
                             .withMethod( GET ),
                           giveResponse( "blub", "application/javascript" ) );

    Response response = resource.get( driver.getBaseUrl() + "/foo.js" );

    String entity = response.readEntity( String.class );
    assertThat( entity ).isEqualTo( "blub" );
  }

  @Test
  public void testFailsWithNullSource() {
    Response response = resource.get( null );

    assertThat( response.getStatus() ).isEqualTo( 400 );
  }

  @Test
  public void testFailsWithEmptySource() {
    Response response = resource.get( "" );

    assertThat( response.getStatus() ).isEqualTo( 400 );
  }

  @Test
  public void testFailsWithEmptyWhitespaceSource() {
    Response response = resource.get( " " );

    assertThat( response.getStatus() ).isEqualTo( 400 );
  }
}
