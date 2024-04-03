package org.customer_book.Pages.CustomerDetailsPage;

import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Pages.CustomerEquipmentPage.CustomerEquipmentPageController;
import org.customer_book.Popups.JobCreate.JobCreateController;

@Getter
@Setter
public class CustomerDetailsPageModel {

  //-----------------Customer Details Page Modes-----------------//
  private SimpleBooleanProperty customerDetailsEditProperty;
  private SimpleBooleanProperty customerNotesEditProperty;
  //-----------------Customer Details Error Messages-----------------//
  private StringProperty customerRatingError = new SimpleStringProperty("");
  private StringProperty customerNameError = new SimpleStringProperty("");
  private StringProperty addressError = new SimpleStringProperty("");
  private StringProperty nickNameError = new SimpleStringProperty("");
  private StringProperty phoneNumberError = new SimpleStringProperty("");
  //-----------------Customer Details-----------------//
  private CustomerDAO customer;
  private ArrayList<JobDAO> jobs;
  private JobDAO selectedJob;

  public CustomerDetailsPageModel() {
    customerDetailsEditProperty = new SimpleBooleanProperty(false);
    customerNotesEditProperty = new SimpleBooleanProperty(false);
  }

  public void setCustomerDetailsEditProperty(boolean value) {
    customerDetailsEditProperty.setValue(value);
  }

  public void setCustomerNotesEditProperty(boolean value) {
    customerNotesEditProperty.setValue(value);
  }

  public void loadJobs() {
    if (customer == null) return;
    jobs = DatabaseConnection.jobCollection.getDAOs(customer.getJobIDs());
    jobs.sort((a, b) -> b.getStartDate().compareTo(a.getStartDate()));
    if (jobs.size() > 0) {
      selectedJob = jobs.get(0);
      selectedJob.initializeFXProperties();
    }
  }

  public void saveChanges() {
    DatabaseConnection.customerCollection.updateCustomer(customer);
  }

  public boolean validChanges(
    String customerName,
    String nickName,
    String phoneNumber,
    String address,
    int rating
  ) {
    boolean valid = true;
    if (customerName == null || customerName.isEmpty()) {
      customerNameError.setValue("Customer Name is required");
      valid = false;
    } else {
      customerNameError.setValue("");
      customer.setName(customerName);
    }
    if (nickName == null || nickName.isEmpty()) {
      nickNameError.setValue("Nick Name is required");
      valid = false;
    } else {
      nickNameError.setValue("");
      customer.setAlias(nickName);
    }
    if (phoneNumber == null || phoneNumber.isEmpty()) {
      phoneNumberError.setValue("Phone Number is required");
      valid = false;
    } else if (!checkPhoneNumber(phoneNumber)) {
      phoneNumberError.setValue("Phone Number is not valid");
      valid = false;
    } else {
      phoneNumberError.setValue("");
      customer.setPhoneNumber(phoneNumber);
    }
    if (address == null || address.isEmpty()) {
      addressError.setValue("Address is required");
      valid = false;
    } else {
      addressError.setValue("");
      customer.setAddress(address);
    }
    if (rating < 0 || rating > 5) {
      customerRatingError.setValue("Rating must be between 0 and 5");
      valid = false;
    } else {
      customerRatingError.setValue("");
      customer.setRating(rating);
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
      loadedPage.sceneProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue == null) {
            loadJobs();
          }
        }
      );
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
    App.setSceneProperty("customerDAP", customer);
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
}
