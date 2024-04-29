package com.book.Pages.BuildDatabase.MenuPages.DatabaseSetupPage.Cards;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CollectionCardController {


    @FXML
    private Label collectionName;

    @FXML
    void initialize() {
        

    }

	public void setCollection(String collection) {
        collectionName.setText(collection);
	}
}
