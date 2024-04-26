package org.customer_book.Pages.PageComponents.NavBar;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NavButtonController {

  private NavButtonModel model;

  @FXML
  private ResourceBundle resources;

  @FXML
  private Button NavOption;

  @FXML
  void NavigateTo(ActionEvent event) {
    try {
      model.navigate(NavOption.getText());
    } catch (IOException e) {
      
      e.printStackTrace();
    }
  }

  @FXML
  void initialize() {
    model = new NavButtonModel();
    assert NavOption !=
    null : "fx:id=\"NavOption\" was not injected: check your FXML file 'NavButton.fxml'.";
  }

  public void setButtonText(String text) {
    NavOption.setText(text);
  }
}
