package com.eclipsesource.connect.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.ErrorTemplate;

@Path( "/error" )
public class ErrorUI {

  @GET
  @Produces( MediaType.TEXT_HTML )
  @ErrorTemplate( name = "/error.mustache" )
  public String get() {
    throw new IllegalStateException( "Expected Exception" );
  }
}
