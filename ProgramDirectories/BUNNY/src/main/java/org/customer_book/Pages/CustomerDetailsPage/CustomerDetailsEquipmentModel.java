package org.customer_book.Pages.CustomerDetailsPage;

import lombok.Getter;
import lombok.Setter;

import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.MachinesCollection.MachineDAO;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Getter@Setter
public class CustomerDetailsEquipmentModel {

    private StringProperty EquipmentName = new SimpleStringProperty("");
    private StringProperty EquipmentNotes = new SimpleStringProperty("");
    private StringProperty MachineNotes = new SimpleStringProperty("");
    
    private MachineDAO machine;
    private EquipmentDAO equipment;

    public void setMachine(MachineDAO machine) {
        this.machine = machine;
        Platform.runLater(()-> {
           this.equipment = DatabaseConnection.equipmentCollection.getEquipment(machine.getEquipmentId());
           EquipmentName.setValue(equipment.getModelNumber());
           EquipmentNotes.setValue(equipment.getNotes());
           MachineNotes.setValue(machine.getNotes());
        });
    }
    
    
}
