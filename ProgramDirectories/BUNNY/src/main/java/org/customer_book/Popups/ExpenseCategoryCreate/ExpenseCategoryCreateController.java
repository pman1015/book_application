package org.customer_book.Popups.ExpenseCategoryCreate;

import org.customer_book.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ExpenseCategoryCreateController {


    @FXML
    private TextField CategoryNameField;

    @FXML
    private Label CategoryNameError;

    @FXML
    void close(ActionEvent event) {
        App.removePopup();
    }

    @FXML
    void addCategory(ActionEvent event) {
        model.saveChanges();
    }

    private ExpenseCategoryCreateModel model;
    @FXML
    void initialize() {
        assert CategoryNameField != null : "fx:id=\"CategoryNameField\" was not injected: check your FXML file 'ExpenseCategoryCreate.fxml'.";
        assert CategoryNameError != null : "fx:id=\"CategoryNameError\" was not injected: check your FXML file 'ExpenseCategoryCreate.fxml'.";

        model = new ExpenseCategoryCreateModel();

        CategoryNameField.textProperty().bindBidirectional(model.getCategoryNameProperty());
        CategoryNameError.textProperty().bind(model.getCategoryNameErrorProperty());

    }
}
