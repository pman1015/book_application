package org.customer_book.Popups.InventoryFilter;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.bson.conversions.Bson;
import org.customer_book.App;

public class InventoryFilterController {

  @FXML
  private ComboBox<String> PartNumberComboBox;

  @FXML
  private ComboBox<String> PartNameComboBox;

  @FXML
  private ComboBox<String> CompatibleEquipmentComboBox;

  @FXML
  void closePopup(ActionEvent event) {
    App.removePopup();
  }

  @FXML
  void ApplyFilter(ActionEvent event) {
    model.applyFilter();
  }

  @FXML
  void ClearFilter(ActionEvent event) {
    model.clearFilter();
  }

  private InventoryFilterModel model;

  @FXML
  void initialize() {
    model = new InventoryFilterModel();

    PartNumberComboBox.setItems(model.getPartNumbers());
    PartNumberComboBox
      .getEditor()
      .textProperty()
      .bindBidirectional(model.getSelectedPartNumber());
    PartNameComboBox.setItems(model.getPartNames());

    PartNameComboBox
      .getEditor()
      .textProperty()
      .bindBidirectional(model.getSelectedPartName());
    CompatibleEquipmentComboBox.setItems(model.getCompatibleEquipment());

    CompatibleEquipmentComboBox
      .getEditor()
      .textProperty()
      .bindBidirectional(model.getCompatibleEquipmentName());
  }

  public void setFilter(ObjectProperty<Bson> filter) {
    model.setInventoryfilter(filter);
  }
}
