package org.customer_book.Pages.InventoryPage;

import org.customer_book.Database.EquipmentCollection.EquipmentDAO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class InventoryCompatibleMachineCardModel {
    private EquipmentDAO equipment;
    private StringProperty machineNameProperty;

    public InventoryCompatibleMachineCardModel() {
       machineNameProperty = new SimpleStringProperty("");
    }
    public void setEquipment(EquipmentDAO equipment) {
        this.equipment = equipment;
        machineNameProperty.set(equipment.getModelNumber());
    }
    
}
