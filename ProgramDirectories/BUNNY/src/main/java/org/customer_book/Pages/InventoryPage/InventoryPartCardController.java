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
  void selectPart() {
    model.setSelfActive();
  }
 
  private InventoryPartCardModel model;
  @FXML
  void initialize() {
    model = new InventoryPartCardModel();

    PartName.textProperty().bind(model.getPartName());
    PartNumber.textProperty().bind(model.getPartNumber());
  }

 
 public void setPart(PartDAO part, ObjectProperty<PartDAO> selectedPart){
    model.setPart(part, selectedPart);
  }
  
 
}
