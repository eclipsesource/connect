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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;


public class UserInfoTest {

  private UserInfo userInfo;

  @Before
  public void setUp() {
    userInfo = new UserInfo();
  }

  @Test
  public void testSavesName() {
    userInfo.setName( "foo" );

    String name = userInfo.getName();

    assertThat( name ).isEqualTo( "foo" );
  }

  @Test
  public void testSavesCompany() {
    userInfo.setCompany( "foo" );

    String company = userInfo.getCompany();

    assertThat( company ).isEqualTo( "foo" );
  }

  @Test
  public void testSavesStreet() {
    userInfo.setStreet( "foo" );

    String street = userInfo.getStreet();

    assertThat( street ).isEqualTo( "foo" );
  }

  @Test
  public void testSavesZip() {
    userInfo.setZip( "foo" );

    String zip = userInfo.getZip();

    assertThat( zip ).isEqualTo( "foo" );
  }

  @Test
  public void testSavesPhone() {
    userInfo.setPhone( "foo" );

    String phone = userInfo.getPhone();

    assertThat( phone ).isEqualTo( "foo" );
  }

  @Test
  public void testSavesCountry() {
    userInfo.setCountry( "foo" );

    String country = userInfo.getCountry();

    assertThat( country ).isEqualTo( "foo" );
  }

  @Test
  public void testSavesVat() {
    userInfo.setVat( "foo" );

    String vat = userInfo.getVat();

    assertThat( vat ).isEqualTo( "foo" );
  }

}
