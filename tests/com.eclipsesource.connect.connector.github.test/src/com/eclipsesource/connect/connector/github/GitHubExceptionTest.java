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
package com.eclipsesource.connect.connector.github;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class GitHubExceptionTest {

  @Test
  public void testHasStatusCode() {
    GitHubException exception = new GitHubException( 200 );

    int statusCode = exception.getStatusCode();

    assertThat( statusCode ).isEqualTo( 200 );
  }
}
