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
package com.eclipsesource.connect.connector.stackexchange.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class SEUserTest {

  @Test
  public void testHasUserId() {
    List<Map<String, Object>> items = createItems( "user_id", Double.valueOf( 234.0 ) );
    SEUser user = new SEUser( items );

    String userId = user.getUserId();

    assertThat( userId ).isEqualTo( "234" );
  }

  @Test
  public void testHasDisplayName() {
    List<Map<String, Object>> items = createItems( "display_name", "foo" );
    SEUser user = new SEUser( items );

    String displayName = user.getDisplayName();

    assertThat( displayName ).isEqualTo( "foo" );
  }

  private List<Map<String, Object>> createItems( String name, Object value ) {
    List<Map<String, Object>> items = new ArrayList<>();
    Map<String, Object> map = new HashMap<>();
    map.put( name, value );
    items.add( map );
    return items;
  }
}
