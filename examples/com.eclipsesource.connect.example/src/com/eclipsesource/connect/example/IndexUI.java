package com.eclipsesource.connect.example;

import static javax.ws.rs.core.Response.seeOther;
import static javax.ws.rs.core.UriBuilder.fromPath;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.eclipsesource.connect.example.mvc.MvcUI;

@Path( "/" )
public class IndexUI {

  @GET
  public Response get() {
    return seeOther( fromPath( MvcUI.class.getAnnotation( Path.class ).value() ).build() ).build();
  }

}
