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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;


public class UserTest {

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullName() {
    new User( null, "foo@bar.com" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyName() {
    new User( "", "foo@bar.com" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceName() {
    new User( " ", "foo@bar.com" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullEmail() {
    new User( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyEmail() {
    new User( "foo", "" );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyWhitespaceEmail() {
    new User( "foo", " " );
  }

  @Test
  public void testHasName() {
    User user = new User( "foo", "foo@bar.com" );

    String name = user.getName();

    assertThat( name ).isEqualTo( "foo" );
  }

  @Test
  public void testHasEmail() {
    User user = new User( "foo", "foo@bar.com" );

    String email = user.getEmail();

    assertThat( email ).isEqualTo( "foo@bar.com" );
  }

  @Test
  public void testHasId() {
    User user = new User( "foo", "foo@bar.com" );

    Id id = user.getId();

    assertThat( id ).isNotNull();
  }

  @Test
  public void testHasCreatedAt() {
    User user = new User( "foo", "foo@bar.com" );

    Date createdAt = user.getCreatedAt();

    assertThat( createdAt ).isNotNull();
  }

  @Test
  public void testHasUpdatedAt() {
    User user = new User( "foo", "foo@bar.com" );

    Date updatedAt = user.getUpdatedAt();

    assertThat( updatedAt ).isNotNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddExternalDataWithNullKey() {
    User user = new User( "foo", "foo@bar.com" );
    TestUserData data = new TestUserData( "foo" );

    user.setData( null, data );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddExternalDataWithEmptyKey() {
    User user = new User( "foo", "foo@bar.com" );
    TestUserData data = new TestUserData( "foo" );

    user.setData( "", data );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddExternalDataWithEmptyWhiteSpaceKey() {
    User user = new User( "foo", "foo@bar.com" );
    TestUserData data = new TestUserData( "foo" );

    user.setData( " ", data );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToAddExternalDataWithNullData() {
    User user = new User( "foo", "foo@bar.com" );

    user.setData( "foo", null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToGetExternalDataWithNullKey() {
    User user = new User( "foo", "foo@bar.com" );

    user.getData( null, Object.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToGetExternalDataWithEmptyKey() {
    User user = new User( "foo", "foo@bar.com" );

    user.getData( "", Object.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToGetExternalDataWithEmptyWhitespaceKey() {
    User user = new User( "foo", "foo@bar.com" );

    user.getData( " ", Object.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToGetExternalDataWithNullType() {
    User user = new User( "foo", "foo@bar.com" );

    user.getData( "foo", null );
  }

  @Test
  public void testHasUserData() {
    User user = new User( "foo", "foo@bar.com" );
    user.setData( "foo", new TestUserData( "foo" ) );

    TestUserData actualData = user.getData( "foo", TestUserData.class );

    assertThat( actualData.getName() ).isEqualTo( "foo" );
  }

  @Test
  public void testAddRolesUpdatesUpdatedAt() {
    User user = new User( "foo", "foo@bar.com" );
    Date before = user.getUpdatedAt();

    user.addRoles( "user" );

    Date after = user.getUpdatedAt();
    assertThat( before ).isNotSameAs( after );
  }

  @Test
  public void testCanAddRoles() {
    User user = new User( "foo", "foo@bar.com" );
    user.addRoles( "user" );

    List<String> roles = user.getRoles();

    assertThat( roles ).hasSize( 1 ).contains( "user" );
  }

  @Test
  public void testCanRemoveRoles() {
    User user = new User( "foo", "foo@bar.com" );
    user.addRoles( "user" );
    user.addRoles( "bar" );

    user.removeRoles( "bar" );

    List<String> roles = user.getRoles();
    assertThat( roles ).hasSize( 1 ).contains( "user" );
  }

  @Test
  public void testRemoveRolesUpdatesUpdatedAt() {
    User user = new User( "foo", "foo@bar.com" );
    user.addRoles( "user" );
    Date before = user.getUpdatedAt();
    user.addRoles( "bar" );

    user.removeRoles( "bar" );

    Date after = user.getUpdatedAt();
    assertThat( before ).isNotSameAs( after );
  }

  @Test
  public void testDoesNotAddDuplicateRoles() {
    User user = new User( "foo", "foo@bar.com" );

    user.addRoles( "user" );
    user.addRoles( "user" );

    List<String> roles = user.getRoles();
    assertThat( roles ).hasSize( 1 ).contains( "user" );
  }

  @Test
  public void testRolesAreEmptyByDefault() {
    User user = new User( "foo", "foo@bar.com" );

    List<String> roles = user.getRoles();

    assertThat( roles ).isEmpty();
  }

  @Test( expected = UnsupportedOperationException.class )
  public void testRolesAreImmutable() {
    User user = new User( "foo", "foo@bar.com" );

    List<String> roles = user.getRoles();

    roles.add( "user" );
  }

  @Test
  public void testCanSetUserInfo() {
    UserInfo userInfo = new UserInfo();
    User user = new User( "foo", "bar" );

    user.setInfo( userInfo );
    UserInfo actualUserInfo = user.getInfo();

    assertThat( userInfo ).isSameAs( actualUserInfo );
  }

  @Test
  public void testSetUserInfoUpdatesUpdatedAt() {
    UserInfo userInfo = new UserInfo();
    User user = new User( "foo", "bar" );
    Date before = user.getUpdatedAt();

    user.setInfo( userInfo );

    Date after = user.getUpdatedAt();
    assertThat( before ).isNotSameAs( after );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsToSetNullInfo() {
    User user = new User( "foo", "bar" );

    user.setInfo( null );
  }

  @Test
  public void testHasAlwaysAUserInfo() {
    User user = new User( "foo", "bar" );

    UserInfo info = user.getInfo();

    assertThat( info ).isNotNull();
  }

  @Test
  public void testDefaultIsInNoOrganization() {
    User user = new User( "foo", "bar" );

    List<String> roles = user.getRoles();

    assertThat( roles ).doesNotContain( "org-member" );
  }

  @Test
  public void testHasNoValidToByDefault() {
    User user = new User( "foo", "bar" );

    Optional<Date> validTo = user.getValidTo();

    assertThat( validTo.isPresent() ).isFalse();
  }

  @Test
  public void testSetsValidTo() {
    User user = new User( "foo", "bar" );
    Date validTo = new Date();

    user.setValidTo( validTo );

    Optional<Date> actualValidTo = user.getValidTo();
    assertThat( actualValidTo.isPresent() ).isTrue();
    assertThat( actualValidTo.get() ).isEqualTo( validTo );
  }

  @Test
  public void testCanInvalidate() {
    User user = new User( "foo", "bar" );
    user.setValidTo( new Date() );

    user.setValidTo( null );

    assertThat( user.getValidTo().isPresent() ).isFalse();
  }

  @Test
  public void testSetValidToUpdatesUpdatedAt() {
    User user = new User( "foo", "bar" );
    Date before = user.getUpdatedAt();

    user.setValidTo( new Date() );
    Date after = user.getUpdatedAt();

    assertThat( before ).isNotSameAs( after );
  }

  private class TestUserData {

    private String name;

    public TestUserData( String name ) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

  }
}
