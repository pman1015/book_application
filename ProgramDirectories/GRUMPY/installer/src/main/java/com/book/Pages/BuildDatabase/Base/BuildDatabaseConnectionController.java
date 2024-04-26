package com.book.Pages.BuildDatabase.Base;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class BuildDatabaseConnectionController {


    @FXML
    private Button NextPaneButton;

    @FXML
    private StackPane CurrentPane;

    @FXML
    private Button PreviousPaneButton;

    @FXML
    void PerviousPane(ActionEvent event) {
        model.getPrevious();
    }

    @FXML
    void NextPane(ActionEvent event) {
        model.getNext();

    }

    private BuildDatabaseConnectionModel model;
    @FXML
    void initialize() {
        model = new BuildDatabaseConnectionModel();
        
        
        model.getCurrentPage().addListener((observable, oldValue, newValue) -> {
            CurrentPane.getChildren().clear();
            CurrentPane.getChildren().add(newValue);
        });
        NextPaneButton.disableProperty().bind(model.getShowNext().not());
        NextPaneButton.visibleProperty().bind(model.getShowNext());
        PreviousPaneButton.disableProperty().bind(model.getShowPrevious().not());
        PreviousPaneButton.visibleProperty().bind(model.getShowPrevious());

    }
}
