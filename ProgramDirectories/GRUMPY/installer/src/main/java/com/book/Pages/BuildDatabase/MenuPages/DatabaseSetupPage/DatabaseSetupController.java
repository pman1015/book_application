package com.book.Pages.BuildDatabase.MenuPages.DatabaseSetupPage;

import com.book.Pages.BuildDatabase.MenuPages.PageControllerAbstract;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DatabaseSetupController extends PageControllerAbstract {

    @FXML
    private ListView<Parent> DatabaseCards;

    @FXML
    private Label ErrorMessage;

    @FXML
    private Button FixErrorButton;

    @FXML
    void FixError(ActionEvent event) {

        ((DatabaseSetupModel)model).FixError();

    }

    @FXML
    void initialize() {
        model = new DatabaseSetupModel();
        ErrorMessage.textProperty().bind(((DatabaseSetupModel) model).getErrorMessage());
        DatabaseCards.setItems(((DatabaseSetupModel) model).getDatabaseCards());
    }
}
