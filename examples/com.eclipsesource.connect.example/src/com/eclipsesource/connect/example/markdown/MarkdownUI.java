package com.eclipsesource.connect.example.markdown;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;

import com.eclipsesource.connect.api.markdown.MarkdownParser;

@Path( "/markdown" )
public class MarkdownUI {

  private MarkdownParser parser;

  @GET
  @Produces( MediaType.TEXT_HTML )
  @Template( name = "/markdown.mustache" )
  @ErrorTemplate( name = "/error.mustache" )
  public String get() {
    return "Hello";
  }

  void setMarkdownParser( MarkdownParser parser ) {
    this.parser = parser;
  }

  void unsetMarkdownParser( MarkdownParser parser ) {
    this.parser = null;
  }

}
