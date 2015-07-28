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


public class GHCommitFile {

  private String filename;
  private int additions;
  private int deletions;
  private int changes;
  private String status;
  private String raw_url;
  private String blob_url;
  private String patch;

  public String getFilename() {
    return filename;
  }

  public int getAdditions() {
    return additions;
  }

  public int getDeletions() {
    return deletions;
  }

  public int getChanges() {
    return changes;
  }

  public String getStatus() {
    return status;
  }

  public String getRawUrl() {
    return raw_url;
  }

  public String getBlobUrl() {
    return blob_url;
  }

  public String getPatch() {
    return patch;
  }

}
