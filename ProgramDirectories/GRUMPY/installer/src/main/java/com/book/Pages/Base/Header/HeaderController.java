package com.book.Pages.Base.Header;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HeaderController {

    @FXML
    void closeWindow(ActionEvent event) {
        model.closeWindow();
    }

    private HeaderModel model;
    @FXML
    void initialize() {
        model = new HeaderModel();

    }
}
