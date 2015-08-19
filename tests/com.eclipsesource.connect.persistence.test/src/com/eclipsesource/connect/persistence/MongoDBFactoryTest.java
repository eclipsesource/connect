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
package com.eclipsesource.connect.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.osgi.service.cm.ConfigurationException;

import com.eclipsesource.connect.persistence.util.MongoDBClientFactory;
import com.mongodb.client.MongoDatabase;


public class MongoDBFactoryTest {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  private MongoDBFactory mongoDBFactory;
  private Dictionary<String, ?> configuration;

  @Before
  public void setUp() {
    mongoDBFactory = new MongoDBFactory();
    configuration = createValidConfiguration();
  }

  private Dictionary<String, ?> createValidConfiguration() {
    Dictionary<String, Object> configuration = new Hashtable<>();
    configuration.put( MongoDBFactory.PROPERTY_HOST, "localhost" );
    configuration.put( MongoDBFactory.PROPERTY_DB_NAME, "db-name" );
    configuration.put( MongoDBFactory.PROPERTY_PORT, "23" );
    return configuration;
  }

  @Test
  public void testConfigureFailsWithoutHost() throws ConfigurationException {
    configuration.remove( MongoDBFactory.PROPERTY_HOST );

    exception.expect( ConfigurationException.class );
    exception.expectMessage( "MongoDB host must be set" );

    mongoDBFactory.updated( configuration );
  }

  @Test
  public void testConfigureFailsWithoutDBName() throws ConfigurationException {
    configuration.remove( MongoDBFactory.PROPERTY_DB_NAME );

    exception.expect( ConfigurationException.class );
    exception.expectMessage( "MongoDB name must be set" );

    mongoDBFactory.updated( configuration );
  }

  @Test
  public void testConfigureFailsWithoutDBPort() throws ConfigurationException {
    configuration.remove( MongoDBFactory.PROPERTY_PORT );

    exception.expect( ConfigurationException.class );
    exception.expectMessage( "MongoDB port must be set" );

    mongoDBFactory.updated( configuration );
  }

  @Test
  public void testUsesClientFactoryToCreateDB() throws ConfigurationException {
    MongoDatabase db = mock( MongoDatabase.class );
    MongoDBFactory mongoDBFactory = createFactory( db );

    mongoDBFactory.updated( configuration );

    assertThat( mongoDBFactory.getDB() ).isSameAs( db );
  }

  @Test
  public void testUsesClientFactoryToCreateOnlyOneDB() throws ConfigurationException {
    MongoDBFactory mongoDBFactory = createFactory( mock( MongoDatabase.class ) );

    mongoDBFactory.updated( configuration );

    assertThat( mongoDBFactory.getDB() ).isSameAs( mongoDBFactory.getDB() ).isNotNull();
  }

  private MongoDBFactory createFactory( MongoDatabase db ) {
    MongoDBClientFactory clientFactory = mock( MongoDBClientFactory.class );
    when( clientFactory.createDB( anyString(), anyString(), anyInt() ) ).thenReturn( db );
    MongoDBFactory mongoDBFactory = new MongoDBFactory( clientFactory );
    return mongoDBFactory;
  }
}
