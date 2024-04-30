package com.book.Pages.Base.NavBar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class NavBarController {

    @FXML
    void goToBuild(ActionEvent event) {
        model.goToBuild();
    }
    @FXML void goToAtlasGuide(ActionEvent event) {
        model.goToAtlasGuide();
    }

    private NavBarModel model;
    @FXML
    void initialize() {
        model = new NavBarModel();
    }
}
