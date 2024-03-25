package org.customer_book.Pages.InventoryPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InventoryCompatibleMachineCardController {

  @FXML
  private Label MachineName;

  @FXML
  void removeMachine(ActionEvent event) {}

  @FXML
  void initialize() {
    assert MachineName !=
    null : "fx:id=\"MachineName\" was not injected: check your FXML file 'CompatibleEquipmentCard.fxml'.";
  }
}
