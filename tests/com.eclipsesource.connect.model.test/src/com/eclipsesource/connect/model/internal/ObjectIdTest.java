package com.eclipsesource.connect.model.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.eclipsesource.connect.model.internal.ObjectId;
import com.google.common.collect.Lists;


public class ObjectIdTest {

  /*
   * @Test(groups = {"basic"}) public void testTSM(){ ObjectId a = new ObjectId(
   * 2667563522304714314L , -1912742877 ); assertEquals( "4a26c3e2e316052523dcfd8d" ,
   * a.toStringMongod() ); assertEquals( "250516e3e2c3264a8dfddc23" , a.toStringBabble() );
   * assertEquals( "4a26c3e2e316052523dcfd8d" , a.toString() ); }
   */
  @Test
  public void testRT1() {
    ObjectId a = new ObjectId();
    assertEquals( a.toStringBabble(), ( new ObjectId( a.toStringBabble(), true ) ).toStringBabble() );
    assertEquals( a.toStringMongod(),
                  ( new ObjectId( a.toStringMongod(), false ) ).toStringMongod() );
    assertEquals( a.toStringMongod(), ( new ObjectId( a.toStringMongod() ) ).toStringMongod() );
    assertEquals( a.toString(), ( new ObjectId( a.toString(), false ) ).toString() );
  }

  @Test
  public void testBabbleToMongo() {
    ObjectId a = new ObjectId();
    assertEquals( a.toStringMongod(), ObjectId.babbleToMongod( a.toStringBabble() ) );
  }

  @Test
  public void testBytes() {
    ObjectId a = new ObjectId();
    assertEquals( a, new ObjectId( a.toByteArray() ) );
    byte b[] = new byte[ 12 ];
    java.util.Random r = new java.util.Random( 17 );
    for( int i = 0; i < b.length; i++ )
      b[ i ] = ( byte )( r.nextInt() );
    a = new ObjectId( b );
    assertEquals( a, new ObjectId( a.toByteArray() ) );
    assertEquals( "41d91c58988b09375cc1fe9f", a.toString() );
  }

  @Test
  public void testTime() {
    long a = System.currentTimeMillis();
    long b = ( new ObjectId() ).getTime();
    assertTrue( Math.abs( b - a ) < 3000 );
  }

  @Test
  public void testBasics() {
    ObjectId a = new ObjectId();
    ObjectId b = new ObjectId();
    assertFalse( a.equals( b ) );
  }

  @Test
  public void testDateCons() {
    java.util.Date d = new java.util.Date();
    ObjectId a = new ObjectId( d );
    assertEquals( d.getTime() / 1000, a.getTime() / 1000 );
  }

  @Test
  public void testFlip() {
    _testFlip( 1, 16777216 );
    _testFlip( 1231231, 2143883776 );
    _testFlip( 0x12345678, 0x78563412 );
    Random r = new Random( 12312312 );
    for( int i = 0; i < 1000; i++ ) {
      _testFlip( r.nextInt(), 0 );
    }
  }

  void _testFlip( int x, int Y ) {
    int y = ObjectId._flip( x );
    int z = ObjectId._flip( y );
    assertEquals( x, z );
    if( Y > 0 )
      assertEquals( Y, y );
  }

  @Test
  public void testUniqueId() {
    List<String> ids = Lists.newArrayList();
    int times = 500000;
    for( int i = 0; i < times; i++ ) {
      ObjectId objectId = new ObjectId();
      ids.add( objectId.toString() );
    }
    assertEquals( times, ids.size() );
  }

  /**
   * Test that within same second, increment value correctly generates ordered ids
   */
  @Test
  public void testInc() {
    ObjectId prev = null;
    Date now = new Date();
    // need to loop more than value of byte, to check that endianness is correct
    for( int i = 0; i < 1000; ++i ) {
      ObjectId id = new ObjectId( now );
      assertEquals( id.getTime() / 1000, now.getTime() / 1000 );
      if( prev != null ) {
        assertTrue( prev.compareTo( id ) < 0 );
      }
      prev = id;
    }
  }
}

