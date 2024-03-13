package org.customer_book.Database;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.customer_book.App;

public class DatabaseConnection {

  private final String uri;
  private final MongoDatabase database;

  /**
   * Constructor for DatabaseConnection
   * This constructor takes in no arguments
   * @throws IOException
   */
  public DatabaseConnection() throws IOException {
    uri =
      new String(
        App.class.getResource("Data/ConnectionURI.txt")
          .openStream()
          .readAllBytes()
          .toString()
      );
    ConnectionString connectionString = new ConnectionString(uri);
    CodecRegistry projectCodecRegistry = CodecRegistries.fromProviders(
      PojoCodecProvider.builder().automatic(true).build()
    );
    CodecRegistry codecRegistry = fromRegistries(
      MongoClientSettings.getDefaultCodecRegistry(),
      projectCodecRegistry
    );
    MongoClientSettings clientSettings = MongoClientSettings
      .builder()
      .applyConnectionString(connectionString)
      .codecRegistry(codecRegistry)
      .build();
    MongoClient mongoClient = MongoClients.create(clientSettings);
    database = mongoClient.getDatabase("CustomerBook");
  }

  public MongoCollection<?> getCollection(
    String collectionName,
    Class<?> className
  ) {
    return database.getCollection(collectionName, className);
  }
}
