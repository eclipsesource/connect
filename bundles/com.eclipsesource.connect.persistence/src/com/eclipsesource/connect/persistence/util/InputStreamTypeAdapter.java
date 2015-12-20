package com.eclipsesource.connect.persistence.util;

import java.io.IOException;
import java.io.InputStream;

import org.bson.types.ObjectId;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;


public class InputStreamTypeAdapter extends TypeAdapter<InputStream> {

  private final GridFSBucket bucket;

  public InputStreamTypeAdapter( MongoDatabase db ) {
    bucket = GridFSBuckets.create( db );
  }

  @Override
  public void write( JsonWriter out, InputStream value ) throws IOException {
    ObjectId id = bucket.uploadFromStream( "", value );
    out.value( id.toString() );
  }

  @Override
  public InputStream read( JsonReader in ) throws IOException {
    String id = in.nextString();
    return bucket.openDownloadStream( new ObjectId( id ) );
  }

}
