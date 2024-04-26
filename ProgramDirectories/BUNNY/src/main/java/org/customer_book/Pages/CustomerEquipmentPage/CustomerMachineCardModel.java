package org.customer_book.Pages.CustomerEquipmentPage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.MachinesCollection.MachineDAO;

@Getter
@Setter
@NoArgsConstructor
public class CustomerMachineCardModel {

  private StringProperty modelNumber = new SimpleStringProperty();
  private ObjectProperty<MachineDAO> selectedMachine;
  private MachineDAO machine;

  public void setMachine(MachineDAO machine) {
    this.machine = machine;
    this.modelNumber.set(
        DatabaseConnection.equipmentCollection
          .getEquipment(machine.getEquipmentId())
          .getModelNumber()
      );
  }
  public void setSelfSelected(){
    if(selectedMachine != null && machine != null){
      selectedMachine.set(machine);
    }
  }
}
