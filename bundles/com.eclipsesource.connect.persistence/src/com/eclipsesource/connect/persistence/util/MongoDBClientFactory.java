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
package com.eclipsesource.connect.persistence.util;

import java.net.UnknownHostException;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;


public class MongoDBClientFactory {

  private MongoClient client;

  // TODO: Needs to be tested by integration tests
  public MongoDatabase createDB( String host, String dbName, int port, String user, String password ) {
    try {
      return doCreateDB( host, dbName, port, user, password );
    } catch( UnknownHostException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private MongoDatabase doCreateDB( String host, String dbName, int port, String user, String password ) throws UnknownHostException {
    if( client != null ) {
      client.close();
    }
    if( user != null && password != null ) {
      MongoCredential credential = MongoCredential.createCredential( user, "admin", password.toCharArray() );
      client = new MongoClient(new ServerAddress(host, port), Lists.newArrayList( credential ) );
    } else {
      client = new MongoClient( host, port );
    }
    return client.getDatabase( dbName );
  }

}
