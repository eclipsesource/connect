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
package com.eclipsesource.connect.mvc.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.slf4j.Logger;


public class ErrorExceptionMapperTest {

  @Test
  public void testLogsOnGetStatus() {
    ErrorExceptionMapper.LOG = mock( Logger.class );
    ErrorExceptionMapper mapper = new ErrorExceptionMapper();
    IllegalStateException throwable = new IllegalStateException();

    mapper.getErrorStatus( throwable );

    verify( ErrorExceptionMapper.LOG ).error( "Unxepected Problem while processing request", throwable );
  }

}
