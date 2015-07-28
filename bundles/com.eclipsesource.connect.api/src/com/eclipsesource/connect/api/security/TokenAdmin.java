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
package com.eclipsesource.connect.api.security;

import com.eclipsesource.connect.model.User;


/**
 * @OSGi-Service Is available as OSGi service during runtime.
 * @noimplement This interface is not intended to be implemented by clients.
 * @since 1.0
 */
public interface TokenAdmin {

  Token createToken( User user );

  User get( String token ) throws TokenException;

  void destroy( String token );

  Token getToken( User user );

}
