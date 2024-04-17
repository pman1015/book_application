package org.customer_book.Popups.CompatiblePartCustomerEquipment;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.InventoryCollection.PartDAO;
import org.customer_book.Database.ReportsCollection.ReportDAO;

@Getter
@Setter
public class CompatiblePartPopupModel {

  //------------------------View Properties------------------------//
  //Field Values
  private StringProperty PartNameProperty = new SimpleStringProperty("");
  private StringProperty PartNumberProperty = new SimpleStringProperty("");
  private StringProperty InStockProperty = new SimpleStringProperty("");
  private StringProperty CostProperty = new SimpleStringProperty("");
  private StringProperty ChargeProperty = new SimpleStringProperty("");
  private StringProperty ExpenseCategoryProperty = new SimpleStringProperty("");
  private ObservableList<String> expenseCategoriesProperty = FXCollections.observableArrayList();
  //Error Messages
  private StringProperty PartNameError = new SimpleStringProperty("");
  private StringProperty PartNumberError = new SimpleStringProperty("");
  private StringProperty InStockError = new SimpleStringProperty("");
  private StringProperty CostError = new SimpleStringProperty("");
  private StringProperty ChargeError = new SimpleStringProperty("");
  private StringProperty ExpenseCategoryError = new SimpleStringProperty("");

  //------------------------Model Properties------------------------//
  private PartDAO part;

  //------------------------ Constructor ------------------------//
  public CompatiblePartPopupModel() {}

  /**
   * setPart:
   * This method sets the PartDAO object and updates the view properties
   */
  public void setPart(PartDAO part) {
    this.part = part;

    PartNameProperty.set(part.getPartName());
    PartNumberProperty.set(part.getPartNumber());
    InStockProperty.set(String.valueOf(part.getInStock()));
    CostProperty.set(String.valueOf(part.getCost()));
    ChargeProperty.set(String.valueOf(part.getCharge()));
    ExpenseCategoryProperty.set(part.getExpenseCategory());

    loadExpenseCategories();
  }

  public void updateDAO() {
    part.setPartName(PartNameProperty.get());
    part.setPartNumber(PartNumberProperty.get());
    part.setInStock(Integer.valueOf(InStockProperty.get()));
    part.setCost(Double.valueOf(CostProperty.get()));
    part.setCharge(Double.valueOf(ChargeProperty.get()));
    part.setExpenseCategory(ExpenseCategoryProperty.get());
  }

  /**
   * SavePartChanges:
   * This method validates the fields and updates the error properties
   */
  public void SavePartChanges() {
    updateDAO();
    boolean valid = true;
    //Store the error message for each field
    String priceError = part.validateCost();
    String chargeError = part.validateCharge();
    String quantityError = part.validateInStock();
    //Run validation checks on the fields and updates the error properties
    if (part.getPartName().equals("")) {
      PartNameError.set("Part Name cannot be empty");
      valid = false;
    } else {
      PartNameError.set("");
    }
    if (part.getPartNumber().equals("")) {
      PartNumberError.set("Part Number cannot be empty");
      valid = false;
    } else {
      PartNumberError.set("");
    }
    if (priceError != null) {
      CostError.set(priceError);
      valid = false;
    } else {
      CostError.set("");
    }
    if (chargeError != null) {
      ChargeError.set(chargeError);
      valid = false;
    } else {
      ChargeError.set("");
    }
    if (quantityError != null) {
      InStockError.set(quantityError);
      valid = false;
    } else {
      InStockError.set("");
    }
    if (valid) {
      DatabaseConnection.inventoryCollection.updatePart(part);
      App.removePopup();
    }
  }

  /**
   * Loads the expense categories from the reports database
   */
  public void loadExpenseCategories() {
    expenseCategoriesProperty.clear();
    ReportDAO categoryReport = DatabaseConnection.reportsCollection.getReport(
      "ExpenseCategories"
    );
    if (categoryReport != null) {
      expenseCategoriesProperty.addAll(categoryReport.getValues());
    }
  }

  public void incrementStock() {
    InStockProperty.set(
      String.valueOf(Integer.valueOf(InStockProperty.get()) + 1)
    );
  }
}
