package org.customer_book.Pages.EquipmentPage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EquipmentPartCardController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Label PartName;

  @FXML
  private Label PartNumber;

  @FXML
  void ChangeSelectedModel(ActionEvent event) {}

  @FXML
  void initialize() {
    assert PartName !=
    null : "fx:id=\"PartName\" was not injected: check your FXML file 'PartCard.fxml'.";
    assert PartNumber !=
    null : "fx:id=\"PartNumber\" was not injected: check your FXML file 'PartCard.fxml'.";
  }
}
