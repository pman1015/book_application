package org.customer_book.Pages.PageComponents.NavBar;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class NavBarController {

  private NavBarModel model;

  @FXML
  private VBox NavBar;

  @FXML
  void initialize() throws IOException {
    model = new NavBarModel();
    NavBar.getChildren().setAll(model.getNavButtons());
  }

 
}
