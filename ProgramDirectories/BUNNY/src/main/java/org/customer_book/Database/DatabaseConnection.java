package org.customer_book.Database;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.server.SocketSecurityException;
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

@Getter
public class DatabaseConnection {

  private String uri;
  private static MongoDatabase database;
  public static CustomerCollection customerCollection;
  public static JobCollection jobCollection;
  public static MachineCollection machineCollection;
  public static EquipmentCollection equipmentCollection;
  public static InventoryCollection inventoryCollection;
  public static ReportsCollection reportsCollection;
  public static InvoiceCollection invoiceCollection;
  private Object[] collections = {
      customerCollection,
      jobCollection,
      machineCollection,
      equipmentCollection,
      inventoryCollection,
      reportsCollection,
      invoiceCollection
  };
  public BooleanProperty databaseConnected = new SimpleBooleanProperty(true);

  /**
   * Constructor for DatabaseConnection
   * This constructor takes in no arguments
   * 
   * @throws IOException
   */
  public DatabaseConnection() {

    reConnect();
  }

  public void reConnect() {
    try {
      databaseConnected.set(connectToDatabase());
      if (isConnected()) {
        System.out.println("Database connection successful");
        App.removeSceneProperty("Error");
      } else {
        System.out.println("Error connecting to database");
        App.setSceneProperty("Error", "Error connecting to database");
      }

    } catch (Exception e) {
      System.out.println("Error connecting to database");
    }
  }

  /**
   * Connect to the database
   * - Runs thru the steps necessary to connect to the database
   * - and logs the progress in the console
   * 
   * @return
   */
  @SuppressWarnings("null")
  private boolean connectToDatabase() {
    System.out.println("Loading connection string....");
    try {
      loadConnectionString();
    } catch (Exception e) {
      System.out.println("Error loading connection string");
      return false;
    }
    System.out.println("Building connection to database....");
    buildDBConnection();
    if (database == null) {
      System.out.println("Error building connection to database");
      return false;
    }
    System.out.println("Connection to database successful \n Loading collections....");
    // Initialize collections
    initializeCollections();
    return true;
  }

  /**
   * Build the connection to the database
   * - This method builds the connection to the database from the connection
   * string and sets the database
   */
  private void buildDBConnection() {
    ConnectionString connectionString = new ConnectionString(uri);
    CodecRegistry projectCodecRegistry = CodecRegistries.fromProviders(
        PojoCodecProvider.builder().automatic(true).build());
    CodecRegistry codecRegistry = fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        projectCodecRegistry);
    MongoClientSettings clientSettings = MongoClientSettings
        .builder()
        .applyConnectionString(connectionString)
        .codecRegistry(codecRegistry)
        .build();
    MongoClient mongoClient = MongoClients.create(clientSettings);
    database = mongoClient.getDatabase("CustomerBook");
  }

  /**
   * Initialize the collections
   * This method initializes all the collections in the database
   */
  private void initializeCollections() {
    customerCollection = new CustomerCollection(database);
    jobCollection = new JobCollection(database);
    equipmentCollection = new EquipmentCollection(database);
    machineCollection = new MachineCollection(database);
    inventoryCollection = new InventoryCollection(database);
    reportsCollection = new ReportsCollection(database);
    invoiceCollection = new InvoiceCollection(database);
  }

  /**
   * Get the collection from the database
   * 
   * @param collectionName - Name of collection to load
   * @param className      - ClassName of the DAO for the collection
   * @return - returns the collection
   */
  public MongoCollection<?> getCollection(
      String collectionName,
      Class<?> className) {
    return database.getCollection(collectionName, className);
  }

  /**
   * Load the connection string from the config file
   * If the config file does not exist, load the connection string from the
   * ConnectionURI.txt file
   */
  public void loadConnectionString() {

    // Try to load the connection string from the config file
    System.out.println("Loading Connection from the config file");
    if (!App.homePageConfigUtil.getHomePageConfig().getDatabaseConnectionString().equals("")) {
      uri = App.homePageConfigUtil.getHomePageConfig().getDatabaseConnectionString();
      return;
    }
    System.out.println("Error Loading Connection from the config file defaulting to ConnectionURI.txt");
    // Load the connection string from the ConnectionURI.txt file
    InputStream is = Launcher.class.getResourceAsStream("Data/ConnectionURI.txt");
    Scanner s = new Scanner(is).useDelimiter("\\A");
    uri = s.hasNext() ? s.next() : "";
  }

  /**
   * 
   */
  public boolean isConnected() {
    return databaseConnected.get();
  }

}
