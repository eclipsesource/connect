package com.eclipsesource.connect.example.template;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;


@Path( "/components" )
public class TemplateComponentsUI {

  @GET
  @Produces( MediaType.TEXT_HTML )
  @Template( name = "/components.mustache" )
  @ErrorTemplate( name = "/error.mustache" )
  public String getComponents() {
    return "Hello";
  }

}
