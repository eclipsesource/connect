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
package com.eclipsesource.connect.api.persistence;

import java.util.List;

/**
 * @OSGi-Client-Service Needs to be registered by client as OSGi service.
 * @since 1.0
 */
@FunctionalInterface
public interface StorageObserver {

  void objectsStored( String place, List<Object> objects );

}
