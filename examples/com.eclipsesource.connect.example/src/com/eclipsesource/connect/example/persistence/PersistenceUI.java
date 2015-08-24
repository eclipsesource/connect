package com.eclipsesource.connect.example.persistence;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;

import com.eclipsesource.connect.api.persistence.Storage;

@Path( "/persistence" )
public class PersistenceUI {

  private Storage storage;

  @GET
  @Produces( MediaType.TEXT_HTML )
  @Template( name = "/persistence.mustache" )
  @ErrorTemplate( name = "/error.mustache" )
  public String get() {
    return "Hello";
  }

  void setStorage( Storage storage ) {
    this.storage = storage;
  }

  void unsetStorage( Storage storage ) {
    this.storage = null;
  }

}
