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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import com.eclipsesource.connect.connector.github.model.GHScope;
import com.google.common.collect.ImmutableList;


public class GitHubScopeException extends RuntimeException {

  private final List<GHScope> requiredNeeded;
  private final List<GHScope> authorizedScopes;

  public GitHubScopeException( List<GHScope> requiredScopes, List<GHScope> authorizedScopes ) {
    checkArgument( requiredScopes != null, "Required Scopes must not be null" );
    checkArgument( authorizedScopes != null, "Authroized Scopes must not be null" );
    this.requiredNeeded = requiredScopes;
    this.authorizedScopes = authorizedScopes;
  }

  public List<GHScope> getRequiredScopes() {
    return ImmutableList.copyOf( requiredNeeded );
  }

  public List<GHScope> getAuthorizedScopes() {
    return ImmutableList.copyOf( authorizedScopes );
  }

}
