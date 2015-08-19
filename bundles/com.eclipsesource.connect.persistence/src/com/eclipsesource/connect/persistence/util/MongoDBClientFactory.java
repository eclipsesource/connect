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

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


public class MongoDBClientFactory {

  private MongoClient client;

  // TODO: Needs to be tested by integration tests
  public MongoDatabase createDB( String host, String dbName, int port ) {
    try {
      return doCreateDB( host, dbName, port );
    } catch( UnknownHostException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private MongoDatabase doCreateDB( String host, String dbName, int port ) throws UnknownHostException {
    if( client != null ) {
      client.close();
    }
    client = new MongoClient( host, port );
    return client.getDatabase( dbName );
  }

}
