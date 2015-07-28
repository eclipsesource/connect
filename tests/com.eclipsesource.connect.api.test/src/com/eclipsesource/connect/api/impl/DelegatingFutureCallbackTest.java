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
package com.eclipsesource.connect.api.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.eclipsesource.connect.api.util.ExecutorFailureCallback;
import com.eclipsesource.connect.api.util.ExecutorSuccessCallback;


public class DelegatingFutureCallbackTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSuccessCallback() {
    new DelegatingFutureCallback( null, t -> {} );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullFailureCallback() {
    new DelegatingFutureCallback( r -> {}, null );
  }

  @Test
  public void testDelegatesOnError() {
    ExecutorFailureCallback failureCallback = mock( ExecutorFailureCallback.class );
    ExecutorSuccessCallback successCallback = mock( ExecutorSuccessCallback.class );
    DelegatingFutureCallback callback = new DelegatingFutureCallback( successCallback, failureCallback );
    IllegalStateException throwable = new IllegalStateException( "bar" );

    callback.onFailure( throwable );

    verify( failureCallback ).onFailure( throwable );
  }

  @Test
  public void testDelegatesOnSuccess() {
    ExecutorFailureCallback failureCallback = mock( ExecutorFailureCallback.class );
    ExecutorSuccessCallback successCallback = mock( ExecutorSuccessCallback.class );
    DelegatingFutureCallback callback = new DelegatingFutureCallback( successCallback, failureCallback );
    Object result = new Object();

    callback.onSuccess( result );

    verify( successCallback ).onSuccess( result );
  }
}
