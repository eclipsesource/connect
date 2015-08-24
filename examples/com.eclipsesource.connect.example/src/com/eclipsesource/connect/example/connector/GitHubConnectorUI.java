package com.eclipsesource.connect.example.connector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;

@Path( "/connectors/github" )
public class GitHubConnectorUI {

  @GET
  @Produces( MediaType.TEXT_HTML )
  @Template( name = "/github.mustache" )
  @ErrorTemplate( name = "/error.mustache" )
  public String get() {
    return "Hello";
  }

}
