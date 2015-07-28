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

import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;

import com.eclipsesource.connect.test.util.FileHelper;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;


public class GitHubMarkdownParserTest {

  @Rule
  public ClientDriverRule driver = new ClientDriverRule();


  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullString() {
    GitHubMarkdownParser parser = new GitHubMarkdownParser();

    parser.parse( null );
  }

  @Test
  public void testProcessesIndex() {
    String fileContent = read( "/index.md" );
    driver.addExpectation( onRequestTo( "/markdown" )
                             .withParam( "access_token", "token" )
                             .withMethod( Method.POST )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( fileContent, "application/json" ) );
    GitHubMarkdownParser parser = new GitHubMarkdownParser( driver.getBaseUrl() );
    parser.setConfiguration( getConfig() );

    String content = parser.parse( fileContent );

    assertThat( content.trim() ).isEqualTo( "index" );
  }

  @Test
  public void testProcessesFileOnRootWithSlash() {
    String fileContent = read( "/a.md" );
    driver.addExpectation( onRequestTo( "/markdown" )
                             .withParam( "access_token", "token" )
                             .withMethod( Method.POST )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( fileContent, "application/json" ) );
    GitHubMarkdownParser parser = new GitHubMarkdownParser( driver.getBaseUrl() );
    parser.setConfiguration( getConfig() );

    String content = parser.parse( fileContent );

    assertThat( content.trim() ).isEqualTo( "a" );
  }

  @Test
  public void testProcessesFileOnRootWithoutSlash() {
    String fileContent = read( "/a.md" );
    driver.addExpectation( onRequestTo( "/markdown" )
                             .withParam( "access_token", "token" )
                             .withMethod( Method.POST )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( fileContent, "application/json" ) );
    GitHubMarkdownParser parser = new GitHubMarkdownParser( driver.getBaseUrl() );
    parser.setConfiguration( getConfig() );

    String content = parser.parse( fileContent );

    assertThat( content.trim() ).isEqualTo( "a" );
  }

  @Test
  public void testProcessesFileOnSubFolder() {
    String fileContent = read( "/b/b.md" );
    driver.addExpectation( onRequestTo( "/markdown" )
                             .withParam( "access_token", "token" )
                             .withMethod( Method.POST )
                             .withHeader( "Accept", "application/vnd.github.v3+json" ),
                           giveResponse( fileContent, "application/json" ) );
    GitHubMarkdownParser parser = new GitHubMarkdownParser( driver.getBaseUrl() );
    parser.setConfiguration( getConfig() );

    String content = parser.parse( fileContent );

    assertThat( content.trim() ).isEqualTo( "b" );
  }

  private String read( String fileName ) {
    return FileHelper.readFile( fileName, GitHubMarkdownParserTest.class );
  }

  private GitHubMarkdownParserConfiguration getConfig() {
    GitHubMarkdownParserConfiguration config = mock( GitHubMarkdownParserConfiguration.class );
    when( config.getAccessToken() ).thenReturn( "token" );
    return config;
  }
}
