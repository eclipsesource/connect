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
package com.eclipsesource.connect.persistence.integrationtest.provider;

import com.eclipsesource.connect.api.persistence.Storage;
import com.eclipsesource.connect.persistence.integrationtest.StorageProvider;
import com.eclipsesource.connect.test.util.persistence.InMemoryStorage;


public class InMemoryStorageProvider implements StorageProvider {

  @Override
  public Storage getCleanStorage() {
    return new InMemoryStorage();
  }

}
