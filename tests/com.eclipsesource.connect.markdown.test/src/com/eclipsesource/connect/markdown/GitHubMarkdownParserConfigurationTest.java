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
package com.eclipsesource.connect.markdown;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;


public class GitHubMarkdownParserConfigurationTest {

  private Dictionary<String, Object> properties;
  private GitHubMarkdownParserConfiguration configuration;

  @Before
  public void setUp() {
    properties = createValidProperties();
    configuration = new GitHubMarkdownParserConfiguration();
  }

  @Test( expected = ConfigurationException.class )
  public void testFailsWithoutToken() throws ConfigurationException {
    properties.remove( GitHubMarkdownParserConfiguration.PROPERTY_ACCESS_TOKEN );

    configuration.updated( properties );
  }

  @Test
  public void testHasToken() throws ConfigurationException {
    configuration.updated( properties );

    String accessToken = configuration.getAccessToken();

    assertThat( accessToken ).isEqualTo( "token" );
  }

  private Dictionary<String, Object> createValidProperties() {
    Dictionary<String, Object> properties = new Hashtable<>();
    properties.put( GitHubMarkdownParserConfiguration.PROPERTY_ACCESS_TOKEN, "token" );
    return properties;
  }
}
