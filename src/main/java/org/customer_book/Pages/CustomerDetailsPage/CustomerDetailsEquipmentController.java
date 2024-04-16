package org.customer_book.Pages.CustomerDetailsPage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.customer_book.Database.MachinesCollection.MachineDAO;

public class CustomerDetailsEquipmentController {

  @FXML
  private TextArea MachineNotes;

  @FXML
  private TextArea EquipmentNotes;

  @FXML
  private Label EquipmentName;

  private CustomerDetailsEquipmentModel model;

  @FXML
  void initialize() {
    model = new CustomerDetailsEquipmentModel();
    //-----------------Bind the model to the view-----------------//
    EquipmentName.textProperty().bind(model.getEquipmentName());
    EquipmentNotes.textProperty().bind(model.getEquipmentNotes());
    MachineNotes.textProperty().bindBidirectional(model.getMachineNotes());


  }

  public void setMachine(MachineDAO machine) {
    model.setMachine(machine);
  }
}
