/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.model;


public class UserInfo {

  private String name;
  private String company;
  private String street;
  private String city;
  private String zip;
  private String country;
  private String phone;
  private String vat;

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany( String company ) {
    this.company = company;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet( String street ) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity( String city ) {
    this.city = city;
  }

  public String getZip() {
    return zip;
  }

  public void setZip( String zip ) {
    this.zip = zip;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone( String phone ) {
    this.phone = phone;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry( String country ) {
    this.country = country;
  }

  public String getVat() {
    return vat;
  }

  public void setVat( String vat ) {
    this.vat = vat;
  }

}
