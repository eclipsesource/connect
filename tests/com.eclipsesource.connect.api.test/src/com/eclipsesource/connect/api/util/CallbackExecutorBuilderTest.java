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
package com.eclipsesource.connect.api.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutorService;

import org.junit.Test;

import com.eclipsesource.connect.api.impl.CallbackExecutorService;
import com.eclipsesource.connect.api.impl.CallbackScheduledExecutorService;


public class CallbackExecutorBuilderTest {

  @Test
  public void testCreatesNewBuilds() {
    CallbackExecutorBuilder builder = CallbackExecutorBuilder.newBuilder();
    CallbackExecutorBuilder builder2 = CallbackExecutorBuilder.newBuilder();

    assertThat( builder ).isNotNull().isNotSameAs( builder2 );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullName() {
    CallbackExecutorBuilder.newBuilder().setName( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyName() {
    CallbackExecutorBuilder.newBuilder().setName( "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceName() {
    CallbackExecutorBuilder.newBuilder().setName( " " );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSuccessCallback() {
    CallbackExecutorBuilder.newBuilder().setSuccessCallback( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullFailureCallback() {
    CallbackExecutorBuilder.newBuilder().setFailureCallback( null );
  }

  @Test
  public void testSetsName() {
    CallbackExecutorBuilder builder = CallbackExecutorBuilder.newBuilder().setName( "foo" );

    assertThat( builder.getName() ).isEqualTo( "foo" );
  }

  @Test
  public void testSetsSuccessCallback() {
    ExecutorSuccessCallback callback = mock( ExecutorSuccessCallback.class );

    CallbackExecutorBuilder builder = CallbackExecutorBuilder.newBuilder().setSuccessCallback( callback );

    assertThat( builder.getSuccessCallback() ).isSameAs( callback );
  }

  @Test
  public void testSetsFailureCallback() {
    ExecutorFailureCallback callback = mock( ExecutorFailureCallback.class );

    CallbackExecutorBuilder builder = CallbackExecutorBuilder.newBuilder().setFailureCallback( callback );

    assertThat( builder.getFailureCallback() ).isSameAs( callback );
  }

  @Test
  public void testCreateNewCachedThreadPool() {
    ExecutorService pool = CallbackExecutorBuilder.newBuilder().createCachedThreadPool();

    assertThat( pool ).isNotNull().isInstanceOf( CallbackExecutorService.class );
  }

  @Test
  public void testCreateNewScheduledThreadPool() {
    ExecutorService pool = CallbackExecutorBuilder.newBuilder().createScheduledThreadPool( 2 );

    assertThat( pool ).isNotNull().isInstanceOf( CallbackScheduledExecutorService.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToCreateNewScheduledThreadPoolWithInvalidSize() {
    CallbackExecutorBuilder.newBuilder().createScheduledThreadPool( -1 );
  }

}
