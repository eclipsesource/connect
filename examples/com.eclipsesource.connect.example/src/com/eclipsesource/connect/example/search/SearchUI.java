package com.eclipsesource.connect.example.search;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;

import com.eclipsesource.connect.api.search.Search;

@Path( "/search" )
public class SearchUI {

  private Search search;

  @GET
  @Produces( MediaType.TEXT_HTML )
  @Template( name = "/search.mustache" )
  @ErrorTemplate( name = "/error.mustache" )
  public String get() {
    return "Hello";
  }

  void setSearch( Search search ) {
    this.search = search;
  }

  void unsetSearch( Search search ) {
    this.search = null;
  }

}
