package org.customer_book.Popups.EquipmentCreate;


import org.customer_book.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EquipmentCreateController {


  @FXML
  private TextField NewEquipmentNotes;

  @FXML
  private TextField NewEquipmentName;

  @FXML
  private Label NewEquipmentNotesError;

  @FXML
  private Label NewEquipmentNameError;

  @FXML
  void closeAdd(ActionEvent event) {
    App.removePopup();
  }

  @FXML
  void addNewEquipment(ActionEvent event) {
    model.addEquipment();
  }


  private EquipmentCreateModel model;

  @FXML
  void initialize() {
    //-----------------initialise model-----------------
    model = new EquipmentCreateModel();
    //-----------------Error handeling for undetected fxID's-----------------
    assert NewEquipmentNotes !=
    null : "fx:id=\"NewEquipmentNotes\" was not injected: check your FXML file 'CreateEquipment.fxml'.";
    assert NewEquipmentName !=
    null : "fx:id=\"NewEquipmentName\" was not injected: check your FXML file 'CreateEquipment.fxml'.";
    assert NewEquipmentNotesError !=
    null : "fx:id=\"NewEquipmentNotesError\" was not injected: check your FXML file 'CreateEquipment.fxml'.";
    assert NewEquipmentNameError !=
    null : "fx:id=\"NewEquipmentNameError\" was not injected: check your FXML file 'CreateEquipment.fxml'.";
    //-----------------Bind model to view-----------------
    NewEquipmentName.textProperty().bindBidirectional(model.getNewDAO().getEquipmentModelNumberProperty());
    NewEquipmentNotes.textProperty().bindBidirectional(model.getNewDAO().getEquipmentNotesProperty());
    NewEquipmentNameError.textProperty().bind(model.getModelError());
    NewEquipmentNotesError.textProperty().bind(model.getNotesError());
}
}
