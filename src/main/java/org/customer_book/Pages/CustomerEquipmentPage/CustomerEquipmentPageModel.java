package org.customer_book.Pages.CustomerEquipmentPage;

import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.MachinesCollection.MachineDAO;
import org.customer_book.Popups.AddEquipmentToUser.AddEquipmentToUserController;

@Getter
@Setter
public class CustomerEquipmentPageModel {

  private ObservableList<Parent> customerEquipmentList;
  private ObservableList<Parent> compatiblePartList;
  private ObservableList<Parent> replacedPartList;

  private BooleanProperty hasSelectedMachine;

  private StringProperty customerName;

  private CustomerDAO customer;
  private ObjectProperty<MachineDAO> selectedMachine;

  private StringProperty selectedMachineModelNumberProperty;
  private StringProperty selectedMachineLastWorkedOnProperty;
  private StringProperty selectedMachineMostRecentJobNameProperty;
  private StringProperty selectedMachineNotesProperty;
  private StringProperty selectedMachineModelNotesProperty;
  private EquipmentDAO selectedMachineEquipment;

  //An Array of size 2, the first element is the name of the page to return to, the second element is the name of the page to return to if the user is not an admin
  private String[] returnDestination;

  public CustomerEquipmentPageModel() {
    hasSelectedMachine = new SimpleBooleanProperty(false);

    customerEquipmentList = FXCollections.observableArrayList();
    compatiblePartList = FXCollections.observableArrayList();
    replacedPartList = FXCollections.observableArrayList();

    selectedMachineModelNumberProperty = new SimpleStringProperty("");
    selectedMachineLastWorkedOnProperty = new SimpleStringProperty("");
    selectedMachineMostRecentJobNameProperty = new SimpleStringProperty("");
    selectedMachineNotesProperty = new SimpleStringProperty("");
    selectedMachineModelNotesProperty = new SimpleStringProperty("");
    
    customerName = new SimpleStringProperty("");
    selectedMachine = new SimpleObjectProperty<>();
    selectedMachine.addListener((observable, oldValue, newValue) -> {
      hasSelectedMachine.set(newValue != null);
      if (newValue != null) {
        //Set the properties of the selected machine
        selectedMachineLastWorkedOnProperty.set(newValue.getLastWorkedOn());
        selectedMachineNotesProperty.set(newValue.getNotes());
        selectedMachineEquipment = DatabaseConnection.equipmentCollection.getEquipment(newValue.getEquipmentId());
        if(selectedMachineEquipment != null){
          selectedMachineModelNumberProperty.set(selectedMachineEquipment.getModelNumber());
          selectedMachineModelNotesProperty.set(selectedMachineEquipment.getNotes());
        }
      }
    });
  }

  public void setCustomer(CustomerDAO customer) {
    this.customer = customer;
    customerName.set(customer.getName());
    loadCustomerEquipment();
  }

  public void goBack() {
    if (returnDestination.length == 2) {
      try {
        App.setPage(returnDestination[0], returnDestination[1]);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void loadCustomerEquipment() {
    customerEquipmentList.clear();
    DatabaseConnection.machineCollection
      .getMachinesbyIDs(customer.getMachineIDs())
      .forEach(machine -> {
        try {
          FXMLLoader loader = App.getLoader(
            "CustomerEquipmentPage",
            "CustomerMachineCard"
          );
          Parent machineCard = loader.load();
          (
            (CustomerMachineCardController) loader.getController()
          ).setMachineAndSelectedMachine(machine, selectedMachine);
          customerEquipmentList.add(machineCard);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
  }

  public void reloadCustomer() {
    customer =
      DatabaseConnection.customerCollection.findCustomerById(customer.getId());
    App.setSceneProperty("customerDAO", customer);
  }

  public void showAddMachine() {
    try {
      FXMLLoader loader = App.getLoader("Popups", "AddCustomerMachine");
      Parent popup = loader.load();
      ((AddEquipmentToUserController) loader.getController()).setCustomer(
          customer
        );
      popup
        .sceneProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue == null) {
            reloadCustomer();
            loadCustomerEquipment();
          }
        });
      App.addPopup(popup);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

public void closeSelectedMachine() {
    selectedMachine.set(null);
}
}
