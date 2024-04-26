package org.customer_book.Popups.AddCompatiblePart;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.InventoryCollection.PartDAO;

@Getter
@Setter
public class AddCompatiblePartModel {

  private EquipmentDAO equipment;
  private ObservableList<String> partNames;
  private ArrayList<PartDAO> parts;

  public AddCompatiblePartModel() {
    partNames = FXCollections.observableArrayList();
  }

  public void setEquipment(EquipmentDAO equipment) {
    this.equipment = equipment;
    parts =
      DatabaseConnection.inventoryCollection.getNamesList(equipment.getParts());
    for (PartDAO part : parts) {
      partNames.add(part.getPartName());
    }
  }

  public void addPart(String value) {
    PartDAO part = parts.get(partNames.indexOf(value));
    if(part == null) {
      return;
    }
    DatabaseConnection.equipmentCollection.addCompatiblePart(
      equipment.getId(),
      part.getId()
    );
    DatabaseConnection.inventoryCollection.addCompatibleEquipment(
      part.getId(),
      equipment.getId()
    );
    App.removePopup();
  }
}
