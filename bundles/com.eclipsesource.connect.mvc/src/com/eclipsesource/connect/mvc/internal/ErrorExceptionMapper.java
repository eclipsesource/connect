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

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.mvc.spi.AbstractErrorTemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Provider
public class ErrorExceptionMapper extends AbstractErrorTemplateMapper<Exception> {

  static Logger LOG = LoggerFactory.getLogger( ErrorExceptionMapper.class );

  @Override
  protected Status getErrorStatus( Exception throwable ) {
    LOG.error( "Unxepected Problem while processing request", throwable );
    return super.getErrorStatus( throwable );
  }

}
