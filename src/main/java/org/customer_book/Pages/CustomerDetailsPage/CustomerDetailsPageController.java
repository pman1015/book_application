package org.customer_book.Pages.CustomerDetailsPage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;

public class CustomerDetailsPageController {

  @FXML
  private AnchorPane Content;

  @FXML
  private Text NickNameError;

  @FXML
  private TextArea JobNotes;

  @FXML
  private AnchorPane AddressEditField;

  @FXML
  private AnchorPane CustomerNameEditField;

  @FXML
  private AnchorPane PhoneNumberEditField;

  @FXML
  private AnchorPane PhoneNumberLabelField;

  @FXML
  private Label Address;

  @FXML
  private TextField PhoneNumberField;

  @FXML
  private TextField CustomerRatingField;

  @FXML
  private Label CustomerRating;

  @FXML
  private Text AddressError;

  @FXML
  private Label JobName;

  @FXML
  private AnchorPane NickNameEditField;

  @FXML
  private TextArea CustomerNotes;

  @FXML
  private Label CurrentJobHours;

  @FXML
  private AnchorPane editButtonContainerNotes;

  @FXML
  private AnchorPane AddressrLabelField;

  @FXML
  private Label CurrentJobCost;

  @FXML
  private ListView<?> equipmentCards;

  @FXML
  private Label CustomerName;

  @FXML
  private AnchorPane NickNameLabelField;

  @FXML
  private AnchorPane CustomerRatingEditField;

  @FXML
  private AnchorPane editButtonContainerDetails;

  @FXML
  private AnchorPane editOptionsDetails;

  @FXML
  private TextField AddressField;

  @FXML
  private Text CustomerRatingError;

  @FXML
  private TextField NickNameField;

  @FXML
  private Text PhoneNumberError;

  @FXML
  private Label CurrentJobEquipmentName;

  @FXML
  private Label NickName;

  @FXML
  private AnchorPane CustomerRatingLabelField;

  @FXML
  private Circle jobStatusIndicator;

  @FXML
  private AnchorPane editOptionsNotes;

  @FXML
  private AnchorPane CustomerNameLabelField;

  @FXML
  private TextField CustomerNameField;

  @FXML
  private Label PhoneNumber;

  @FXML
  private Text CustomerNameError;

  @FXML
  void loadCustomerEquipment(ActionEvent event) {
    model.showCustomerEquipment();
  }

  @FXML
  void loadJobHistory(ActionEvent event) {
    model.showJobHistory();
  }

  @FXML
  void loadRecords(ActionEvent event) {}

  @FXML
  void cancelEditDetails(ActionEvent event) {
  
    model.setCustomerDetailsEditProperty(false);
    editButtonContainerDetails.toFront();
  }

  @FXML
  void saveEditDetails(ActionEvent event) {
    if (
      model.validChanges(
        CustomerNameField.getText(),
        NickNameField.getText(),
        PhoneNumberField.getText(),
        AddressField.getText(),
        Integer.parseInt(CustomerRatingField.getText())
      )
    ) {
      model.saveChanges();
      model.setCustomerDetailsEditProperty(false);
      editButtonContainerDetails.toFront();
    }
  }

  @FXML
  void enableEditDetails(ActionEvent event) {
    model.setCustomerDetailsEditProperty(true);
    editButtonContainerDetails.toBack();
  }

  @FXML
  void cancelEditNotes(ActionEvent event) {
    model.setCustomerNotesEditProperty(false);
    CustomerNotes.setText(customerDAO.getCustomerNotes().getValue());
    editButtonContainerNotes.toFront();
  }

  @FXML
  void saveEditNotes(ActionEvent event) {
    model.setCustomerNotesEditProperty(false);
    customerDAO.setNotes(CustomerNotes.getText());
    model.saveChanges();
    editButtonContainerNotes.toFront();
  }

  @FXML
  void enableEditNotes(ActionEvent event) {
    model.setCustomerNotesEditProperty(true);
    editButtonContainerNotes.toBack();
  }

  @FXML
  void showAddNewJob(ActionEvent event) {
    model.showAddNewJob();
  }
  @FXML
  void loadMostRecentJob(){
    model.goToMostRecentJob();
  }

  private CustomerDAO customerDAO;

  private CustomerDetailsPageModel model;

  @FXML
  void initialize() {
    model = new CustomerDetailsPageModel();
    //----Add an event listener so if page is displayed before the customerDAO is set then
    //Load the doa from scene properties
    Content
      .sceneProperty()
      .addListener(
        (ChangeListener) (observable, oldValue, newValue) -> {
          if (newValue != null) {
            if (
              App.getSceneProperty("customerDAO") != null &&
              model.getCustomer() == null
            ) {
              setCustomerDAO((CustomerDAO) App.getSceneProperty("customerDAO"));
            }
          }
        }
      );

    //--------------------------------------------------------------------------------
    //Bind the states of the stackPanes to the boolean values in the model
    //--------------------------------------------------------------------------------
    editOptionsDetails
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty());
    editButtonContainerDetails
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty().not());
    editButtonContainerDetails.toFront();

    editOptionsNotes
      .visibleProperty()
      .bind(model.getCustomerNotesEditProperty());
    editButtonContainerNotes
      .visibleProperty()
      .bind(model.getCustomerNotesEditProperty().not());
    editButtonContainerNotes.toFront();

    //--------------------------------------------------------------------------------
    //Bind the Editablitiy of the notes field to the edit Mode
    //--------------------------------------------------------------------------------
    CustomerNotes.editableProperty().bind(model.getCustomerNotesEditProperty());

    //--------------------------------------------------------------------------------
    //Bind the visibility of fields
    //--------------------------------------------------------------------------------
    CustomerNameEditField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty());
    CustomerNameLabelField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty().not());

    NickNameEditField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty());
    NickNameLabelField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty().not());

    PhoneNumberEditField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty());
    PhoneNumberLabelField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty().not());

    AddressEditField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty());
    AddressrLabelField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty().not());

    CustomerRatingEditField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty());
    CustomerRatingLabelField
      .visibleProperty()
      .bind(model.getCustomerDetailsEditProperty().not());
  }

  //--------------------------------------------------------------------------------
  // Set the customerDAO and preforme actions to update the fields for the page
  //--------------------------------------------------------------------------------
  public void setCustomerDAO(CustomerDAO customerDAO) {
    this.customerDAO = customerDAO;
    model.setCustomer(customerDAO);
    App.setSceneProperty("customerDAO", customerDAO);
    ExecutorService executor = Executors.newFixedThreadPool(3);

    executor.submit(() -> {
      Platform.runLater(() -> {
        updateFields(customerDAO);
      });
    });
    executor.submit(() -> {
      Platform.runLater(() -> {
        setActiveJob();
      });
    });
    executor.shutdown();
  }

  //--------------------------------------------------------------------------------
  //Load the active Jobs in the model and bind the active job info to
  //the fields on the most recent job card
  //--------------------------------------------------------------------------------
  private void setActiveJob() {
    model.loadJobs();
    System.out.println("Active Job: " + model.getSelectedJob());
    if (model.getSelectedJob() == null) {
      return;
    }
    JobName.textProperty().bind(model.getSelectedJob().getJobNameProperty());
    CurrentJobEquipmentName
      .textProperty()
      .bind(model.getSelectedJob().getEquipmentNameProperty());
    CurrentJobCost
      .textProperty()
      .bind(model.getSelectedJob().getCurrentCostProperty());
    CurrentJobHours
      .textProperty()
      .bind(model.getSelectedJob().getCurrentHoursProperty());
    JobNotes.textProperty().bind(model.getSelectedJob().getJobNotesProperty());
    jobStatusIndicator
      .fillProperty()
      .bind(model.getSelectedJob().getJobStatusProperty());
  }

  //--------------------------------------------------------------------------------
  //Update the customer detail fields of the page to that of the DAO
  //--------------------------------------------------------------------------------
  public void updateFields(CustomerDAO dao) {
    //--------------------------------------------------------------------------------
    // Bind the customer details to the fields
    //--------------------------------------------------------------------------------
    CustomerName.textProperty().bind(customerDAO.getCustomerName());
    CustomerNameField.setText(customerDAO.getCustomerName().getValue());
    CustomerNameError.textProperty().bind(this.model.getCustomerNameError());

    Address.textProperty().bind(customerDAO.getCustomerAddress());
    AddressField.setText(customerDAO.getCustomerAddress().getValue());
    AddressError.textProperty().bind(model.getAddressError());

    PhoneNumber.textProperty().bind(customerDAO.getCustomerPhoneNumber());
    PhoneNumberField.setText(customerDAO.getCustomerPhoneNumber().getValue());
    PhoneNumberError.textProperty().bind(model.getPhoneNumberError());

    NickName.textProperty().bind(customerDAO.getCustomerNickName());
    NickNameField.setText(customerDAO.getCustomerNickName().getValue());
    NickNameError.textProperty().bind(model.getNickNameError());

    CustomerRating.textProperty().bind(customerDAO.getCustomerRating());
    CustomerRatingField.setText(
      customerDAO.getCustomerRating().getValue().substring(0, 1)
    );
    CustomerRatingError.textProperty().bind(model.getCustomerRatingError());
    //--------------------------------------------------------------------------------
    // bind the Notes to the NotesField
    //--------------------------------------------------------------------------------
    CustomerNotes.setText(customerDAO.getCustomerNotes().getValue());
  }
}
