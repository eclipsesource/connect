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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import com.eclipsesource.connect.api.persistence.Query;
import com.eclipsesource.connect.api.persistence.Storage;
import com.eclipsesource.connect.api.persistence.StorageObserver;
import com.eclipsesource.connect.api.security.Token;
import com.eclipsesource.connect.api.security.TokenAdmin;
import com.eclipsesource.connect.api.security.TokenException;
import com.eclipsesource.connect.model.Id;
import com.eclipsesource.connect.model.User;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;


public class TokenAdminImpl implements TokenAdmin, StorageObserver {

  private static final String PLACE_TOKENS = "tokens";

  private final LoadingCache<String, User> cache;
  private Storage storage;

  public TokenAdminImpl() {
    cache = CacheBuilder.newBuilder().build( new CacheLoader<String, User>() {

      @Override
      public User load( String token ) throws Exception {
        User user = loadUser( token );
        if( user == null ) {
          throw new IllegalStateException( "Could not load user for token: " + token );
        }
        return user;
      }

    } );
  }

  private User loadUser( String token ) {
    Token loadedToken = storage.find( new Query<>( PLACE_TOKENS, Token.class ).where( "token", token ) );
    if( loadedToken != null ) {
      return storage.find( new Query<>( "users", User.class ).where( "_id", loadedToken.getUserId() ) );
    }
    throw new IllegalStateException( "Could not load token: " + token );
  }

  @Override
  public Token createToken( User user ) {
    checkArgument( user != null, "User must not be null" );
    ensureServices();
    Token token = createNewToken( user );
    cache.put( token.getToken(), user );
    return token;
  }

  private Token createNewToken( User user ) {
    Token token = new Token( user.getId().toString(), new Id().toString() );
    storage.store( PLACE_TOKENS, token );
    return token;
  }

  @Override
  public User get( String token ) {
    checkArgument( token != null, "Token must not be null" );
    ensureServices();
    return getUser( token );
  }

  @Override
  public Token getToken( User user ) {
    checkArgument( user != null, "User must not be null" );
    List<Token> tokens = storage.findAll( new Query<>( PLACE_TOKENS, Token.class ).where( "userId", user.getId().toString() ) );
    if( tokens.isEmpty() ) {
      Token token = createToken( user );
      return token;
    }
    return tokens.get( 0 );
  }

  private User getUser( String token ) {
    try {
      return cache.getUnchecked( token );
    } catch( UncheckedExecutionException shouldNotHappen ) {
      throw new TokenException( token );
    }
  }

  @Override
  public void destroy( String token ) {
    checkArgument( token != null, "Token must not be null" );
    checkArgument( !token.trim().isEmpty(), "Token must not be empty" );
    ensureServices();
    storage.delete( new Query<>( PLACE_TOKENS, Token.class ).where( "token", token ) );
    cache.invalidate( token );
  }

  @Override
  public void objectsStored( String place, List<Object> objects ) {
    if( place.equals( "users" ) ) {
      objects.forEach( object -> {
        User user = ( User )object;
        List<Token> tokens = storage.findAll( new Query<>( PLACE_TOKENS, Token.class ).where( "userId", user.getId().toString() ) );
        tokens.forEach( token -> cache.put( token.getToken(), user ) );
      } );
    }
  }

  private void ensureServices() {
    checkState( storage != null, "Storage not set" );
  }

  void setStorage( Storage storage ) {
    checkArgument( storage != null, "Storage must not be null" );
    this.storage = storage;
  }

  void unsetStorage( Storage storage ) {
    this.storage = null;
  }

}
