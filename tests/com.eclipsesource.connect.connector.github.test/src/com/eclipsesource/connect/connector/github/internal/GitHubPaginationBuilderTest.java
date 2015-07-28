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
package com.eclipsesource.connect.connector.github.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.ws.rs.core.Link;

import org.junit.Test;

import com.eclipsesource.connect.api.html.Pagination;
import com.google.common.collect.Lists;


public class GitHubPaginationBuilderTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullLinks() {
    new GitHubPaginationBuilder( null );
  }

  @Test
  public void testBuildsPagination() {
    List<Link> links = Lists.newArrayList( Link.valueOf( "<https://api.github.com/user/repos?page=4>; rel=\"next\"" ),
                                           Link.valueOf( "<https://api.github.com/user/repos?page=2>; rel=\"prev\"" ),
                                           Link.valueOf( "<https://api.github.com/user/repos?page=6>; rel=\"last\"" ));
    GitHubPaginationBuilder builder = new GitHubPaginationBuilder( links );

    Pagination pagination = builder.build();

    assertThat( pagination.getNextPages() ).hasSize( 3 ).contains( 4, 5, 6 );
    assertThat( pagination.getPreviousPages() ).hasSize( 2 ).contains( 1, 2 );
    assertThat( pagination.getCurrentPage() ).isEqualTo( 3 );
  }
}
