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
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Database.MachinesCollection.MachineDAO;
import org.customer_book.Database.MachinesCollection.MachineWorkDAO;
import org.customer_book.Popups.AddEquipmentToUser.AddEquipmentToUserController;

@Getter
@Setter
public class CustomerEquipmentPageModel {

  //--------------- Page Properties ------------------//
  private BooleanProperty hasSelectedMachine = new SimpleBooleanProperty(false);
  private StringProperty customerName = new SimpleStringProperty("");
  private ObjectProperty<MachineDAO> selectedMachine = new SimpleObjectProperty<>();
  private EquipmentDAO selectedMachineEquipment;
  private JobDAO mostRecentJob;
  private CustomerDAO customer;

  //-----------------Equipment List Property ----------------//
  private ObservableList<Parent> customerEquipmentList = FXCollections.observableArrayList();

  //-----------------Selected Machine Properties----------------//
  private ObservableList<Parent> compatiblePartList = FXCollections.observableArrayList();
  private ObservableList<Parent> replacedPartList = FXCollections.observableArrayList();

  private StringProperty selectedMachineModelNumberProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedMachineLastWorkedOnProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedMachineMostRecentJobNameProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedMachineNotesProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedMachineModelNotesProperty = new SimpleStringProperty(
    ""
  );
  private BooleanProperty selectedMachineNotesEdit = new SimpleBooleanProperty(
    false
  );
  private BooleanProperty selectedMachineModelNotesEdit = new SimpleBooleanProperty(
    false
  );

  //An Array of size 2, the first element is the name of the page to return to, the second element is the name of the page to return to if the user is not an admin
  private String[] returnDestination;

  public CustomerEquipmentPageModel() {
    //--------------- Add Change listner for the selected Machine ------------------//
    selectedMachine.addListener((observable, oldValue, newValue) -> {
      hasSelectedMachine.set(newValue != null);
      updateSelectedMachineInfo();
    });

    //--------------- Add Change Listner for the machine notes ------------------//
    selectedMachineNotesEdit.addListener((observable, oldValue, newValue) -> {
      if (selectedMachine.get() != null) {
        updateMachineNotes();
      }
    });

    //--------------- Add Change Listner for the model notes ------------------//
    selectedMachineModelNotesEdit.addListener(
      (observable, oldValue, newValue) -> {
        if (
          selectedMachineEquipment != null &&
          !selectedMachineModelNotesProperty
            .get()
            .equals(selectedMachineEquipment.getNotes())
        ) {
          selectedMachineEquipment.setNotes(
            selectedMachineModelNotesProperty.get()
          );
          DatabaseConnection.equipmentCollection.updateEquipment(
            selectedMachineEquipment
          );
        }
      }
    );
  }

  private void updateMachineNotes() {
    if (
      selectedMachineNotesProperty.get() != null &&
      !selectedMachineNotesProperty
        .get()
        .equals(selectedMachine.get().getNotes())
    ) {
      selectedMachine.get().setNotes(selectedMachineNotesProperty.get());
      DatabaseConnection.machineCollection.updateMachineNotes(
        selectedMachine.get()
      );
    }
  }

  private void updateMachineHistory() {
    replacedPartList.clear();
    if (selectedMachine.get() == null) return;

    if (selectedMachine.get().getWorkHistory() != null) {
      for (MachineWorkDAO job : selectedMachine.get().getWorkHistory()) {
        try {
          FXMLLoader workCardLoader = App.getLoader(
            "CustomerEquipmentPage",
            "WorkHistoryCard"
          );
          Parent workCard = workCardLoader.load();
          (
            (EquipmentWorkHistoryController) workCardLoader.getController()
          ).setMachineWork(job);
          replacedPartList.add(workCard);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void updateSelectedMachineInfo() {
    if (selectedMachine.get() != null) {
      updateMachineHistory();
      setMostRecentJob();
      selectedMachineLastWorkedOnProperty.set(
        selectedMachine.get().getLastWorkedOn()
      );
      selectedMachineNotesProperty.set(selectedMachine.get().getNotes());
      selectedMachineEquipment =
        DatabaseConnection.equipmentCollection.getEquipment(
          selectedMachine.get().getEquipmentId()
        );
      if (selectedMachineEquipment != null) {
        selectedMachineModelNumberProperty.set(
          selectedMachineEquipment.getModelNumber()
        );
        selectedMachineModelNotesProperty.set(
          selectedMachineEquipment.getNotes()
        );
        loadCompatibleParts();
      }
    }
  }

  private void loadCompatibleParts() {
    compatiblePartList.clear();
    DatabaseConnection.inventoryCollection
      .getSelectParts(selectedMachineEquipment.getParts())
      .forEach(part -> {
        try {
          FXMLLoader loader = App.getLoader(
            "CustomerEquipmentPage",
            "CompatiblePartCard"
          );
          Parent partCard = loader.load();
          ((CompatiblePartCardController) loader.getController()).setPartDAO(
              part
            );
          compatiblePartList.add(partCard);
        } catch (IOException e) {
          e.printStackTrace();
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

  public void setMostRecentJob() {
    if (
      selectedMachine.get() != null &&
      selectedMachine.get().getWorkHistory() != null &&
      selectedMachine.get().getWorkHistory().size() > 0
    ) {
      ObjectId mostRecentJobId = selectedMachine
        .get()
        .getWorkHistory()
        .get(selectedMachine.get().getWorkHistory().size() - 1)
        .getJobId();
      this.mostRecentJob =
        DatabaseConnection.jobCollection.getDAO(mostRecentJobId);
      if (mostRecentJob == null) {
        System.out.println("Job not found");
        return;
      }
      selectedMachineMostRecentJobNameProperty.set(mostRecentJob.getJobName());
    }else{
      mostRecentJob = null;
      selectedMachineMostRecentJobNameProperty.set("No Jobs Found");

    }
  }

  public void goToMostRecentJob() {
    if(mostRecentJob == null)return;
    try {
      App.setSceneProperty("jobDAO", mostRecentJob);
      App.setBackPointer("CustomerEquipmentPage", "CustomerEquipmentPage");
      App.setPage("CustomerJobDetailsPage", "CustomerJobDetailsPage");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
