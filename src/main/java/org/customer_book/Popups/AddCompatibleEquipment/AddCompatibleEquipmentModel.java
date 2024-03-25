package org.customer_book.Popups.AddCompatibleEquipment;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.InventoryCollection.PartDAO;

@Getter
@Setter
@ToString
public class AddCompatibleEquipmentModel {

  private ObjectProperty<PartDAO> part;;
  private ObservableList<String> equipmentList;
  private StringProperty selectedProperty;

  public AddCompatibleEquipmentModel() {
    selectedProperty = new SimpleStringProperty();
    equipmentList =
      FXCollections.observableArrayList(
        DatabaseConnection.equipmentCollection.getAllEquipmentList()
      );
  }

  public void setPart(ObjectProperty<PartDAO> part) {
    this.part = part;
    //Get all of the model numbers of the equipment stored in the part
    ArrayList<String> equipmentAlreadyAdded = new ArrayList<>();
    DatabaseConnection.equipmentCollection
      .getEquipmentList(part.get().getCompatibleEquipment())
      .forEach(e -> {
        equipmentAlreadyAdded.add(e.getModelNumber());
      });
    //Remove the equipment that is already added to the part
    equipmentList.removeAll(equipmentAlreadyAdded);
  }

  public void addCompatible() {
    if (selectedProperty.get() != null) {
      //Get the equipment id of the selected value
      ObjectId equipmentId = DatabaseConnection.equipmentCollection.getEquipmentID(
        selectedProperty.get()
      );
      if (equipmentId != null) {
        part.get().getCompatibleEquipment().add(equipmentId);
        DatabaseConnection.inventoryCollection.updatePart(part.get());
        App.removePopup();
      }
    }
  }
}
