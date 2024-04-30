package com.book.Pages.BuildDatabase.MenuPages.SuscessPage;


import com.book.App;
import com.book.Pages.BuildDatabase.MenuPages.PageControllerAbstract;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SuscessPageController extends PageControllerAbstract{

    @FXML
    private TextArea connectionString;

    @FXML
    void exit(ActionEvent event) {
        App.closeWindow();
    }

    @FXML
    void initialize() {
        model = new SuscessPageModel();
        connectionString.textProperty().bind(((SuscessPageModel) model).getConnectionString());
    }
}
