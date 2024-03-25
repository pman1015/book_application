package org.customer_book.Pages.InventoryPage;

import org.customer_book.Database.EquipmentCollection.EquipmentDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InventoryCompatibleMachineCardController {

  @FXML
  private Label MachineName;

  @FXML
  void removeMachine(ActionEvent event) {}

  private InventoryCompatibleMachineCardModel model;
  @FXML
  void initialize() {
    model = new InventoryCompatibleMachineCardModel();
    MachineName.textProperty().bind(model.getMachineNameProperty());
    assert MachineName !=
    null : "fx:id=\"MachineName\" was not injected: check your FXML file 'CompatibleEquipmentCard.fxml'.";
  }

public void setEquipment(EquipmentDAO equipment) {
  model.setEquipment(equipment);
}

}
