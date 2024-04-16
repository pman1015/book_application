package org.customer_book.Popups.CompatiblePartCustomerEquipment;

import java.util.concurrent.CompletableFuture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.customer_book.App;
import org.customer_book.Database.InventoryCollection.PartDAO;

public class CompatiblePartPopupController {

  @FXML
  private TextField PartNameField;

  @FXML
  private Label PartNameError;

  @FXML
  private TextField InStockField;

  @FXML
  private Label ExpenseCategoryError;

  @FXML
  private TextField ChargeField;

  @FXML
  private TextField PartNumberField;

  @FXML
  private ComboBox<String> ExpenseCategoryComboBox;

  @FXML
  private Label InStockError;

  @FXML
  private Label ChargeError;

  @FXML
  private Label CostError;

  @FXML
  private TextField CostField;

  @FXML
  private Label PartNumberLabel;

  @FXML
  private Label PartNameLabel;

  @FXML
  void SavePartChanges(ActionEvent event) {
    model.SavePartChanges();
  }

  @FXML
  void ClosePartChanges(ActionEvent event) {
    App.removePopup();
  }
  @FXML
  void incrementStock(){
    model.incrementStock();
  }

  private CompatiblePartPopupModel model;

  @FXML
  void initialize() {
    model = new CompatiblePartPopupModel();
    CompletableFuture<Void> bindErrors = CompletableFuture.runAsync(() ->
      bindError()
    );
    CompletableFuture<Void> bindFields = CompletableFuture.runAsync(() ->
      bindFields()
    );
    CompletableFuture.allOf(bindErrors, bindFields).join();
  }

  /**
   * bindError:
   * This method binds the error messages to the model properties
   */
  public void bindError() {
    PartNameError.textProperty().bindBidirectional(model.getPartNameError());
    PartNumberLabel.textProperty().bindBidirectional(model.getPartNumberError());
    InStockError.textProperty().bindBidirectional(model.getInStockError());
    CostError.textProperty().bindBidirectional(model.getCostError());
    ChargeError.textProperty().bindBidirectional(model.getChargeError());
    ExpenseCategoryError.textProperty().bindBidirectional(model.getExpenseCategoryError());
  }

  /**
   * bindFields:
   * This method binds the fields to the model properties
   */
  public void bindFields() {
    PartNameField.textProperty().bindBidirectional(model.getPartNameProperty());
    PartNumberField
      .textProperty()
      .bindBidirectional(model.getPartNumberProperty());
    InStockField.textProperty().bindBidirectional(model.getInStockProperty());
    CostField.textProperty().bindBidirectional(model.getCostProperty());
    ChargeField.textProperty().bindBidirectional(model.getChargeProperty());
    ExpenseCategoryComboBox
      .valueProperty()
      .bindBidirectional(model.getExpenseCategoryProperty());
  }

  public void setPartDAO(PartDAO part) {
    model.setPart(part);
  }
}
