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
package com.eclipsesource.connect.persistence.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.eclipsesource.connect.api.persistence.Storage;
import com.eclipsesource.connect.persistence.integrationtest.provider.Providers;


@RunWith( Parameterized.class )
public class PersistenceTest {

  private StorageProvider storageProvider;
  private Storage storage;

  public PersistenceTest( StorageProvider storageProvider ) {
    this.storageProvider = storageProvider;
  }

  @Before
  public void setUp() {
    storage = storageProvider.getCleanStorage();
  }

  @Test
  public void testStorageCanBeCreated() {
    assertThat( storage ).isNotNull();
  }

  public Storage getStorage() {
    return storage;
  }

  @Parameters
  public static Iterable<StorageProvider[]> data() {
    return Providers.getProviders().stream()
                                   .map( provider -> new StorageProvider[] { provider } )
                                   .collect( Collectors.toList() );
  }
}
