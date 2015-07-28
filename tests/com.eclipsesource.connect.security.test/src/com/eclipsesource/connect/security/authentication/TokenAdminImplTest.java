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
package com.eclipsesource.connect.security.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import com.eclipsesource.connect.api.persistence.Query;
import com.eclipsesource.connect.api.persistence.Storage;
import com.eclipsesource.connect.api.security.Token;
import com.eclipsesource.connect.api.security.TokenException;
import com.eclipsesource.connect.model.Id;
import com.eclipsesource.connect.model.User;
import com.google.common.collect.Lists;


public class TokenAdminImplTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToCreateTokenWithNullUser() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();

    tokenAdmin.createToken( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullStorage() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();

    tokenAdmin.setStorage( null );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToCreateTokenWithouStorage() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();

    tokenAdmin.createToken( new User( "foo", "bar" ) );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToGetTokenWithoutStorage() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();

    tokenAdmin.get( "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullToken() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    tokenAdmin.setStorage( mock( Storage.class ) );

    tokenAdmin.get( null );
  }

  @Test( expected = TokenException.class )
  public void testAllowsEmptyToken() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    tokenAdmin.setStorage( mock( Storage.class ) );

    tokenAdmin.get( "" );
  }

  @Test( expected = IllegalStateException.class )
  public void testFailsToDestroyTokenWithoutStorage() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();

    tokenAdmin.destroy( "foo" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testDestroyFailsWithNullToken() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();

    tokenAdmin.destroy( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testDestroyFailsWithEmptyToken() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();

    tokenAdmin.destroy( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testDestroyFailsWithEmptyWhitespaceToken() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();

    tokenAdmin.destroy( " " );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testCreatesNewTokensWhenUsersAreStored() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    Token token = new Token( "foo", "bar" );
    Storage storage = createStorage( token );
    User user = createUser( "lol", "foo" );
    List<Token> tokens = Lists.newArrayList( token );
    Query<Token> query = any( Query.class );
    when( storage.findAll( query ) ).thenReturn( tokens );
    tokenAdmin.setStorage( storage );

    tokenAdmin.objectsStored( "users", Lists.newArrayList( user ) );

    User actualUser = tokenAdmin.get( token.getToken() );
    assertThat( user ).isSameAs( actualUser );
  }

  @Test
  public void testLStoresNewTokenInStorage() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    Storage storage = mock( Storage.class );
    tokenAdmin.setStorage( storage );

    Token token = tokenAdmin.createToken( createUser( "lol", "foo" ) );

    verify( storage ).store( eq( "tokens" ), eq( token ) );
  }

  @Test
  public void testLoadsTokenFromCacheAfterItWasAdded() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    Storage storage = createStorage( new Token( "foo", "bar" ) );
    tokenAdmin.setStorage( storage );

    Token token = tokenAdmin.createToken( createUser( "lol", "foo" ) );
    User user = tokenAdmin.get( token.getToken() );

    assertThat( user.getId().toString() ).isEqualTo( "foo" );
  }

  @Test
  public void testCacheLoadsTokenFromFinder() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    Storage storage = createStorage( new Token( "foo", "bar" ) );
    tokenAdmin.setStorage( storage );

    User user = tokenAdmin.get( "bar" );

    assertThat( user.getId().toString() ).isEqualTo( "bar" );
    verify( storage, times( 2 ) ).find( any() );
  }

  @Test( expected = TokenException.class )
  public void testThrowsTokenExceptionWhenNoTokenFound() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    tokenAdmin.setStorage( mock( Storage.class ) );

    tokenAdmin.get( "bar" );
  }

  @Test
  public void testDestroyDeletesToken() {
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    Token token = new Token( "foo", "bar" );
    Storage storage = createStorage( token );
    tokenAdmin.setStorage( storage );

    tokenAdmin.destroy( "bar" );

    verify( storage ).delete( any( Query.class ) );
  }

  @Test
  public void testGetsUserToken() {
    Token token = new Token( "foo", "bar" );
    Storage storage = createStorage( token );
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    tokenAdmin.setStorage( storage );

    Token actualToken = tokenAdmin.getToken( new User( "foo", "bar" ) );

    assertThat( actualToken ).isNotNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testGetsUserTokenFailsWithNullUser() {
    Token token = new Token( "foo", "bar" );
    Storage storage = createStorage( token );
    TokenAdminImpl tokenAdmin = new TokenAdminImpl();
    tokenAdmin.setStorage( storage );

    tokenAdmin.getToken( null );
  }

  @SuppressWarnings("unchecked")
  private Storage createStorage( Token token ) {
    Storage storage = mock( Storage.class );
    User user = createUser( "foo", "bar" );
    Query<Object> query = any( Query.class );
    when( storage.find( query ) ).thenReturn( token, user );
    when( storage.findAll( query ) ).thenReturn( Lists.newArrayList( token ) );
    return storage;
  }

  private User createUser( String name, String id ) {
    User user = spy( new User( name, "foo@bar.com" ) );
    when( user.getId() ).thenReturn( new Id( id ) );
    return user;
  }

}
