package org.customer_book.Pages.InventoryPage;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.customer_book.Database.InventoryCollection.PartDAO;

public class InventoryPartCardController {

  @FXML
  private Label PartName;

  @FXML
  private Label PartNumber;

  @FXML
  void initialize() {
    assert PartName !=
    null : "fx:id=\"PartName\" was not injected: check your FXML file 'InventoryPartCard.fxml'.";
    assert PartNumber !=
    null : "fx:id=\"PartNumber\" was not injected: check your FXML file 'InventoryPartCard.fxml'.";
  }

  private InventoryPartCardModel model;

  public void setModel(InventoryPartCardModel model) {
    this.model = model;
    PartName.setText(model.getPart().getPartName());
    PartNumber.setText(model.getPart().getPartNumber());
  }

  public void setSelectedPart(ObjectProperty<PartDAO> selectedPart) {
    this.model.setSelectedPart(selectedPart);
  }



  @FXML
  void selectPart() {
    model.setSelfActive();
  }
}
