package org.customer_book.Popups.AddEquipmentToUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;

public class AddEquipmentToUserController {

  @FXML
  private Label EquipmentNameError;

  @FXML
  private ComboBox<String> EquipmentList;

  @FXML
  void addEquipmentToUser(ActionEvent event) {
    if (model.addEquipmentToUser()) {
      App.removePopup();
    }
  }

  @FXML
  void closeAdd(ActionEvent event) {
    App.removePopup();
  }

  private AddEquipmentToUserModel model;

  @FXML
  void initialize() {
    model = new AddEquipmentToUserModel();

    EquipmentNameError
      .textProperty()
      .bind(model.getEquipmentNameErrorProperty());
    EquipmentList
      .valueProperty()
      .bindBidirectional(model.getSelectedEquipmentProperty());
    EquipmentList.setItems(model.getEquipmentListProperty());
    EquipmentList
      .getEditor()
      .textProperty()
      .addListener((obs, oldText, newText) -> {
        if (newText == null || newText.isEmpty()) {
          EquipmentList.setItems(
            FXCollections.observableArrayList(model.getMasterList())
          );
          return;
        }
        EquipmentList.setItems(filter(model.getMasterList(), newText));
      });
  }

  private ObservableList<String> filter(
    ArrayList<String> equipmentListProperty,
    String newText
  ) {
    List<String> refinedValues = equipmentListProperty
      .stream()
      .filter(equipment ->
        equipment.toLowerCase().contains(newText.toLowerCase())
      )
      .collect(Collectors.toList());
    return FXCollections.observableArrayList(new ArrayList<>(refinedValues));
  }

  public void setCustomer(CustomerDAO customer) {
    model.setUser(customer);
  }
}
