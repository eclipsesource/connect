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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.eclipsesource.connect.api.markdown.MarkdownParser;
import com.eclipsesource.connect.connector.github.GitHub;
import com.eclipsesource.connect.connector.github.GitHubResponse;
import com.eclipsesource.connect.connector.github.model.GHMarkdownConfiguration;


public class GitHubMarkdownParser implements MarkdownParser {

  private final MarkdownPostProcessor postProcessor;
  private final String baseUrl;
  private GitHub gitHub;
  private GitHubMarkdownParserConfiguration configuration;

  public GitHubMarkdownParser() {
    this( GitHub.API_URL );
  }

  GitHubMarkdownParser( String baseUrl ) {
    this.baseUrl = baseUrl;
    this.postProcessor = new MarkdownPostProcessor( 3 );
  }

  @Override
  public String parse( String toParse ) {
    checkArgument( toParse != null, "toParse must not be null" );
    checkState( configuration != null, "Configuration not set" );
    if( gitHub == null ) {
      gitHub = new GitHub( configuration.getAccessToken(), baseUrl );
    }
    GitHubResponse<String> response = gitHub.getMarkdown( new GHMarkdownConfiguration( toParse ) );
    String entity = response.getEntity();
    return postProcessor.process( entity );
  }

  void setConfiguration( GitHubMarkdownParserConfiguration configuration ) {
    checkArgument( configuration != null, "configuration must not be null" );
    this.configuration = configuration;
  }

  void unsetConfiguration( GitHubMarkdownParserConfiguration configuration ) {
    this.configuration = null;
  }
}
