package org.customer_book.Popups.AddEquipmentToUser;

import java.util.ArrayList;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.MachinesCollection.MachineDAO;

@Getter
@Setter
@NoArgsConstructor
public class AddEquipmentToUserModel {

  private CustomerDAO user;
  private StringProperty selectedEquipmentProperty = new SimpleStringProperty();
  private StringProperty equipmentNameErrorProperty = new SimpleStringProperty();
  private ObservableList<String> equipmentListProperty = FXCollections.observableArrayList();
  private ArrayList<String> masterList = new ArrayList<String>();

  public void setUser(CustomerDAO user) {
    this.user = user;
    DatabaseConnection.equipmentCollection
      .getAllEquipmentList()
      .stream()
      .forEach(equipment -> {
        equipmentListProperty.add(equipment);
      });
    getAllEquipmentList();
   
  }

  public void getAllEquipmentList() {
    equipmentListProperty.clear();
    DatabaseConnection.equipmentCollection
      .getAllEquipmentList()
      .stream()
      .forEach(equipment -> {
        equipmentListProperty.add(equipment);
      });
      masterList.addAll(equipmentListProperty);
  }

  public boolean addEquipmentToUser() {
    if (selectedEquipmentProperty.get() == null) {
      equipmentNameErrorProperty.set("Please select an equipment");
      return false;
    } else {
      if (equipmentListProperty.contains(selectedEquipmentProperty.get())) {
        ObjectId addedMachineID = generateNewMachine();
        user.addMachine(addedMachineID);
        DatabaseConnection.customerCollection.updateCustomer(user);
        return true;
      } else {
        equipmentNameErrorProperty.set("Please select an equipment");
        return false;
      }
    }
  }

  public ObjectId generateNewMachine() {
    MachineDAO newMachine = new MachineDAO();
    newMachine.setCustomerName(user.getName());
    newMachine.setEquipmentId(
      DatabaseConnection.equipmentCollection.getEquipmentId(
        selectedEquipmentProperty.get()
      )
    );
    newMachine.setLastWorkedOn("Never");
    newMachine.setNotes("");
    return DatabaseConnection.machineCollection.addMachine(newMachine);
  }
}
