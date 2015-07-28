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
package com.eclipsesource.connect.test.util.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


public class TrackingAnswer implements Answer<Object> {

  private List<Method> invocations;

  TrackingAnswer() {
    this.invocations = new ArrayList<>();
  }

  @Override
  public Object answer( InvocationOnMock invocation ) throws Throwable {
    invocations.add( invocation.getMethod() );
    return invocation.callRealMethod();
  }

  List<Method> getInvocations() {
    return invocations;
  }

}