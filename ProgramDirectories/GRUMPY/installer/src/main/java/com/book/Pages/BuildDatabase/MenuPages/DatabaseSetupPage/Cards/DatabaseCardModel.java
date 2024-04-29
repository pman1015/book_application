package com.book.Pages.BuildDatabase.MenuPages.DatabaseSetupPage.Cards;

import com.book.App;
import com.mongodb.client.MongoDatabase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;

@Getter
@Setter
public class DatabaseCardModel {
    //-----------View Properties-----------//
    private StringProperty databaseName = new SimpleStringProperty("");
    private ObservableList<Parent> collectionList = FXCollections.observableArrayList();

    //-----------Model Properties-----------//
    private MongoDatabase database;

    public void setDatabase(MongoDatabase database) {
        collectionList.clear();
        this.database = database;
        databaseName.set(database.getName());
        database.listCollectionNames().forEach(collection -> {
            FXMLLoader loader = App.getLoader("BuildDatabaseConnection/MenuPages", "CollectionCard");
            try {
                Parent card = loader.load();
                ((CollectionCardController) loader.getController()).setCollection(collection);
                collectionList.add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
}
