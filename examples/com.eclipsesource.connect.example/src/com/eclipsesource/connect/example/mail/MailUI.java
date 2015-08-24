package com.eclipsesource.connect.example.mail;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;

import com.eclipsesource.connect.api.mail.SendMail;

@Path( "/mail" )
public class MailUI {

  private SendMail sendMail;

  @GET
  @Produces( MediaType.TEXT_HTML )
  @Template( name = "/mail.mustache" )
  @ErrorTemplate( name = "/error.mustache" )
  public String get() {
    return "Hello";
  }

  void setSendMail( SendMail sendMail ) {
    this.sendMail = sendMail;
  }

  void unsetSendMail( SendMail sendMail ) {
    this.sendMail = null;
  }
}
