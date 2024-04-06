package org.customer_book.Pages.BillsPage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InvoiceCardEquipment {

  @FXML
  private Label EquipmentName;

  @FXML
  void initialize() {}

  public void setEquipmentName(String name) {
    EquipmentName.setText(name);
  }
}
