package com.book.Pages.BuildDatabase.MenuPages.DatabaseSetupPage.Cards;

import org.bson.Document;

import com.mongodb.client.MongoDatabase;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DatabaseCardController {

    @FXML
    private Label databaseName;

    @FXML
    private VBox collectionList;

    private DatabaseCardModel model;

    @FXML
    void initialize() {
        model = new DatabaseCardModel();
        databaseName.textProperty().bind(model.getDatabaseName());
        collectionList.getChildren().addAll(model.getCollectionList());

        // Add listener to collectionList
        model.getCollectionList().addListener(new ListChangeListener<Parent>() {
            @Override
            public void onChanged(Change<? extends Parent> c) {
                collectionList.getChildren().clear();
                collectionList.getChildren().addAll(model.getCollectionList());
            }
        });

    }

    public void setDatabase(MongoDatabase database) {
        model.setDatabase(database);
    }
}
