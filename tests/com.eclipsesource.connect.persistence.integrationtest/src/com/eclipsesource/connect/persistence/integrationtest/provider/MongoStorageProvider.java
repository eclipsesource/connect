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
package com.eclipsesource.connect.persistence.integrationtest.provider;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.service.cm.ConfigurationException;

import com.eclipsesource.connect.api.persistence.Storage;
import com.eclipsesource.connect.persistence.MongoDBFactory;
import com.eclipsesource.connect.persistence.MongoStorage;
import com.eclipsesource.connect.persistence.integrationtest.StorageProvider;
import com.eclipsesource.connect.serialization.GsonSerialization;


public class MongoStorageProvider implements StorageProvider {

  @Override
  public Storage getCleanStorage() {
    try {
      clearStorage();
      return getStorage();
    } catch( Exception exception ) {
      throw new IllegalStateException( exception );
    }
  }

  private Storage getStorage() throws ConfigurationException {
    MongoDBFactory dbFactory = getDbFactory();
    dbFactory.getDB().dropDatabase();
    MongoStorage storage = new MongoStorage();
    GsonSerialization serialization = new GsonSerialization();
    serialization.updated( new Hashtable<>() );
    storage.setSerializer( serialization );
    storage.setDeserializer( serialization );
    storage.setDBFactory( dbFactory );
    return storage;
  }

  private void clearStorage() throws ConfigurationException {
    MongoDBFactory dbFactory = getDbFactory();
    dbFactory.getDB().dropDatabase();
  }

  private MongoDBFactory getDbFactory() throws ConfigurationException {
    MongoDBFactory dbFactory = new MongoDBFactory();
    Dictionary<String, Object> properties = new Hashtable<>();
    properties.put( "db.host", "127.0.0.1" );
    properties.put( "db.name", "test" );
    properties.put( "db.port", "27017" );
    dbFactory.updated( properties );
    return dbFactory;
  }
}
