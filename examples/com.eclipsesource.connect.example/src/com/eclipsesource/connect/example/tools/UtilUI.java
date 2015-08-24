package com.eclipsesource.connect.example.tools;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;

@Path( "/utils" )
public class UtilUI {

  @GET
  @Produces( MediaType.TEXT_HTML )
  @Template( name = "/utils.mustache" )
  @ErrorTemplate( name = "/error.mustache" )
  public String get() {
    return "Hello";
  }
}
