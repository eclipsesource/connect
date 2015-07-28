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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;


public class CallbackScheduledExecutorServiceTest {

  private CallbackScheduledExecutorService executor;
  private ListeningScheduledExecutorService delegate;

  @Before
  public void setUp() {
    delegate = mock( ListeningScheduledExecutorService.class );
    executor = new CallbackScheduledExecutorService( delegate, r -> {}, t -> {} );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullDelegate() {
    new CallbackScheduledExecutorService( null, r -> {}, t -> {} );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullSuccessCallback() {
    new CallbackScheduledExecutorService( delegate, null, t -> {} );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullFailureCallback() {
    new CallbackScheduledExecutorService( delegate, r -> {}, null );
  }

  @Test
  public void testDelegatesShutdown() {
    executor.shutdown();

    verify( delegate ).shutdown();
  }

  @Test
  public void testDelegatesShutdownNow() {
    executor.shutdownNow();

    verify( delegate ).shutdownNow();
  }

  @Test
  public void testDelegatesIsShutdown() {
    executor.isShutdown();

    verify( delegate ).isShutdown();
  }

  @Test
  public void testDelegatesIsTerminated() {
    executor.isTerminated();

    verify( delegate ).isTerminated();
  }

  @Test
  public void testDelegatesAwaitsTermination() throws InterruptedException {
    executor.awaitTermination( 23L, TimeUnit.DAYS );

    verify( delegate ).awaitTermination( 23L, TimeUnit.DAYS );
  }

  @Test
  public void testDelegatesInvokeAny() throws InterruptedException, ExecutionException {
    Collection<Callable<Object>> tasks = new ArrayList<>();

    executor.invokeAny( tasks );

    verify( delegate ).invokeAny( tasks );
  }

  @Test
  public void testDelegatesInvokeAnyDelayed() throws InterruptedException, ExecutionException, TimeoutException {
    Collection<Callable<Object>> tasks = new ArrayList<>();

    executor.invokeAny( tasks, 23L, TimeUnit.DAYS );

    verify( delegate ).invokeAny( tasks, 23L, TimeUnit.DAYS );
  }

  @Test
  public void testDelegatesExecute() {
    Runnable command = mock( Runnable.class );
    executor.execute( command );

    verify( delegate ).execute( command );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testDelegatesSubmitWithCallable() {
    Callable<Object> callable = mock( Callable.class );
    ListenableFuture<Object> future = mock( ListenableFuture.class );
    when( delegate.submit( callable ) ).thenReturn( future );

    executor.submit( callable );

    verify( delegate ).submit( callable );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testDelegatesSubmitWithCallableAndAddsCallback() {
    Callable<Object> callable = mock( Callable.class );
    ListenableFuture<Object> future = mock( ListenableFuture.class );
    when( delegate.submit( callable ) ).thenReturn( future );

    executor.submit( callable );

    verify( future ).addListener( any( Runnable.class ), any( ExecutorService.class ) );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesSubmitWithRunnable() {
    Runnable runnable = mock( Runnable.class );
    ListenableFuture future = mock( ListenableFuture.class );
    when( delegate.submit( runnable ) ).thenReturn( future );

    executor.submit( runnable );

    verify( delegate ).submit( runnable );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesSubmitWithRunnableAndAddsCallback() {
    Runnable runnable = mock( Runnable.class );
    ListenableFuture future = mock( ListenableFuture.class );
    when( delegate.submit( runnable ) ).thenReturn( future );

    executor.submit( runnable );

    verify( future ).addListener( any( Runnable.class ), any( ExecutorService.class ) );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesSubmitWithRunnableAndResult() {
    Runnable runnable = mock( Runnable.class );
    ListenableFuture future = mock( ListenableFuture.class );
    Object result = new Object();
    when( delegate.submit( runnable, result ) ).thenReturn( future );

    executor.submit( runnable, result );

    verify( delegate ).submit( runnable, result );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesSubmitWithRunnableAndResultAndAddsCallback() {
    Runnable runnable = mock( Runnable.class );
    ListenableFuture future = mock( ListenableFuture.class );
    Object result = new Object();
    when( delegate.submit( runnable, result ) ).thenReturn( future );

    executor.submit( runnable, result );

    verify( future ).addListener( any( Runnable.class ), any( ExecutorService.class ) );
  }

  @Test
  public void testDelegatesInvokeAll() throws InterruptedException, ExecutionException {
    Collection<Callable<Object>> tasks = new ArrayList<>();

    executor.invokeAll( tasks );

    verify( delegate ).invokeAll( tasks );
  }

  @Test
  public void testDelegatesInvokeAllDelayed() throws InterruptedException, ExecutionException {
    Collection<Callable<Object>> tasks = new ArrayList<>();

    executor.invokeAll( tasks, 23L, TimeUnit.DAYS );

    verify( delegate ).invokeAll( tasks, 23L, TimeUnit.DAYS );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesScheduleWithRunnable() {
    Runnable runnable = mock( Runnable.class );
    ListenableScheduledFuture future = mock( ListenableScheduledFuture.class );
    when( delegate.schedule( runnable, 23L, TimeUnit.DAYS ) ).thenReturn( future );

    executor.schedule( runnable, 23L, TimeUnit.DAYS );

    verify( delegate ).schedule( runnable, 23L, TimeUnit.DAYS );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesScheduleWithRunnableAndAddsCalback() {
    Runnable runnable = mock( Runnable.class );
    ListenableScheduledFuture future = mock( ListenableScheduledFuture.class );
    when( delegate.schedule( runnable, 23L, TimeUnit.DAYS ) ).thenReturn( future );

    executor.schedule( runnable, 23L, TimeUnit.DAYS );

    verify( future ).addListener( any( Runnable.class ), any( ExecutorService.class ) );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesScheduleWithCallable() {
    Callable<Object> callable = mock( Callable.class );
    ListenableScheduledFuture future = mock( ListenableScheduledFuture.class );
    when( delegate.schedule( callable, 23L, TimeUnit.DAYS ) ).thenReturn( future );

    executor.schedule( callable, 23L, TimeUnit.DAYS );

    verify( delegate ).schedule( callable, 23L, TimeUnit.DAYS );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesScheduleWithCallableAndAddsCallback() {
    Callable<Object> callable = mock( Callable.class );
    ListenableScheduledFuture future = mock( ListenableScheduledFuture.class );
    when( delegate.schedule( callable, 23L, TimeUnit.DAYS ) ).thenReturn( future );

    executor.schedule( callable, 23L, TimeUnit.DAYS );

    verify( future ).addListener( any( Runnable.class ), any( ExecutorService.class ) );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesScheduleAtFixedRateWithRunnable() {
    Runnable runnable = mock( Runnable.class );
    ListenableScheduledFuture future = mock( ListenableScheduledFuture.class );
    when( delegate.scheduleAtFixedRate( runnable, 23L, 23L, TimeUnit.DAYS ) ).thenReturn( future );

    executor.scheduleAtFixedRate( runnable, 23L, 23L, TimeUnit.DAYS );

    verify( delegate ).scheduleAtFixedRate( runnable, 23L, 23L, TimeUnit.DAYS );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesScheduleAtFixedRateWithRunnableAndAddsCallback() {
    Runnable runnable = mock( Runnable.class );
    ListenableScheduledFuture future = mock( ListenableScheduledFuture.class );
    when( delegate.scheduleAtFixedRate( runnable, 23L, 23L, TimeUnit.DAYS ) ).thenReturn( future );

    executor.scheduleAtFixedRate( runnable, 23L, 23L, TimeUnit.DAYS );

    verify( future ).addListener( any( Runnable.class ), any( ExecutorService.class ) );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesScheduleWithDelayWithRunnable() {
    Runnable runnable = mock( Runnable.class );
    ListenableScheduledFuture future = mock( ListenableScheduledFuture.class );
    when( delegate.scheduleWithFixedDelay( runnable, 23L, 23L, TimeUnit.DAYS ) ).thenReturn( future );

    executor.scheduleWithFixedDelay( runnable, 23L, 23L, TimeUnit.DAYS );

    verify( delegate ).scheduleWithFixedDelay( runnable, 23L, 23L, TimeUnit.DAYS );
  }

  @Test
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testDelegatesScheduleWithDelayWithRunnableAndAddsCallback() {
    Runnable runnable = mock( Runnable.class );
    ListenableScheduledFuture future = mock( ListenableScheduledFuture.class );
    when( delegate.scheduleWithFixedDelay( runnable, 23L, 23L, TimeUnit.DAYS ) ).thenReturn( future );

    executor.scheduleWithFixedDelay( runnable, 23L, 23L, TimeUnit.DAYS );

    verify( future ).addListener( any( Runnable.class ), any( ExecutorService.class ) );
  }

}
