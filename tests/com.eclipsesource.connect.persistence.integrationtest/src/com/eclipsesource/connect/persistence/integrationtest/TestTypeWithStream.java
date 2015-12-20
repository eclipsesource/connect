package com.eclipsesource.connect.persistence.integrationtest;

import java.io.InputStream;

import com.eclipsesource.connect.model.Id;


public class TestTypeWithStream {

  private Id id;
  private InputStream stream;

  public TestTypeWithStream( InputStream stream ) {
    this.id = new Id();
    this.stream = stream;
  }

  public Id getId() {
    return id;
  }

  public InputStream getStream() {
    return stream;
  }

}
