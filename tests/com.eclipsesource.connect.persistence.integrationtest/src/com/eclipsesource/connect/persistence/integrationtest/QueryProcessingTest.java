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
package com.eclipsesource.connect.persistence.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.eclipsesource.connect.api.persistence.Query;
import com.eclipsesource.connect.api.persistence.Query.SortDirection;
import com.google.common.collect.Lists;


public class QueryProcessingTest extends PersistenceTest {

  public QueryProcessingTest( StorageProvider storageProvider ) {
    super( storageProvider );
  }

  @Test
  public void testFindsObjectByField() {
    TestType expected = new TestType( "foo2" );
    getStorage().store( "foo", new TestType( "foo" ) );
    getStorage().store( "foo", expected );

    TestType actual = getStorage().find( new Query<>( "foo", TestType.class ).where( "name", "foo2" ) );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void testFindsObjectByParentField() {
    SubType expected = new SubType( "foo2" );
    getStorage().store( "foo", new SubType( "foo" ) );
    getStorage().store( "foo", expected );

    SubType actual = getStorage().find( new Query<>( "foo", SubType.class ).where( "name", "foo2" ) );

    assertThat( actual.getName() ).isEqualTo( expected.getName() );
  }

  @Test
  public void testFindsObjectByFieldWithPattern() {
    TestType expected = new TestType( "foo2" );
    getStorage().store( "foo", new TestType( "bar" ) );
    getStorage().store( "foo", expected );

    TestType actual = getStorage().find( new Query<>( "foo", TestType.class ).where( "name", Pattern.compile( ".*?foo2.*?" ) ) );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void testLimitsResults() {
    for( int i = 0; i < 10; i++ ) {
      getStorage().store( "foo", new TestType( "foo" + i ) );
    }

    List<TestType> all = getStorage().findAll( new Query<>( "foo", TestType.class ).limit( 5 ) );

    assertThat( all ).hasSize( 5 );
    for( int i = 0; i < 5; i++ ) {
      assertThat( all.get( i ).getName() ).isEqualTo( "foo" + i );
    }
  }

  @Test
  public void testLimitsResultsNotWithBiggerLimitThanResults() {
    for( int i = 0; i < 3; i++ ) {
      getStorage().store( "foo", new TestType( "foo" + i ) );
    }

    List<TestType> all = getStorage().findAll( new Query<>( "foo", TestType.class ).limit( 5 ) );

    assertThat( all ).hasSize( 3 );
    for( int i = 0; i < 3; i++ ) {
      assertThat( all.get( i ).getName() ).isEqualTo( "foo" + i );
    }
  }

  @Test
  public void testSkipsResults() {
    for( int i = 0; i < 10; i++ ) {
      getStorage().store( "foo", new TestType( "foo" + i ) );
    }

    List<TestType> all = getStorage().findAll( new Query<>( "foo", TestType.class ).skip( 5 ) );

    assertThat( all ).hasSize( 5 );
    for( int i = 0; i < 5; i++ ) {
      assertThat( all.get( i ).getName() ).isEqualTo( "foo" + ( i + 5 ) );
    }
  }

  @Test
  public void testSkipsResultsAndRespectsSortingDESC() {
    for( int i = 0; i < 10; i++ ) {
      getStorage().store( "foo", new TestType( "foo" + i ) );
    }

    List<TestType> all = getStorage().findAll( new Query<>( "foo", TestType.class )
                                                 .skip( 5 )
                                                 .sortBy( "name", SortDirection.DESC ) );

    assertThat( all ).hasSize( 5 );
    List<TestType> reverse = Lists.reverse( all );
    for( int i = 0; i < 5; i++ ) {
      assertThat( reverse.get( i ).getName() ).isEqualTo( "foo" + i );
    }
  }

  @Test
  public void testSkipsResultsAndRespectsSortingASC() {
    for( int i = 0; i < 10; i++ ) {
      getStorage().store( "foo", new TestType( "foo" + i ) );
    }

    List<TestType> all = getStorage().findAll( new Query<>( "foo", TestType.class )
                                                 .skip( 5 )
                                                 .sortBy( "name", SortDirection.ASC ) );

    assertThat( all ).hasSize( 5 );
    for( int i = 0; i < 5; i++ ) {
      assertThat( all.get( i ).getName() ).isEqualTo( "foo" + ( i + 5 ) );
    }
  }

  @Test
  public void testSkipsMoreAsPresentResults() {
    for( int i = 0; i < 10; i++ ) {
      getStorage().store( "foo", new TestType( "foo" + i ) );
    }

    List<TestType> all = getStorage().findAll( new Query<>( "foo", TestType.class ).skip( 50 ) );

    assertThat( all ).isEmpty();
  }

  @Test
  public void testSortsResultASC() {
    List<TestType> types = new ArrayList<>();
    types.add( new TestType( "foo1" ) );
    types.add( new TestType( "foo2" ) );
    types.add( new TestType( "foo3" ) );
    Collections.shuffle( types );
    for( TestType testType : types ) {
      getStorage().store( "foo", testType );
    }

    List<TestType> all = getStorage().findAll( new Query<>( "foo", TestType.class ).sortBy( "name", SortDirection.ASC ) );

    for( int i = 0; i < 3; i++ ) {
      assertThat( all.get( i ).getName() ).isEqualTo( "foo" + ( i + 1 ) );
    }
  }

  @Test
  public void testSortsResultDESC() {
    List<TestType> types = new ArrayList<>();
    types.add( new TestType( "foo1" ) );
    types.add( new TestType( "foo2" ) );
    types.add( new TestType( "foo3" ) );
    Collections.shuffle( types );
    for( TestType testType : types ) {
      getStorage().store( "foo", testType );
    }

    List<TestType> all = getStorage().findAll( new Query<>( "foo", TestType.class ).sortBy( "name", SortDirection.DESC ) );

    List<TestType> reverse = Lists.reverse( all );
    for( int i = 0; i < 3; i++ ) {
      assertThat( reverse.get( i ).getName() ).isEqualTo( "foo" + ( i + 1 ) );
    }
  }

  @Test
  public void testLimitsAndSortsResult() {
    List<TestType> types = new ArrayList<>();
    types.add( new TestType( "foo1" ) );
    types.add( new TestType( "foo2" ) );
    types.add( new TestType( "foo3" ) );
    Collections.shuffle( types );
    for( TestType testType : types ) {
      getStorage().store( "foo", testType );
    }

    List<TestType> all = getStorage().findAll( new Query<>( "foo", TestType.class )
                                                 .sortBy( "name", SortDirection.ASC )
                                                 .limit( 2 ) );

    for( int i = 0; i < 2; i++ ) {
      assertThat( all.get( i ).getName() ).isEqualTo( "foo" + ( i + 1 ) );
    }
  }

  @Test
  public void testCanHandleQueryWithDotSyntaxOnSubFields() {
    TestTypeWithId testType = new TestTypeWithId( "foo" );
    TypeHolder typeHolder = new TypeHolder( testType );
    getStorage().store( "foo", typeHolder );

    TypeHolder holder = getStorage().find( new Query<>( "foo", TypeHolder.class )
                                             .where( "type._id", testType.getId().toString() ) );

    assertThat( holder.type.getId().toString() ).isEqualTo( testType.getId().toString() );
  }

  @Test
  public void testCanHandleQueryWithDotSyntaxOnArrays() {
    TestTypeWithId testType = new TestTypeWithId( "foo" );
    ArrayTypeHolder typeHolder = new ArrayTypeHolder( testType, new TestTypeWithId( "bar" ) );
    getStorage().store( "foo", typeHolder );

    ArrayTypeHolder holder = getStorage().find( new Query<>( "foo", ArrayTypeHolder.class )
                                                  .where( "types._id", testType.getId().toString() ) );

    assertThat( holder.types[ 0 ].getId().toString() ).isEqualTo( testType.getId().toString() );
  }

  @Test
  public void testCanHandleQueryWithDotSyntaxOnArraysWithParentFields() {
    SubType testType = new SubType( "foo" );
    ArrayTypeHolder typeHolder = new ArrayTypeHolder( testType, new SubType( "bar" ) );
    getStorage().store( "foo", typeHolder );

    ArrayTypeHolder holder = getStorage().find( new Query<>( "foo", ArrayTypeHolder.class )
                                                  .where( "types._id", testType.getId().toString() ) );

    assertThat( holder.types[ 0 ].getId().toString() ).isEqualTo( testType.getId().toString() );
  }

  @Test
  public void testCanHandleQueryWithDotSyntaxOnArraysWithPattern() {
    TestTypeWithId testType = new TestTypeWithId( "foo" );
    ArrayTypeHolder typeHolder = new ArrayTypeHolder( testType, new TestTypeWithId( "bar" ) );
    getStorage().store( "foo", typeHolder );

    ArrayTypeHolder holder = getStorage().find( new Query<>( "foo", ArrayTypeHolder.class )
                                                  .where( "types.name", Pattern.compile( ".*?bar.*?" ) ) );

    assertThat( holder.types[ 0 ].getId().toString() ).isEqualTo( testType.getId().toString() );
  }

  @Test
  public void testCanHandleQueryWithDotSyntaxOnCollections() {
    TestTypeWithId testType = new TestTypeWithId( "foo" );
    CollectionTypeHolder typeHolder = new CollectionTypeHolder( testType, new TestTypeWithId( "bar" ) );
    getStorage().store( "foo", typeHolder );

    CollectionTypeHolder holder = getStorage().find( new Query<>( "foo", CollectionTypeHolder.class )
                                                       .where( "types._id", testType.getId().toString() ) );

    assertThat( holder.types.get( 0 ).getId().toString() ).isEqualTo( testType.getId().toString() );
  }

  @Test
  public void testCanHandleQueryWithDotSyntaxOnCollectionsWithParentFields() {
    SubType testType = new SubType( "foo" );
    CollectionTypeHolder typeHolder = new CollectionTypeHolder( testType, new SubType( "bar" ) );
    getStorage().store( "foo", typeHolder );

    CollectionTypeHolder holder = getStorage().find( new Query<>( "foo", CollectionTypeHolder.class )
                                                       .where( "types.name", Pattern.compile( ".*?bar.*?" ) ) );

    assertThat( holder.types.get( 0 ).getId().toString() ).isEqualTo( testType.getId().toString() );
  }

  @Test
  public void testCanHandleQueryWithDotSyntaxOnCollectionsWithPattern() {
    TestTypeWithId testType = new TestTypeWithId( "foo" );
    CollectionTypeHolder typeHolder = new CollectionTypeHolder( testType, new TestTypeWithId( "bar" ) );
    getStorage().store( "foo", typeHolder );

    CollectionTypeHolder holder = getStorage().find( new Query<>( "foo", CollectionTypeHolder.class )
                                                       .where( "types.name", Pattern.compile( ".*?bar.*?" ) ) );

    assertThat( holder.types.get( 0 ).getId().toString() ).isEqualTo( testType.getId().toString() );
  }

  private static class TypeHolder {

    private TestTypeWithId type;

    public TypeHolder( TestTypeWithId type ) {
      this.type = type;
    }

  }

  private static class ArrayTypeHolder {

    private TestTypeWithId[] types;

    public ArrayTypeHolder( TestTypeWithId... types ) {
      this.types = types;
    }

  }

  private static class CollectionTypeHolder {

    private List<TestTypeWithId> types;

    public CollectionTypeHolder( TestTypeWithId... types ) {
      this.types = Arrays.asList( types );
    }

  }

  private static class SubType extends TestTypeWithId {

    public SubType( String name ) {
      super( name );
    }

  }

}
