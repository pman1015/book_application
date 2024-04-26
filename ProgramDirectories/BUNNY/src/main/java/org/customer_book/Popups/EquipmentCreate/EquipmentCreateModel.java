package org.customer_book.Popups.EquipmentCreate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;

@Getter
@Setter
public class EquipmentCreateModel {

  //DAO to be added to the database
  private EquipmentDAO newDAO;

  //--------------------FXML Attributes----------------//
  //These store the error messages to be displayed
  private StringProperty modelError;
  private StringProperty notesError;

  //--------------------NoArgs constructor ----------------------//
  public EquipmentCreateModel() {
    //----initialize the DAO----//
    newDAO = new EquipmentDAO();
    //----initialize the error messages----//
    modelError = new SimpleStringProperty();
    notesError = new SimpleStringProperty();
  }

  /**
   * addEquipment:
   * This Function checks the data stored in the DAO then if its valid
   * adds it to the database and closes the popu[]
   */
   
  public void addEquipment() {
    if (validateEquipment()) {
      if(newDAO.getNotes() == null) {
        newDAO.setNotes("");
      }
      if(newDAO.getParts()== null){
        newDAO.setParts(new ArrayList<ObjectId>());
      }
      DatabaseConnection.equipmentCollection.addEquipment(newDAO);
      //Remove the popup from the scene Graph
      App.removePopup();
    }
  }

  /**
   * validateEquipment:
   * This function checks the data stored in the DAO and sets the error messages
   * if the data is invalid it returns false otherwise it returns true
   * @return
   */
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
    if (newDAO.getNotes() == null || newDAO.getNotes().isEmpty()) {
      newDAO.setNotes("");
    }
    return valid;
  }
}
