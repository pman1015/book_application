package com.book.Pages.BuildDatabase.MenuPages.DatabaseSetupPage;

import java.io.IOException;

import com.book.App;
import com.book.Pages.BuildDatabase.MenuPages.PageModelAbstract;
import com.book.Pages.BuildDatabase.MenuPages.DatabaseSetupPage.Cards.DatabaseCardController;
import com.mongodb.client.MongoDatabase;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter
public class DatabaseSetupModel extends PageModelAbstract {

  // -------------------- View Properties --------------------
  @SuppressWarnings("unchecked")
  private ObservableList<Parent> databaseCards = FXCollections.observableArrayList();
  private StringProperty errorMessage = new SimpleStringProperty("");

  // -------------------- Model Properties --------------------
  public String corrections;
  private final int position = 2;
  private boolean hasCustomerBook = false;
  private String[] collections = { "Customers", "Equipment", "Inventory", "Invoices", "Jobs", "Machines", "Reports" };

  @Override
  public void onPageChange() {
    if (page.get() == position) {
      currentConnection = getConnectionProperty();
      System.out.println("Database Setup Page");
      if (currentConnection == null || currentConnection.getClient() == null) {
        System.out.println("Connection is null");
        page.set(position - 1);
      } else {
        System.out.println("Connection is not null");
        databaseCards.clear();
        Thread loadDatabases = new Thread(() -> {
          loadDatabases();
        });
        loadDatabases.start();
      }
    }
  }

  private void loadDatabases() {

    currentConnection.getClient().listDatabases().forEach(database -> {
      Platform.runLater(() -> {
        
        FXMLLoader loader = App.getLoader("BuildDatabaseConnection/MenuPages", "DatabaseCard");
        try {
          Parent card = loader.load();
          ((DatabaseCardController) loader.getController()).setDatabase(
              currentConnection.lookupDatabase(database.getString("name")));
          databaseCards.add(card);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    });
    hasCustomerBook = currentConnection.getClient().listDatabaseNames().into(new ArrayList<String>()).contains("CustomerBook");
    if (!hasCustomerBook) {
      Platform.runLater(() -> {
        errorMessage.set("CustomerBook database not found. Please create a new database.");
      });
    } else {
      MongoDatabase book = currentConnection.lookupDatabase("CustomerBook");
      ArrayList<String> missingCollections = new ArrayList<String>();
      ArrayList<String> currentCollections = book.listCollectionNames().into(new ArrayList<String>());
      for (int i = 0; i < collections.length; i++) {
        if(!currentCollections.contains(collections[i])) {
          missingCollections.add(collections[i]);
        }
      }
      if (missingCollections.size() > 0) {
        Platform.runLater(() -> {
          errorMessage.set("The following collections are missing: " + missingCollections.toString());
        });
        collections = missingCollections.toArray(new String[missingCollections.size()]);
      }else{
        Platform.runLater(() -> {
          errorMessage.set("Database Configured Correctly.");
        });
      }
    }

  }

  public void FixError() {
    if (!hasCustomerBook) {
      currentConnection.getClient().getDatabase("CustomerBook").createCollection("Customers");
    } else {
      MongoDatabase book = currentConnection.lookupDatabase("CustomerBook");
      for (int i = 0; i < collections.length; i++) {
        book.createCollection(collections[i]);
      }

    }
    databaseCards.clear();
    loadDatabases();
  }

}
