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
package com.eclipsesource.connect.api.asset;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * The {@link ETag} annotation can be used on methods that are already annotated with GET, PUT, POST and DELETE.
 * All GET requests to such a method are supporting <a href="http://tools.ietf.org/html/rfc7232#section-2.3">ETag</a>s.
 * This means the first GET request produces a new ETag. ETags will be updated when PUT and POST requests are done to
 * the same URL and delete with DELETE requests.
 * </p>
 * <p>
 * Inspired by <a href="http://www.ikelin.com/implementing-etag-caching-jersey/">Implementing ETag Caching with
 * Jersey</a>.
 * </p>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface ETag {

}
