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
package com.eclipsesource.connect.security.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;


public class TokenTestUtil {

  public static ContainerRequestContext createContext( NewCookie cookie ) {
    ContainerRequestContext context = mock( ContainerRequestContext.class );
    Map<String, Cookie> cookies = new HashMap<>();
    cookies.put( cookie.getName(), cookie );
    when( context.getCookies() ).thenReturn( cookies );
    return context;
  }
}
