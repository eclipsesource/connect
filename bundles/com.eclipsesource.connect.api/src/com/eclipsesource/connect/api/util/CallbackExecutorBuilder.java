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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import com.eclipsesource.connect.api.impl.CallbackExecutorService;
import com.eclipsesource.connect.api.impl.CallbackScheduledExecutorService;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;


public class CallbackExecutorBuilder {

  public static CallbackExecutorBuilder newBuilder() {
    return new CallbackExecutorBuilder();
  }

  private ExecutorFailureCallback failureCallback;
  private ExecutorSuccessCallback successCallback;
  private String name;

  private CallbackExecutorBuilder() {
    this.failureCallback = t -> {};
    this.successCallback = r -> {};
    this.name = "CallbackThreadPool";
  }

  public ExecutorService createCachedThreadPool() {
    ThreadFactory threadFactory = createThreadFactory();
    ListeningExecutorService listeningDecorator = listeningDecorator( Executors.newCachedThreadPool( threadFactory ) );
    return new CallbackExecutorService( listeningDecorator, successCallback, failureCallback );
  }

  public ScheduledExecutorService createScheduledThreadPool( int corePoolSize ) {
    ThreadFactory threadFactory = createThreadFactory();
    ListeningScheduledExecutorService listeningDecorator = listeningDecorator( Executors.newScheduledThreadPool( corePoolSize, threadFactory ) );
    return new CallbackScheduledExecutorService( listeningDecorator, successCallback, failureCallback );
  }

  private ThreadFactory createThreadFactory() {
    return new ThreadFactoryBuilder().setNameFormat( name + "-%d" ).setUncaughtExceptionHandler( new UncaughtExceptionHandler() {

      @Override
      public void uncaughtException( Thread thread, Throwable throwable ) {
        failureCallback.onFailure( throwable );
      }
    } ).build();
  }

  public CallbackExecutorBuilder setFailureCallback( ExecutorFailureCallback callback ) {
    checkArgument( callback != null, "callback must not be null" );
    this.failureCallback = callback;
    return this;
  }

  public ExecutorFailureCallback getFailureCallback() {
    return failureCallback;
  }

  public CallbackExecutorBuilder setSuccessCallback( ExecutorSuccessCallback callback ) {
    checkArgument( callback != null, "callback must not be null" );
    this.successCallback = callback;
    return this;
  }

  public ExecutorSuccessCallback getSuccessCallback() {
    return successCallback;
  }

  public CallbackExecutorBuilder setName( String name ) {
    checkArgument( name != null, "Name must not be null" );
    checkArgument( !name.trim().isEmpty(), "Name must not be empty" );
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

}
