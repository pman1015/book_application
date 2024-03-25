package org.customer_book.Pages.InventoryPage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.customer_book.Database.InventoryCollection.PartDAO;

public class InventoryPageController {

  @FXML
  private Button EditButton;

  @FXML
  private AnchorPane PartNameEdit;

  @FXML
  private Label PartNameError;

  @FXML
  private ScrollPane PartsScrollPane;

  @FXML
  private Label PartNumberError;

  @FXML
  private Label URLError;

  @FXML
  private AnchorPane CloseSVG;

  @FXML
  private TextField URLField;

  @FXML
  private AnchorPane URLEdit;

  @FXML
  private Label PartNumberLabel;

  @FXML
  private ListView<Parent> CompatibleEquipmentList;

  @FXML
  private Label PartNameLabel;

  @FXML
  private AnchorPane PurchasePriceEdit;

  @FXML
  private TextField PartNameField;

  @FXML
  private Label StockError;

  @FXML
  private AnchorPane CloseSVG1;

  @FXML
  private Label URLLabel;

  @FXML
  private Label StockLabel;

  @FXML
  private TextField ChargeField;

  @FXML
  private Label ExpenseCategoryLabel;

  @FXML
  private TextField PartNumberField;

  @FXML
  private AnchorPane ChargeEdit;

  @FXML
  private AnchorPane StockEdit;

  @FXML
  private Label ChargeError;

  @FXML
  private HBox editOptions;

  @FXML
  private TextField PurchasePriceField;

  @FXML
  private ListView<Parent> PartList;

  @FXML
  private Label PurchasePriceLabel;

  @FXML
  private Label PurchasePriceError;

  @FXML
  private VBox PartDetails;

  @FXML
  private TextField StockField;

  @FXML
  private ChoiceBox<String> ExpenseCategoryField;

  @FXML
  private Label ChargeLabel;

  @FXML
  private AnchorPane PartNumberEdit;

  @FXML
  private HBox ExpenseCategoryEdit;

  @FXML
  void showFilterOptions(ActionEvent event) {}

  @FXML
  void showAddPart(ActionEvent event) {
    model.showAddPart();
  }

  @FXML
  void enableDetailsEdit(ActionEvent event) {
    model.toggleEditMode();
  }

  @FXML
  void saveChanges(ActionEvent event) {
    if (model.validateChanges()) {
      model.saveChanges();
      model.toggleEditMode();
    }
  }

  @FXML
  void cancelChanges(ActionEvent event) {
    model.resetValues();
    model.toggleEditMode();
  }

  @FXML
  void showAddNewCategory(ActionEvent event) {
    model.showAddExpenseCategory();
  }

  @FXML
  void showAddCompatible(ActionEvent event) {
    model.addCompatibleEquipment();
  }

  private InventoryPageModel model;

  @FXML
  void initialize() {
    //---- initalise model ---------
    model = new InventoryPageModel();
    //----- EventListners -----------
    PartsScrollPane
      .vvalueProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue.doubleValue() == 1.0) {
          model.loadCards();
        }
      });
    //-- Bindings -------------------
    PartList.setItems(model.getPartsCardsProperty());
    bindVisibility();

    //---- Error Labels Binding ----//
    PartNameError.textProperty().bind(model.getPartNameErrorProperty());
    PartNumberError.textProperty().bind(model.getPartNumberErrorProperty());
    URLError.textProperty().bind(model.getURLErrorProperty());
    PurchasePriceError
      .textProperty()
      .bind(model.getCurrentPurchasePriceErrorProperty());
    ChargeError.textProperty().bind(model.getChargePriceErrorProperty());
    StockError.textProperty().bind(model.getQuantityErrorProperty());

    //----- event listners ------//
    model
      .getSelectedPartProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
          PartDetails.setVisible(true);
          model.loadCompatibleEquipment();
          //---- bind the values in the part to the fields ----//
          unBindFromOldValue(oldValue);
          bindToNewValue(newValue);
          model.resetErrors();
          oldValue.resetChanges();
        } else {
          PartDetails.setVisible(false);
        }
      });
  }

  public void bindVisibility() {
    EditButton.visibleProperty().bind(model.getEditModeProperty().not());
    editOptions.visibleProperty().bind(model.getEditModeProperty());
    PartDetails.setVisible(false);
    //---- Bindings for the part details visiblity ----//
    PartNameEdit.visibleProperty().bind(model.getEditModeProperty());
    PartNameLabel.visibleProperty().bind(model.getEditModeProperty().not());
    PartNumberEdit.visibleProperty().bind(model.getEditModeProperty());
    PartNumberLabel.visibleProperty().bind(model.getEditModeProperty().not());
    PurchasePriceEdit.visibleProperty().bind(model.getEditModeProperty());
    PurchasePriceLabel
      .visibleProperty()
      .bind(model.getEditModeProperty().not());
    ChargeEdit.visibleProperty().bind(model.getEditModeProperty());
    ChargeLabel.visibleProperty().bind(model.getEditModeProperty().not());
    StockEdit.visibleProperty().bind(model.getEditModeProperty());
    StockLabel.visibleProperty().bind(model.getEditModeProperty().not());
    URLEdit.visibleProperty().bind(model.getEditModeProperty());
    URLLabel.visibleProperty().bind(model.getEditModeProperty().not());
    ExpenseCategoryLabel
      .visibleProperty()
      .bind(model.getEditModeProperty().not());
    ExpenseCategoryEdit.visibleProperty().bind(model.getEditModeProperty());
  }

  public void unBindFromOldValue(PartDAO oldValue) {
    PartNameField
      .textProperty()
      .unbindBidirectional(oldValue.getPartNameProperty());
    PartNameLabel.textProperty().unbind();
    PartNumberField
      .textProperty()
      .unbindBidirectional(oldValue.getPartNumberProperty());
    PartNumberLabel.textProperty().unbind();
    PurchasePriceField
      .textProperty()
      .unbindBidirectional(oldValue.getPartCostProperty());
    PurchasePriceLabel.textProperty().unbind();
    ChargeField
      .textProperty()
      .unbindBidirectional(oldValue.getPartChargeProperty());
    ChargeLabel.textProperty().unbind();
    StockField
      .textProperty()
      .unbindBidirectional(oldValue.getPartInStockProperty());
    StockLabel.textProperty().unbind();
    URLField.textProperty().unbindBidirectional(oldValue.getPartURLProperty());
    URLLabel.textProperty().unbind();
    ExpenseCategoryField
      .valueProperty()
      .unbindBidirectional(oldValue.getPartExpenseCategoryProperty());
    ExpenseCategoryField.setItems(model.getExpenseCategoriesProperty());
    ExpenseCategoryLabel.textProperty().unbind();
   
  }

  public void bindToNewValue(PartDAO newValue) {
    PartNameField
      .textProperty()
      .bindBidirectional(newValue.getPartNameProperty());
    PartNameLabel.textProperty().bind(newValue.getPartNameProperty());
    PartNumberField
      .textProperty()
      .bindBidirectional(newValue.getPartNumberProperty());
    PartNumberLabel.textProperty().bind(newValue.getPartNumberProperty());
    PurchasePriceField
      .textProperty()
      .bindBidirectional(newValue.getPartCostProperty());
    PurchasePriceLabel.textProperty().bind(newValue.getPartCostProperty());
    ChargeField
      .textProperty()
      .bindBidirectional(newValue.getPartChargeProperty());
    ChargeLabel.textProperty().bind(newValue.getPartChargeProperty());
    StockField
      .textProperty()
      .bindBidirectional(newValue.getPartInStockProperty());
    StockLabel.textProperty().bind(newValue.getPartInStockProperty());
    URLField.textProperty().bindBidirectional(newValue.getPartURLProperty());
    URLLabel.textProperty().bind(newValue.getPartURLProperty());
    ExpenseCategoryField
      .valueProperty()
      .bindBidirectional(newValue.getPartExpenseCategoryProperty());
    ExpenseCategoryLabel
      .textProperty()
      .bind(newValue.getPartExpenseCategoryProperty());
    CompatibleEquipmentList.setItems(model.getEquipmentCards());
  }
}
