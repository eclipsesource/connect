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
package com.eclipsesource.connect.connector.github;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.eclipsesource.connect.connector.github.model.GHScope;
import com.google.common.collect.Lists;


public class GitHubScopeExceptionTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutRequiredScopes() {
    List<GHScope> authorizedScopes = Lists.newArrayList( GHScope.NOTIFICATIONS );

    throw new GitHubScopeException( null, authorizedScopes );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithoutAuthorizedScopes() {
    List<GHScope> requiredScopes = Lists.newArrayList( GHScope.GIST );

    throw new GitHubScopeException( requiredScopes, null );
  }

  @Test
  public void testHasRequiredScopes() {
    List<GHScope> requiredScopes = Lists.newArrayList( GHScope.GIST );
    List<GHScope> authorizedScopes = Lists.newArrayList( GHScope.NOTIFICATIONS );
    GitHubScopeException exception = new GitHubScopeException( requiredScopes, authorizedScopes );

    List<GHScope> actualRequiredScopes = exception.getRequiredScopes();

    assertThat( actualRequiredScopes ).isEqualTo( requiredScopes );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testRequiredScopesAreImmutable() {
    List<GHScope> requiredScopes = Lists.newArrayList( GHScope.GIST );
    List<GHScope> authorizedScopes = Lists.newArrayList( GHScope.NOTIFICATIONS );
    GitHubScopeException exception = new GitHubScopeException( requiredScopes, authorizedScopes );

    List<GHScope> actualRequiredScopes = exception.getRequiredScopes();

    actualRequiredScopes.add( GHScope.ORG_ADMIN );
  }

  @Test
  public void testHasAuthroizedScopes() {
    List<GHScope> requiredScopes = Lists.newArrayList( GHScope.GIST );
    List<GHScope> authorizedScopes = Lists.newArrayList( GHScope.NOTIFICATIONS );
    GitHubScopeException exception = new GitHubScopeException( requiredScopes, authorizedScopes );

    List<GHScope> actualAuthorizedScopes = exception.getAuthorizedScopes();

    assertThat( actualAuthorizedScopes ).isEqualTo( authorizedScopes );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testAuthroizedScopesAreImmutable() {
    List<GHScope> requiredScopes = Lists.newArrayList( GHScope.GIST );
    List<GHScope> authorizedScopes = Lists.newArrayList( GHScope.NOTIFICATIONS );
    GitHubScopeException exception = new GitHubScopeException( requiredScopes, authorizedScopes );

    List<GHScope> actualAuthorizedScopes = exception.getAuthorizedScopes();

    actualAuthorizedScopes.add( GHScope.ORG_READ );
  }
}
