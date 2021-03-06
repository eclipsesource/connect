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

import static org.mockito.Mockito.mock;


public class Annockito {

  public static <T> TrackingStub<T> on( Class<T> type ) {
    TrackingAnswer answer = new TrackingAnswer();
    T mock = mock( type, answer );
    return new TrackingStub<>( mock, answer );
  }


  private Annockito() {
    // prevent instantiation
  }

}
