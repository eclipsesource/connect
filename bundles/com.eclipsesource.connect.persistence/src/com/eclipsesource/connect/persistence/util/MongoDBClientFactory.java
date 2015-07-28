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

import com.mongodb.DB;
import com.mongodb.MongoClient;


public class MongoDBClientFactory {

  // TODO: Needs to be tested by integration tests
  public DB createDB( String host, String dbName, int port ) {
    try {
      return doCreateDB( host, dbName, port );
    } catch( UnknownHostException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private DB doCreateDB( String host, String dbName, int port ) throws UnknownHostException {
    MongoClient client = new MongoClient( host, port );
    return client.getDB( dbName );
  }
}
