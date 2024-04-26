package org.customer_book.Popups.PartAdd;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.InventoryCollection.PartDAO;

@Getter
@Setter
public class CreatePartModel {

  //--------------------FXML Attributes inputs----------------//
  private StringProperty partName;
  private StringProperty partNumber;
  private StringProperty purchasePrice;
  private StringProperty chargePrice;
  private StringProperty url;
  private StringProperty expenseCategory;

  //--------------------FXML Attributes errors----------------//
  private StringProperty partNameError;
  private StringProperty partNumberError;
  private StringProperty purchasePriceError;
  private StringProperty chargePriceError;
  private StringProperty urlError;
  private StringProperty expenseError;

  //--------------------DAO to be added----------------//
  private PartDAO newPart;

  public CreatePartModel() {
    //---initailize the DAO---//
    newPart = new PartDAO();

    //---initailize all input properties---//
    partName = new SimpleStringProperty("");
    partNumber = new SimpleStringProperty("");
    purchasePrice = new SimpleStringProperty("");
    chargePrice = new SimpleStringProperty("");
    url = new SimpleStringProperty("");
    expenseCategory = new SimpleStringProperty("");

    //---initailize all error properties---//
    partNameError = new SimpleStringProperty();
    partNumberError = new SimpleStringProperty();
    purchasePriceError = new SimpleStringProperty();
    chargePriceError = new SimpleStringProperty();
    urlError = new SimpleStringProperty();
    expenseError = new SimpleStringProperty();
  }

  public void addPart() {
    boolean validPart = true;
    //---validate all inputs---//
    if (partName.get().isEmpty()) {
      partNameError.set("Part Name is required");
      validPart = false;
    } else {
      if (
        DatabaseConnection.inventoryCollection.partNameExists(partName.get())
      ) {
        partNameError.set("Part Name already exists");
        validPart = false;
      } else {
        partNameError.set("");
      }
    }

    if (partNumber.get().isEmpty()) {
      partNumberError.set("Part Number is required");
      validPart = false;
    } else {
      if (
        DatabaseConnection.inventoryCollection.partNumberExists(
          partNumber.get()
        )
      ) {
        partNumberError.set("Part Number already exists");
        validPart = false;
      } else {
        partNumberError.set("");
      }
    }

    if (purchasePrice.get().isEmpty()) {
      purchasePriceError.set("Purchase Price is required");
      validPart = false;
    } else {
      if (purchasePrice.get().matches(".*[a-zA-Z]+.*")) {
        purchasePriceError.set("Purchase Price must be a number");
        validPart = false;
      } else {
        purchasePriceError.set("");
      }
    }

    if (chargePrice.get().isEmpty()) {
      chargePriceError.set("Charge Price is required");
      validPart = false;
    } else {
      if (chargePrice.get().matches(".*[a-zA-Z]+.*")) {
        chargePriceError.set("Charge Price must be a number");
        validPart = false;
      } else {
        chargePriceError.set("");
      }
    }

    if (!validPart) {
      return;
    }
    //---set the DAO---//
    newPart.setPartName(partName.get());
    newPart.setPartNumber(partNumber.get());
    newPart.setCost(Double.parseDouble(purchasePrice.get()));
    newPart.setCharge(Double.parseDouble(chargePrice.get()));
    newPart.setUrl(url.get());
    newPart.setExpenseCategory(expenseCategory.get());
    newPart.setInStock(0);
    newPart.setCompatibleEquipment(new ArrayList<>());
    //---add the part to the database---//
    DatabaseConnection.inventoryCollection.addPart(newPart);
    //---close the popup---//
    App.removePopup();
  }
}
