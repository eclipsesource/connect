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
package com.eclipsesource.connect.mvc.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.osgi.service.cm.ConfigurationException;


public class StaticResourceConfigurationTest {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  private StaticResourceConfiguration configuration;
  private Dictionary<String, Object> properties;

  @Before
  public void setUp() {
    configuration = new StaticResourceConfiguration();
    properties = createValidConfiguration();
  }

  private Dictionary<String, Object> createValidConfiguration() {
    Dictionary<String, Object> configuration = new Hashtable<>();
    configuration.put( StaticResourceConfiguration.PROPERTY_USE_CACHE, "true" );
    configuration.put( StaticResourceConfiguration.PROPERTY_USE_COMPRESS, "true" );
    return configuration;
  }

  @Test
  public void testConfigureFailsWithoutuseCache() throws ConfigurationException {
    properties.remove( StaticResourceConfiguration.PROPERTY_USE_CACHE );

    exception.expect( ConfigurationException.class );
    exception.expectMessage( "Use Cache must be set" );

    configuration.updated( properties );
  }

  @Test
  public void testConfigureFailsWithoutUseCompress() throws ConfigurationException {
    properties.remove( StaticResourceConfiguration.PROPERTY_USE_COMPRESS );

    exception.expect( ConfigurationException.class );
    exception.expectMessage( "Use Compress must be set" );

    configuration.updated( properties );
  }

  @Test
  public void testHasUseCache() throws ConfigurationException {
    configuration.updated( properties );

    boolean useCache = configuration.useCache();

    assertThat( useCache ).isTrue();
  }

  @Test
  public void testHasFalseUseCache() throws ConfigurationException {
    properties.put( StaticResourceConfiguration.PROPERTY_USE_CACHE, "false" );
    configuration.updated( properties );

    boolean useCache = configuration.useCache();

    assertThat( useCache ).isFalse();
  }

  @Test
  public void testHasUseCompress() throws ConfigurationException {
    configuration.updated( properties );

    boolean useCompress = configuration.useCompress();

    assertThat( useCompress ).isTrue();
  }

  @Test
  public void testHasFalseUseCompres() throws ConfigurationException {
    properties.put( StaticResourceConfiguration.PROPERTY_USE_COMPRESS, "false" );
    configuration.updated( properties );

    boolean useCompress = configuration.useCompress();

    assertThat( useCompress ).isFalse();
  }
}
