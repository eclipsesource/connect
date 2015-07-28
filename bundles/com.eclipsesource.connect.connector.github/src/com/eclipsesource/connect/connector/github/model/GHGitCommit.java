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
package com.eclipsesource.connect.connector.github.model;


public class GHGitCommit {

  private String url;
  private GHGitCommiter author;
  private GHGitCommiter committer;
  private String message;
  private GHCommit tree;
  private int comment_count;

  public String getUrl() {
    return url;
  }

  public GHGitCommiter getAuthor() {
    return author;
  }

  public GHGitCommiter getCommitter() {
    return committer;
  }

  public String getMessage() {
    return message;
  }

  public GHCommit getTree() {
    return tree;
  }

  public int getCommentCount() {
    return comment_count;
  }

}
