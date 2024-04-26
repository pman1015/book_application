package org.customer_book.Popups.PartAdd;

import java.util.ResourceBundle;

import org.customer_book.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreatePartController {

  @FXML
  private Label ChargePriceError;

  @FXML
  private ComboBox<String> ExpenseCategoryComboBox;

  @FXML
  private TextField PartNameField;

  @FXML
  private Label PartNameError;

  @FXML
  private TextField URLField;

  @FXML
  private TextField PurchasePriceField;

  @FXML
  private TextField ChargePriceField;

  @FXML
  private Label PartNumberError;

  @FXML
  private Label PurchasePriceError;

  @FXML
  private Label URLError;

  @FXML
  private TextField PartNumberField;

  @FXML
  private Label ExpenseError;

  @FXML
  void closeAdd(ActionEvent event) {
    App.removePopup();
  }

  @FXML
  void ShowNewExpense(ActionEvent event) {}

  @FXML
  void addPart(ActionEvent event) {
    model.addPart();
  }

  private CreatePartModel model;

  @FXML
  void initialize() {
    model = new CreatePartModel();

    //--------------------Bind Inputs to model properties---------------//
    PartNameField.textProperty().bindBidirectional(model.getPartName());
    PartNumberField.textProperty().bindBidirectional(model.getPartNumber());
    PurchasePriceField.textProperty().bindBidirectional(model.getPurchasePrice());
    ChargePriceField.textProperty().bindBidirectional(model.getChargePrice());
    URLField.textProperty().bindBidirectional(model.getUrl());
    ExpenseCategoryComboBox.valueProperty().bindBidirectional(model.getExpenseCategory());
    
    //--------------------Bind Errors to model properties---------------//
    PartNameError.textProperty().bind(model.getPartNameError());
    PartNumberError.textProperty().bind(model.getPartNumberError());
    PurchasePriceError.textProperty().bind(model.getPurchasePriceError());
    ChargePriceError.textProperty().bind(model.getChargePriceError());
    URLError.textProperty().bind(model.getUrlError());
    ExpenseError.textProperty().bind(model.getExpenseError());

}
}
