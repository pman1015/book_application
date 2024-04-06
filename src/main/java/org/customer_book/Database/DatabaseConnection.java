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
import java.util.Scanner;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.customer_book.App;
import org.customer_book.Launcher;
import org.customer_book.Database.CustomerCollection.CustomerCollection;
import org.customer_book.Database.EquipmentCollection.EquipmentCollection;
import org.customer_book.Database.InventoryCollection.InventoryCollection;
import org.customer_book.Database.InvoiceCollection.InvoiceCollection;
import org.customer_book.Database.JobsCollection.JobCollection;
import org.customer_book.Database.MachinesCollection.MachineCollection;
import org.customer_book.Database.ReportsCollection.ReportsCollection;

public class DatabaseConnection {

  private final String uri;
  private final MongoDatabase database;
  public static CustomerCollection customerCollection;
  public static JobCollection jobCollection;
  public static MachineCollection machineCollection;
  public static EquipmentCollection equipmentCollection;
  public static InventoryCollection inventoryCollection;
  public static ReportsCollection reportsCollection;
  public static InvoiceCollection invoiceCollection;

  /**
   * Constructor for DatabaseConnection
   * This constructor takes in no arguments
   * @throws IOException
   */
  public DatabaseConnection() throws IOException {
    System.out.println(App.class.getResource("").toString());
    InputStream is = Launcher.class.getResourceAsStream("Data/ConnectionURI.txt");
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

    //Initalise collections
    database = mongoClient.getDatabase("CustomerBook");

    customerCollection = new CustomerCollection(database);
    jobCollection = new JobCollection(database);
    equipmentCollection = new EquipmentCollection(database);
    machineCollection = new MachineCollection(database);
    inventoryCollection = new InventoryCollection(database);
    reportsCollection = new ReportsCollection(database);
    invoiceCollection = new InvoiceCollection(database);
    
  }

  public MongoCollection<?> getCollection(
    String collectionName,
    Class<?> className
  ) {
    return database.getCollection(collectionName, className);
  }
}
