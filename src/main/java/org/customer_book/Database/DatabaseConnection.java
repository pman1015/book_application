package org.customer_book.Database;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerCollection;

public class DatabaseConnection {

  private final String uri;
  private final MongoDatabase database;
  public static CustomerCollection customerCollection;

  /**
   * Constructor for DatabaseConnection
   * This constructor takes in no arguments
   * @throws IOException
   */
  public DatabaseConnection() throws IOException {
    InputStream is = App.class.getResourceAsStream("Data/ConnectionURI.txt");
    Scanner s = new Scanner(is).useDelimiter("\\A");
    uri = s.hasNext() ? s.next() : "";
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

    //Initalise collections
    customerCollection = new CustomerCollection(database);
  }

  public MongoCollection<?> getCollection(
    String collectionName,
    Class<?> className
  ) {
    return database.getCollection(collectionName, className);
  }
}
