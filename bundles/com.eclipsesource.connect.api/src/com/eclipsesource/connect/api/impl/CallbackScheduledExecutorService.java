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
import static com.google.common.util.concurrent.Futures.addCallback;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.eclipsesource.connect.api.util.ExecutorFailureCallback;
import com.eclipsesource.connect.api.util.ExecutorSuccessCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;


public class CallbackScheduledExecutorService implements ListeningScheduledExecutorService {

  private final ListeningScheduledExecutorService delegate;
  private final ExecutorFailureCallback failureCallback;
  private final ExecutorSuccessCallback successCallback;

  public CallbackScheduledExecutorService( ListeningScheduledExecutorService delegate,
                                               ExecutorSuccessCallback successCallback ,
                                               ExecutorFailureCallback successHandler )
  {
    checkArgument( delegate != null, "Delegate must not be null" );
    checkArgument( successCallback != null, "ExecutorSuccessCallback must not be null" );
    checkArgument( successHandler != null, "ExecutorFaExecutorFailureCallbackilureHandler must not be null" );
    this.delegate = delegate;
    this.successCallback = successCallback;
    this.failureCallback = successHandler;
  }

  @Override
  public void shutdown() {
    delegate.shutdown();
  }

  @Override
  public List<Runnable> shutdownNow() {
    return delegate.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return delegate.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return delegate.isTerminated();
  }

  @Override
  public boolean awaitTermination( long timeout, TimeUnit unit ) throws InterruptedException {
    return delegate.awaitTermination( timeout, unit );
  }

  @Override
  public <T> T invokeAny( Collection<? extends Callable<T>> tasks ) throws InterruptedException, ExecutionException {
    return delegate.invokeAny( tasks );
  }

  @Override
  public <T> T invokeAny( Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit )
    throws InterruptedException, ExecutionException, TimeoutException
  {
    return delegate.invokeAny( tasks, timeout, unit );
  }

  @Override
  public void execute( Runnable command ) {
    delegate.execute( command );
  }

  @Override
  public <T> ListenableFuture<T> submit( Callable<T> task ) {
    ListenableFuture<T> future = delegate.submit( task );
    addCallback( future, new DelegatingFutureCallback( successCallback, failureCallback ) );
    return future;
  }

  @Override
  public ListenableFuture<?> submit( Runnable task ) {
    ListenableFuture<?> future = delegate.submit( task );
    addCallback( future, new DelegatingFutureCallback( successCallback, failureCallback ) );
    return future;
  }

  @Override
  public <T> ListenableFuture<T> submit( Runnable task, T result ) {
    if( result != null ) {
      ListenableFuture<T> future = delegate.submit( task, result );
      addCallback( future, new DelegatingFutureCallback( successCallback, failureCallback ) );
      return future;
    }
    return null;
  }

  @Override
  public <T> List<Future<T>> invokeAll( Collection<? extends Callable<T>> tasks ) throws InterruptedException {
    return delegate.invokeAll( tasks );
  }

  @Override
  public <T> List<Future<T>> invokeAll( Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit ) throws InterruptedException {
    return delegate.invokeAll( tasks, timeout, unit );
  }

  @Override
  public ListenableScheduledFuture<?> schedule( Runnable command, long delay, TimeUnit unit ) {
    ListenableScheduledFuture<?> future = delegate.schedule( command, delay, unit );
    addCallback( future, new DelegatingFutureCallback( successCallback, failureCallback ) );
    return future;
  }

  @Override
  public <V> ListenableScheduledFuture<V> schedule( Callable<V> callable, long delay, TimeUnit unit ) {
    ListenableScheduledFuture<V> future = delegate.schedule( callable, delay, unit );
    addCallback( future, new DelegatingFutureCallback( successCallback, failureCallback ) );
    return future;
  }

  @Override
  public ListenableScheduledFuture<?> scheduleAtFixedRate( Runnable command, long initialDelay, long period, TimeUnit unit ) {
    ListenableScheduledFuture<?> future = delegate.scheduleAtFixedRate( command, initialDelay, period, unit );
    addCallback( future, new DelegatingFutureCallback( successCallback, failureCallback ) );
    return future;
  }

  @Override
  public ListenableScheduledFuture<?> scheduleWithFixedDelay( Runnable command, long initialDelay, long delay, TimeUnit unit ) {
    ListenableScheduledFuture<?> future = delegate.scheduleWithFixedDelay( command, initialDelay, delay, unit );
    addCallback( future, new DelegatingFutureCallback( successCallback, failureCallback ) );
    return future;
  }

}