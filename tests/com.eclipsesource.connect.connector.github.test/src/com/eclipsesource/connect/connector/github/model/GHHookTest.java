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
package com.eclipsesource.connect.connector.github.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.gson.Gson;


public class GHHookTest {

  private GHHook hook;

  @Before
  public void setUp() {
    hook = new Gson().fromJson( new InputStreamReader( GHUserTest.class.getResourceAsStream( "/test-hook.json" ) ),
                                GHHook.class );
  }

  @Test
  public void testHasUrl() {
    String url = hook.getUrl();

    assertThat( url ).isEqualTo( "https://api.github.com/repos/octocat/Hello-World/hooks/1" );
  }

  @Test
  public void testHasCreatedAt() {
    String createdAt = hook.getCreatedAt();

    assertThat( createdAt ).isEqualTo( "2011-09-06T17:26:27Z" );
  }

  @Test
  public void testHasUpdatedAt() {
    String updatedAt = hook.getUpdatedAt();

    assertThat( updatedAt ).isEqualTo( "2011-09-06T20:39:23Z" );
  }

  @Test
  public void testHasName() {
    String name = hook.getName();

    assertThat( name ).isEqualTo( "web" );
  }

  @Test
  public void testHasEvents() {
    List<String> events = hook.getEvents();

    assertThat( events ).contains( "push", "pull_request" );
  }

  @Test
  public void testHasActivate() {
    boolean active = hook.isActive();

    assertThat( active ).isTrue();
  }

  @Test
  public void testHasId() {
    String id = hook.getId();

    assertThat( id ).isEqualTo( "1" );
  }

  @Test
  public void testhasConfig() {
    GHHookConfiguration config = hook.getConfig();

    assertThat( config ).isNotNull();
  }

  @Test
  public void testHasConfigFromConstructor() {
    GHHookConfiguration configuration = mock( GHHookConfiguration.class );
    GHHook hook = new GHHook( "web", configuration, Lists.newArrayList( "push" ), true );

    GHHookConfiguration config = hook.getConfig();

    assertThat( config ).isSameAs( configuration );
  }

  @Test
  public void testHasEventsFromConstructor() {
    GHHookConfiguration configuration = mock( GHHookConfiguration.class );
    GHHook hook = new GHHook( "web", configuration, Lists.newArrayList( "push" ), true );

    List<String> events = hook.getEvents();

    assertThat( events ).isEqualTo( Lists.newArrayList( "push" ) );
  }

  @Test
  public void testHasActiveFromConstructor() {
    GHHookConfiguration configuration = mock( GHHookConfiguration.class );
    GHHook hook = new GHHook( "web", configuration, Lists.newArrayList( "push" ), true );

    boolean active = hook.isActive();

    assertThat( active ).isTrue();
  }

  @Test
  public void testHasNameFromConstructor() {
    GHHookConfiguration configuration = mock( GHHookConfiguration.class );
    GHHook hook = new GHHook( "web", configuration, Lists.newArrayList( "push" ), true );

    String name = hook.getName();

    assertThat( name ).isEqualTo( "web" );
  }
}
