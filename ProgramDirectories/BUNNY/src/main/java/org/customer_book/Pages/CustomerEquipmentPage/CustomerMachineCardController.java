package org.customer_book.Pages.CustomerEquipmentPage;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.customer_book.Database.MachinesCollection.MachineDAO;

public class CustomerMachineCardController {

  @FXML
  private Label ModelNumber;

  @FXML
  void SetMachineAsSelected() {
    if (model == null) return;
    model.setSelfSelected();
  }

  private CustomerMachineCardModel model;

  @FXML
  void initialize() {
    model = new CustomerMachineCardModel();

    ModelNumber.textProperty().bind(model.getModelNumber());

    assert ModelNumber !=
    null : "fx:id=\"ModelNumber\" was not injected: check your FXML file 'CustomerMachineCard.fxml'.";
  }
  public void setMachineAndSelectedMachine(MachineDAO machine, ObjectProperty<MachineDAO> selectedMachine) {
    model.setMachine(machine);
    model.setSelectedMachine(selectedMachine);
  }
}
