package org.customer_book.Popups.InventoryFilter;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;

@Getter
@Setter
public class InventoryFilterModel {

  // ------------------- FXML Properties ------------------- //
  private ObservableList<String> partNumbers = FXCollections.observableArrayList();
  private ArrayList<String> partNumberStrings = new ArrayList<>();
  private StringProperty selectedPartNumber = new SimpleStringProperty("");

  private ObservableList<String> partNames = FXCollections.observableArrayList();
  private ArrayList<String> partNameStrings = new ArrayList<>();
  private StringProperty selectedPartName = new SimpleStringProperty("");

  private ObservableList<String> compatibleEquipment = FXCollections.observableArrayList();
  private ArrayList<String> compatibleEquipmentStrings = new ArrayList<>();
  private StringProperty compatibleEquipmentName = new SimpleStringProperty("");

  //------------------- Filter Properties ------------------- //
  private ObjectProperty<Bson> Inventoryfilter;
  private HashMap<String, ObjectId> modelNameToID = new HashMap<>();

  @SuppressWarnings("unchecked")
  public InventoryFilterModel() {
    Object[] partInfo = DatabaseConnection.inventoryCollection.getAllNamesAndNumber();
    partNumberStrings = (ArrayList<String>) partInfo[1];
    partNameStrings = (ArrayList<String>) partInfo[0];
    partNumbers.addAll(partNumberStrings);
    partNames.addAll(partNameStrings);
    loadCompatibleEquipment();
    selectedPartNumber.addListener((obs, oldVal, newVal) -> refinePartNumbers(newVal));
    selectedPartName.addListener((obs, oldVal, newVal) -> refinePartNames(newVal));
    compatibleEquipmentName.addListener((obs, oldVal, newVal) -> refineMachines(newVal));
  }

  public void refinePartNumbers(String input) {
    partNames.clear();
    ArrayList<String> newPartNumbers = new ArrayList<>();
    for (String partNumber : partNumberStrings) {
      if (partNumber.contains(input)) {
        if(partNumber.equals(input)){
          selectedPartNumber.set(partNumber);
          return;
        }
        newPartNumbers.add(partNumber);
      }
    }
    partNumbers.setAll(newPartNumbers);
  }

  public void refinePartNames(String input) {
    partNames.clear();
    for (String partName : partNameStrings) {
      if (partName.contains(input)) {
        partNames.add(partName);
      }
    }
  }

  public void refineMachines(String input) {
    compatibleEquipment.clear();
    for (String machine : compatibleEquipmentStrings) {
      if (machine.contains(input)) {
        compatibleEquipment.add(machine);
      }
    }
  }

  public void applyFilter() {
    Bson filter = null;
    if (!selectedPartNumber.get().equals("")) {
      filter = regex("partNumber", selectedPartNumber.get());
    }
    if (selectedPartName.get() != null && !selectedPartName.get().equals("")) {
      if (filter != null) {
        filter = or(filter, regex("partName", selectedPartName.get()));
      } else {
        filter = regex("partName", selectedPartName.get());
      }
    }
    if (
      !compatibleEquipmentName.get().equals("") &&
      compatibleEquipmentStrings.contains(compatibleEquipmentName.get())
    ) {
      if (filter != null) {
        filter =
          or(
            filter,
            in(
              "compatibleEquipment",
              modelNameToID.get(compatibleEquipmentName.get())
            )
          );
      } else {
        filter =
          in(
            "compatibleEquipment",
            modelNameToID.get(compatibleEquipmentName.get())
          );
      }
    }
    if (filter == null) {
      filter = (ne("_id", "null"));
    }
    Inventoryfilter.set(filter);

    App.removePopup();
  }

  public void clearFilter() {
    selectedPartNumber.set("");
    selectedPartName.set("");
    compatibleEquipmentName.set("");
  }

  private void loadCompatibleEquipment() {
    modelNameToID = DatabaseConnection.equipmentCollection.getNameObjectMap();
    compatibleEquipmentStrings = new ArrayList<>(modelNameToID.keySet());
    compatibleEquipment.addAll(compatibleEquipmentStrings);
  }
}
