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
package com.eclipsesource.connect.connector.github.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.Test;


public class GHScopeTest {

  @Test
  public void testFindsScopeForString() {
    GHScope[] values = GHScope.values();

    Stream.of( values ).forEach( value -> assertThat( GHScope.fromString( value.getScope() ) ).isSameAs( value ) );
  }

  @Test
  public void testFindsNoScopeForUnknownString() {
    GHScope scope = GHScope.fromString( "foo" );

    assertThat( scope ).isNull();
  }

  @Test
  public void testScopeUser() {
    GHScope scope = GHScope.USER;

    assertThat( scope.getScope() ).isEqualTo( "user" );
  }

  @Test
  public void testScopeUserEmail() {
    GHScope scope = GHScope.USER_EMAIL;

    assertThat( scope.getScope() ).isEqualTo( "user:email" );
  }

  @Test
  public void testScopeUserFollow() {
    GHScope scope = GHScope.USER_FOLLOW;

    assertThat( scope.getScope() ).isEqualTo( "user:follow" );
  }

  @Test
  public void testScopePublicRepo() {
    GHScope scope = GHScope.PUBLIC_REPO;

    assertThat( scope.getScope() ).isEqualTo( "public_repo" );
  }

  @Test
  public void testScopeRepo() {
    GHScope scope = GHScope.REPO;

    assertThat( scope.getScope() ).isEqualTo( "repo" );
  }

  @Test
  public void testScopeRepoDeployment() {
    GHScope scope = GHScope.REPO_DEPLOYMENT;

    assertThat( scope.getScope() ).isEqualTo( "repo_deployment" );
  }

  @Test
  public void testScopeRepoStatus() {
    GHScope scope = GHScope.REPO_STATUS;

    assertThat( scope.getScope() ).isEqualTo( "repo:status" );
  }

  @Test
  public void testScopeRepoDelete() {
    GHScope scope = GHScope.REPO_DELETE;

    assertThat( scope.getScope() ).isEqualTo( "delete_repo" );
  }

  @Test
  public void testScopeNotifications() {
    GHScope scope = GHScope.NOTIFICATIONS;

    assertThat( scope.getScope() ).isEqualTo( "notifications" );
  }

  @Test
  public void testScopeGist() {
    GHScope scope = GHScope.GIST;

    assertThat( scope.getScope() ).isEqualTo( "gist" );
  }

  @Test
  public void testScopeRepoHookRead() {
    GHScope scope = GHScope.REPO_HOOK_READ;

    assertThat( scope.getScope() ).isEqualTo( "read:repo_hook" );
  }

  @Test
  public void testScopeRepoHookWrite() {
    GHScope scope = GHScope.REPO_HOOK_WRITE;

    assertThat( scope.getScope() ).isEqualTo( "write:repo_hook" );
  }

  @Test
  public void testScopeRepoHookAdmin() {
    GHScope scope = GHScope.REPO_HOOK_ADMIN;

    assertThat( scope.getScope() ).isEqualTo( "admin:repo_hook" );
  }

  @Test
  public void testScopeOrgRead() {
    GHScope scope = GHScope.ORG_READ;

    assertThat( scope.getScope() ).isEqualTo( "read:org" );
  }

  @Test
  public void testScopeOrgWrite() {
    GHScope scope = GHScope.ORG_WRITE;

    assertThat( scope.getScope() ).isEqualTo( "write:org" );
  }

  @Test
  public void testScopeOrgAdmin() {
    GHScope scope = GHScope.ORG_ADMIN;

    assertThat( scope.getScope() ).isEqualTo( "admin:org" );
  }

  @Test
  public void testScopePublicKeyRead() {
    GHScope scope = GHScope.PUBLIC_KEY_READ;

    assertThat( scope.getScope() ).isEqualTo( "read:public_key" );
  }

  @Test
  public void testScopePublicKeyWrite() {
    GHScope scope = GHScope.PUBLIC_KEY_WRITE;

    assertThat( scope.getScope() ).isEqualTo( "write:public_key" );
  }

  @Test
  public void testScopePublicKeyAdmin() {
    GHScope scope = GHScope.PUBLIC_KEY_ADMIN;

    assertThat( scope.getScope() ).isEqualTo( "admin:public_key" );
  }
}
