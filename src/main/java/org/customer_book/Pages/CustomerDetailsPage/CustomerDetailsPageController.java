package org.customer_book.Pages.CustomerDetailsPage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
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
  private ListView<Parent> equipmentCards;

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
    model.cancelCustomerDetailsEdit();
    editButtonContainerDetails.toFront();
  }

  @FXML
  void saveEditDetails(ActionEvent event) {
    model.saveCustomerDetailsEdit();
  }

  @FXML
  void enableEditDetails(ActionEvent event) {
    model.showEditCustomerDetails();
    editButtonContainerDetails.toBack();
  }

  @FXML
  void cancelEditNotes(ActionEvent event) {
    model.cancelCustomerNotesEdit();
    editButtonContainerNotes.toFront();
  }

  @FXML
  void saveEditNotes(ActionEvent event) {
    model.saveCustomerNotesEdit();
    editButtonContainerNotes.toFront();
  }

  @FXML
  void enableEditNotes(ActionEvent event) {
    model.showCustomerNotesEdit();
    editButtonContainerNotes.toBack();
  }

  @FXML
  void showAddNewJob(ActionEvent event) {
    model.showAddNewJob();
  }

  @FXML
  void loadMostRecentJob() {
    model.goToMostRecentJob();
  }

  private CustomerDetailsPageModel model;

  @FXML
  void initialize() {
    model = new CustomerDetailsPageModel();
    CustomerDAO customerDAO = (CustomerDAO) App.getSceneProperty("customerDAO");
    if (customerDAO != null) {
      model.setCustomer(customerDAO);
    }
    //-----------------Bind the model to the view-----------------//

    CompletableFuture<Void> bindEditProperties = CompletableFuture.runAsync(
      this::bindEditProperties
    );
    CompletableFuture<Void> bindCustomerDetails = CompletableFuture.runAsync(
      this::bindCustomerDetails
    );
    CompletableFuture<Void> bindMostRecentJob = CompletableFuture.runAsync(
      this::bindMostRecentJob
    );
    CompletableFuture<Void> bindFields = CompletableFuture.runAsync(
      this::bindFields
    );
    CompletableFuture<Void> bindValueProperties = CompletableFuture.runAsync(
      this::bindValueProperties
    );
    CompletableFuture<Void> bindErrors = CompletableFuture.runAsync(
      this::bindErrors
    );

    CompletableFuture
      .allOf(
        bindEditProperties,
        bindCustomerDetails,
        bindMostRecentJob,
        bindFields,
        bindValueProperties,
        bindErrors
      )
      .join();

      //Bind the edit properties to the model to insure the update applies
      model.getCustomerDetailsEditProperty().addListener((observable, oldValue, newValue) -> {
       bindEditProperties();
       bindCustomerDetails();
      });
      model.getCustomerNotesEditProperty().addListener((observable, oldValue, newValue) -> {
       bindEditProperties();
       bindCustomerDetails();
      });
  }

  //--------------------------------------------------------------------------------
  //Bind the most recentJobs
  //--------------------------------------------------------------------------------
  private void bindMostRecentJob() {
    JobName.textProperty().bind(model.getMostRecentJobNameProperty());
    jobStatusIndicator
      .fillProperty()
      .bind(model.getMostRecentJobStatusProperty());
    CurrentJobEquipmentName
      .textProperty()
      .bind(model.getMostRecentJobEquipmentNameProperty());
    CurrentJobCost
      .textProperty()
      .bind(model.getMostRecentJobCurrentCostProperty());
    CurrentJobHours
      .textProperty()
      .bind(model.getMostRecentJobCurrentHourProperty());
    JobNotes.textProperty().bind(model.getMostRecentJobNotesProperty());
  }

  //--------------------------------------------------------------------------------
  //Bind the fields to the model
  //--------------------------------------------------------------------------------
  private void bindFields() {
    CustomerNameField
      .textProperty()
      .bindBidirectional(model.getCustomerNameProperty());
    NickNameField
      .textProperty()
      .bindBidirectional(model.getCustomerNickNameProperty());
    PhoneNumberField
      .textProperty()
      .bindBidirectional(model.getCustomerPhoneNumberProperty());
    AddressField
      .textProperty()
      .bindBidirectional(model.getCustomerAddressProperty());
    CustomerRatingField
      .textProperty()
      .bindBidirectional(model.getCustomerRatingProperty());
  }

  //--------------------------------------------------------------------------------
  //Bind the values of the fields to the model
  //--------------------------------------------------------------------------------
  private void bindValueProperties() {
    CustomerName
      .textProperty()
      .bindBidirectional(model.getCustomerNameProperty());
    NickName
      .textProperty()
      .bindBidirectional(model.getCustomerNickNameProperty());
    PhoneNumber
      .textProperty()
      .bindBidirectional(model.getCustomerPhoneNumberProperty());
    Address
      .textProperty()
      .bindBidirectional(model.getCustomerAddressProperty());
    CustomerRating
      .textProperty()
      .bindBidirectional(model.getCustomerRatingProperty());
    CustomerNotes
      .textProperty()
      .bindBidirectional(model.getCustomerNotesProperty());
    equipmentCards.setItems(model.getEquipmentCards());
  }

  //--------------------------------------------------------------------------------
  //Bind the states of the stackPanes to the boolean values in the model
  //--------------------------------------------------------------------------------
  private void bindEditProperties() {
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
  }

  //--------------------------------------------------------------------------------
  //Bind the visibility of fields
  //--------------------------------------------------------------------------------
  private void bindCustomerDetails() {
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
  //bind Errors
  //--------------------------------------------------------------------------------
  private void bindErrors() {
    CustomerNameError.textProperty().bind(model.getCustomerNameError());
    NickNameError.textProperty().bind(model.getNickNameError());
    PhoneNumberError.textProperty().bind(model.getPhoneNumberError());
    AddressError.textProperty().bind(model.getAddressError());
    CustomerRatingError.textProperty().bind(model.getCustomerRatingError());
  }

  //--------------------------------------------------------------------------------
  // Set the customerDAO and preforme actions to update the fields for the page
  //--------------------------------------------------------------------------------
  public void setCustomerDAO(CustomerDAO customerDAO) {
    model.setCustomer(customerDAO);
    App.setSceneProperty("customerDAO", customerDAO);
  }
}
