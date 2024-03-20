package org.customer_book.Popups.EquipmentCreate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;

@Getter
@Setter
public class EquipmentCreateModel {

  private EquipmentDAO newDAO;
  private StringProperty modelError = new SimpleStringProperty();
  private StringProperty notesError = new SimpleStringProperty();

  public EquipmentCreateModel() {
    newDAO = new EquipmentDAO();
  }

  public void addEquipment() {
    if (validateEquipment()) {
      DatabaseConnection.equipmentCollection.addEquipment(newDAO);
      App.removePopup();
    }
  }

  private boolean validateEquipment() {
    boolean valid = true;
    //Validate Model Number and set Respective Errors
    if (newDAO.getModelNumber().isEmpty()) {
      valid = false;
      modelError.set("Model Number is required");
    } else {
      if (
        DatabaseConnection.equipmentCollection.modelNumberExists(
          newDAO.getModelNumber()
        )
      ) {
        valid = false;
        modelError.set("Model Number already exists");
      }
    }
    //Validate Notes and set Respective Errors
    //Note: -Currently not required
    if (newDAO.getNotes()== null || newDAO.getNotes().isEmpty()) {
      newDAO.setNotes("");
    }

    return valid;
  }
}
