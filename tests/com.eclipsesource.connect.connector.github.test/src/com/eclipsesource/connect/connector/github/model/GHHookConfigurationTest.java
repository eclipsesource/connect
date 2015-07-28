/***********************************************************import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
ms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.connector.github.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;


public class GHHookConfigurationTest {

  private GHHookConfiguration config;

  @Before
  public void setUp() {
    config = new Gson().fromJson( new InputStreamReader( GHUserTest.class.getResourceAsStream( "/test-hook-config.json" ) ),
                                  GHHookConfiguration.class );
  }

  @Test
  public void testHasUrl() {
    String url = config.getUrl();

    assertThat( url ).isEqualTo( "http://example.com" );
  }

  @Test
  public void testHasContentType() {
    String contentType = config.getContentType();

    assertThat( contentType ).isEqualTo( "json" );
  }

  @Test
  public void testHasUrlWithContructor() {
    GHHookConfiguration configuration = new GHHookConfiguration( "foo", "json" );

    String url = configuration.getUrl();

    assertThat( url ).isEqualTo( "foo" );
  }

  @Test
  public void testContentTypeWithContructor() {
    GHHookConfiguration configuration = new GHHookConfiguration( "foo", "json" );

    String contentType = configuration.getContentType();

    assertThat( contentType ).isEqualTo( "json" );
  }
}
