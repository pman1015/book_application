package org.customer_book.Pages.CustomerEquipmentPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;

public class CustomerEquipmentPageController {
  @FXML
  private AnchorPane ArchiveWarning;
  @FXML
  private Text ArchiveMessageHeding;
  @FXML
  private Text ArchiveMessageText;
  
  @FXML
  private TextArea ModelNotes;

  @FXML
  private Label LastWorkedOnLabel;

  @FXML
  private ListView<Parent> CompatiblePartList;

  @FXML
  private TextArea MachineNotes;

  @FXML
  private ListView<Parent> ReplacedPartList;

  @FXML
  private Label SelectedModelNameLabel;

  @FXML
  private Label customerNameLabel;

  @FXML
  private ListView<Parent> CustomersEquipmentList;

  @FXML
  private Button mostRecentJobButton;

  @FXML
  private Button ArchiveMachineButton;

  @FXML
  private VBox MachineDetails;

  @FXML
  void showArchiveWarning(){
    model.showArchiveWarning();
  }
  @FXML
  void hideArchive(){
    model.hideArchiveWaring();
  }
  @FXML
  void archiveSelectedMachine(){
    model.archiveSelectedMachine();
  }

  @FXML
  void NavigateBack(ActionEvent event) {
    App.useBackPointer();
  }

  @FXML
  void showAddMachine() {
    model.showAddMachine();
  }

  @FXML
  void closeSelectedMachine(ActionEvent event) {
    model.closeSelectedMachine();
  }

  @FXML
  void goToMostRecentJob(ActionEvent event) {
    model.goToMostRecentJob();
  }

 

  private CustomerEquipmentPageModel model;

  @FXML
  void initialize() {
    model = new CustomerEquipmentPageModel();
    customerNameLabel.textProperty().bind(model.getCustomerName());

    //----Bind the lists to the model so the model controls the content
    CustomersEquipmentList.setItems(model.getCustomerEquipmentList());
    CompatiblePartList.setItems(model.getCompatiblePartList());
    ReplacedPartList.setItems(model.getReplacedPartList());

    //--- Bind the visibility of details and archive button to the model
    MachineDetails.visibleProperty().bind(model.getHasSelectedMachine());
    ArchiveMachineButton.visibleProperty().bind(model.getHasSelectedMachine());

    //---Bind visibility of the archive warning to the model
    ArchiveWarning.visibleProperty().bind(model.getShowArchiveWarning());
    ArchiveMessageHeding.textProperty().bind(model.getArchivePopupHeading());
    ArchiveMessageText.textProperty().bind(model.getArchivePopupMessage());
    ArchiveMachineButton.textProperty().bind(model.getArchiveButtonMessageProperty());

    //--- Bind the selected machine details to the model
    SelectedModelNameLabel
      .textProperty()
      .bind(model.getSelectedMachineModelNumberProperty());
    LastWorkedOnLabel
      .textProperty()
      .bind(model.getSelectedMachineLastWorkedOnProperty());
    MachineNotes
      .textProperty()
      .bindBidirectional(model.getSelectedMachineNotesProperty());
    ModelNotes
      .textProperty()
      .bindBidirectional(model.getSelectedMachineModelNotesProperty());
    mostRecentJobButton
      .textProperty()
      .bind(model.getSelectedMachineMostRecentJobNameProperty());
    
    //------------ Bind the edit properties of the text areas ---------//
    MachineNotes.focusedProperty().addListener((observable, oldValue, newValue) -> {
     model.getSelectedMachineNotesEdit().set(newValue);
    });
    ModelNotes.focusedProperty().addListener((observable, oldValue, newValue) -> {
     model.getSelectedMachineModelNotesEdit().set(newValue);
    });

    // ------------------ Default load the customer from the scene property ----//
    if(model.getCustomer() == null){
      model.setCustomer((CustomerDAO) App.getSceneProperty("customerDAO"));
      App.setBackPointer("CustomerDetailsPage", "CustomerDetailsPage");
    }
  }

  public void setCustomer(CustomerDAO customer) {
    model.setCustomer(customer);
  }

  public void setReturnDestination(String[] returnDestination) {
    model.setReturnDestination(returnDestination);
  }
}
