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

import static com.google.common.base.Preconditions.checkArgument;

import com.eclipsesource.connect.api.util.ExecutorFailureCallback;
import com.eclipsesource.connect.api.util.ExecutorSuccessCallback;
import com.google.common.util.concurrent.FutureCallback;


public class DelegatingFutureCallback implements FutureCallback<Object> {

  private final ExecutorFailureCallback failureCallback;
  private final ExecutorSuccessCallback successCallback;

  public DelegatingFutureCallback( ExecutorSuccessCallback successCallback, ExecutorFailureCallback failureCallback ) {
    checkArgument( successCallback != null, "successCallback must not be null" );
    checkArgument( failureCallback != null, "failureCallback must not be null" );
    this.failureCallback = failureCallback;
    this.successCallback = successCallback;
  }

  @Override
  public void onSuccess( Object result ) {
    successCallback.onSuccess( result );
  }

  @Override
  public void onFailure( Throwable throwable ) {
    failureCallback.onFailure( throwable );
  }

}