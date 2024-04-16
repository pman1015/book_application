package org.customer_book.Pages.CustomerDetailsPage;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Database.MachinesCollection.MachineDAO;
import org.customer_book.Pages.CustomerEquipmentPage.CustomerEquipmentPageController;
import org.customer_book.Popups.JobCreate.JobCreateController;

@Getter
@Setter
public class CustomerDetailsPageModel {

  //-----------------Customer Details Page Modes-----------------//
  private SimpleBooleanProperty customerDetailsEditProperty = new SimpleBooleanProperty(
    false
  );
  private SimpleBooleanProperty customerNotesEditProperty = new SimpleBooleanProperty(
    false
  );

  //----------------- Customer Details values -----------------//
  private StringProperty customerNameProperty = new SimpleStringProperty("");
  private StringProperty customerNickNameProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty customerPhoneNumberProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty customerAddressProperty = new SimpleStringProperty("");
  private StringProperty customerRatingProperty = new SimpleStringProperty("");
  private StringProperty customerNotesProperty = new SimpleStringProperty("");

  //-----------------Customer Details Error Messages-----------------//
  private StringProperty customerRatingError = new SimpleStringProperty("");
  private StringProperty customerNameError = new SimpleStringProperty("");
  private StringProperty addressError = new SimpleStringProperty("");
  private StringProperty nickNameError = new SimpleStringProperty("");
  private StringProperty phoneNumberError = new SimpleStringProperty("");

  //-----------------Most Recent Job Details-----------------//
  private StringProperty mostRecentJobNameProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty mostRecentJobEquipmentNameProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty mostRecentJobCurrentCostProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty mostRecentJobCurrentHourProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty mostRecentJobNotesProperty = new SimpleStringProperty(
    ""
  );
  private ObjectProperty<Paint> mostRecentJobStatusProperty = new SimpleObjectProperty<>(
    Paint.valueOf("#00FF00")
  );

  //-----------------Customer Details-----------------//
  private CustomerDAO customer;
  private ArrayList<JobDAO> jobs;
  private JobDAO selectedJob;

  //----------------- Customer Equipment Cards ---------//
  private ObservableList<Parent> equipmentCards = FXCollections.observableArrayList();

  //Constructor
  public CustomerDetailsPageModel() {
    //Listners for notes edit
  }

  public void showCustomerNotesEdit(){
    customerNotesEditProperty.set(true);
  }
  public void cancelCustomerNotesEdit() {
    customerNotesEditProperty.set(false);
    customerNotesProperty.set(customer.getNotes());
  }

  public void saveCustomerNotesEdit() {
    customer.setNotes(customerNotesProperty.getValue());
    DatabaseConnection.customerCollection.updateCustomer(customer);
    customerNotesEditProperty.set(false);
  }

  public void saveCustomerDetailsEdit() {
    if (validChanges()) {
      saveChanges();
      customerDetailsEditProperty.set(false);
    }
  }

  public void cancelCustomerDetailsEdit() {
    updateCustomerDetails();
    customerDetailsEditProperty.set(false);
  }

  public void showEditCustomerDetails() {
    customerDetailsEditProperty.set(true);
  }

  public void setCustomer(CustomerDAO customer) {
    this.customer = customer;
    if(customer == null) return;

    updateCustomerDetails();
    loadEquipmentCards();
    updateMostRecentJobDetails();
  }

  private void updateMostRecentJobDetails() {
    this.selectedJob = customer.getMostRecentJob();
    if (selectedJob == null || selectedJob.getId() == null) return;
    mostRecentJobNameProperty.set(selectedJob.getJobName());
    mostRecentJobEquipmentNameProperty.set(selectedJob.getEquipmentName()
    );
    mostRecentJobCurrentCostProperty.set(
     "$" + selectedJob.getBill().getBillTotal()
    );
    mostRecentJobCurrentHourProperty.set(selectedJob.getBill().getTotalHours() + " Hours");
    
    mostRecentJobNotesProperty.set(selectedJob.getDetails());
    mostRecentJobStatusProperty.set(selectedJob.resolveColor(selectedJob.getStatus()));
  }

  private void updateCustomerDetails() {
    customerNameProperty.set(customer.getName());
    customerNickNameProperty.set(customer.getAlias());
    customerPhoneNumberProperty.set(customer.getPhoneNumber());
    customerAddressProperty.set(customer.getAddress());
    customerRatingProperty.set(Integer.toString(customer.getRating()));
    customerNotesProperty.set(customer.getNotes());
  }

  public void saveChanges() {
    DatabaseConnection.customerCollection.updateCustomer(customer);
  }

  public boolean validChanges() {
    boolean valid = true;
    if (
      customerNameProperty.get() == null ||
      customerNameProperty.get().equals("")
    ) {
      customerNameError.setValue("Customer Name is required");
      valid = false;
    } else {
      customerNameError.setValue("");
      customer.setName(customerNameProperty.get());
    }
    if (
      customerNickNameProperty.get() == null ||
      customerNickNameProperty.get().equals("")
    ) {
      nickNameError.setValue("Nick Name is required");
      valid = false;
    } else {
      nickNameError.setValue("");
      customer.setAlias(customerNickNameProperty.get());
    }
    if (
      customerPhoneNumberProperty.get() == null ||
      customerPhoneNumberProperty.get().equals("")
    ) {
      phoneNumberError.setValue("Phone Number is required");
      valid = false;
    } else if (!checkPhoneNumber(customerPhoneNumberProperty.get())) {
      phoneNumberError.setValue("Phone Number is not valid");
      valid = false;
    } else {
      phoneNumberError.setValue("");
      customer.setPhoneNumber(customerPhoneNumberProperty.get());
    }
    if (
      customerAddressProperty.get() == null ||
      customerAddressProperty.get().equals("")
    ) {
      addressError.setValue("Address is required");
      valid = false;
    } else {
      addressError.setValue("");
      customer.setAddress(customerAddressProperty.get());
    }
    if (
      Integer.valueOf(customerRatingProperty.get()) < 0 ||
      Integer.valueOf(customerRatingProperty.get()) > 5
    ) {
      customerRatingError.setValue("Rating must be between 0 and 5");
      valid = false;
    } else {
      customerRatingError.setValue("");
      customer.setRating(Integer.valueOf(customerRatingProperty.get()));
    }
    return valid;
  }

  private boolean checkPhoneNumber(String phoneNumber) {
    String regex = "^\\d{3}-\\d{3}-\\d{4}$";

    return phoneNumber.matches(regex);
  }

  public void showAddNewJob() {
    try {
      FXMLLoader jobCreateLoader = App.getLoader("Popups", "CreateJob");
      Parent loadedPage = jobCreateLoader.load();
      ((JobCreateController) jobCreateLoader.getController()).setCustomer(
          customer
        );
      App.addPopup(loadedPage);
      loadedPage
        .sceneProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue == null) {
            updateMostRecentJobDetails();
          }
        });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showCustomerEquipment() {
    try {
      FXMLLoader equipmentPageLoader = App.getLoader(
        "CustomerEquipmentPage",
        "CustomerEquipmentPage"
      );
      Parent loadedPage = equipmentPageLoader.load();
      (
        (CustomerEquipmentPageController) equipmentPageLoader.getController()
      ).setCustomer(customer);
      (
        (CustomerEquipmentPageController) equipmentPageLoader.getController()
      ).setReturnDestination(
          new String[] { "CustomerDetailsPage", "CustomerDetailsPage" }
        );
      App.setPage(loadedPage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showJobHistory() {
    App.setSceneProperty("customerDAO", customer);
    try {
      FXMLLoader jobHistoryLoader = App.getLoader(
        "CustomerJobsPage",
        "CustomerJobPage"
      );
      App.setBackPointer("CustomerDetailsPage", "CustomerDetailsPage");
      Parent loadedPage = jobHistoryLoader.load();
      App.setPage(loadedPage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void goToMostRecentJob() {
    if (selectedJob == null || selectedJob.getId() == null) return;
    App.setBackPointer("CustomerDetailsPage", "CustomerDetailsPage");
    App.setSceneProperty("customerDAO", customer);
    App.setSceneProperty("jobDAO", selectedJob);
    try {
      App.setPage("CustomerJobDetailsPage", "CustomerJobDetailsPage");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void loadEquipmentCards() {
    equipmentCards.clear();
    if (customer == null) return;
    DatabaseConnection.machineCollection
      .getMachinesbyIDs(customer.getMachineIDs())
      .forEach(machine -> {
        try {
          FXMLLoader equipmentCardLoader = App.getLoader(
            "CustomerDetailsPage",
            "CustomerDetailsEquipmentCard"
          );
          Parent loadedCard = equipmentCardLoader.load();
          (
            (CustomerDetailsEquipmentController) equipmentCardLoader.getController()
          ).setMachine(machine);
          equipmentCards.add(loadedCard);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
  }
}
