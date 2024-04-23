package org.customer_book.Pages.JobDetailsPage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.customer_book.App;
import org.customer_book.Database.JobsCollection.JobDAO;

public class JobsDetailsPageController {

  @FXML
  private Label RateErrorLabel;

  @FXML
  private Button GoToInvoiceButton;

  @FXML
  private ChoiceBox<String> JobStatusSelect;

  @FXML
  private Label CreatedDateLabel;
  
  @FXML
  private Label MachineNameLabel;

  @FXML
  private Label JobNameLabel;

  @FXML
  private HBox JobNameEditContainer;

  @FXML
  private Label HoursErrorLabel;

  @FXML
  private TextField LaborChargeNameField;

  @FXML
  private TextField HoursField;

  @FXML
  private TextField DeliveryEditField;

  @FXML
  private Button addLaborChargeButton;

  @FXML
  private AnchorPane JobDetailsSettings;

  @FXML
  private Button JobStatus;

  @FXML
  private AnchorPane PopupContentField;

  @FXML
  private TextField defaultLaborNameField;

  @FXML
  private Label DeliveryChargeLabel;

  @FXML
  private Label LaborChargeNameErrorLabel;

  @FXML
  private AnchorPane AddAPartPane;

  @FXML
  private Label BillTotalLabel;

  @FXML
  private AnchorPane JobStatusPopup;

  @FXML
  private ListView<Parent> PartChargeList;

  @FXML
  private Label PartTotalLabel;

  @FXML
  private AnchorPane DeleteWarning;

  @FXML
  private Text AddLaborChargeHeading;

  @FXML
  private TextField JobNameField;

  @FXML
  private ListView<Parent> LaborChargeList;

  @FXML
  private TextField DefaultLaborRate;

  @FXML
  private TextField RateField;

  @FXML
  private Label LaborChargeLabel;

  @FXML
  private ComboBox<String> PartNumberComboBox;

  @FXML
  private Label EndDateLabel;

  @FXML
  private ComboBox<String> PartNameComboBox;

  @FXML
  private Label QuantityErrorLabel;

  @FXML
  private Label StartDateLabel;

  @FXML
  private TextArea JobDetailsLabel;

  @FXML
  private TextField PartQuantityField;

  @FXML
  private AnchorPane AddLaborChargePane;

  @FXML
  private Button CloseChargeButton;

  @FXML
  private AnchorPane UpdatePartChargePane;

  @FXML
  private AnchorPane UpdateLaborChargePane;

  @FXML
  private TextField UpdateLaborChargeNameField;

  @FXML
  private TextField UpdateLaborChargeRateField;

  @FXML
  private TextField UpdateLaborChargeHoursField;

  @FXML
  private Label UpdateLaborChargeNameErrorLabel;

  @FXML
  private Label UpdateLaborChargeRateErrorLabel;

  @FXML
  private Label UpdateLaborChargeHoursErrorLabel;

  @FXML
  private TextField UpdatePartQuantityField;

  @FXML
  private Label UpdatePartQuantityErrorLabel;

  @FXML
  private Tooltip DeliveryCostError;

  @FXML
  private HBox DeliveryCostEditContainer;

  @FXML
  private Label DefaultLaborRateError;

  @FXML
  private Label DefaultLaborRateNameError;
  @FXML
  void goToInvoice(){
    model.goToInvoice();
  }

  @FXML
  void updateLaborCharge(ActionEvent event) {
    model.updateLaborCharge();
  }

  @FXML
  void deleteLaborCharge(ActionEvent event) {
    model.deleteLaborCharge();
  }

  @FXML
  void LaborChargeUpdate(ActionEvent event) {
    model.updateLaborCharge();
  }

  @FXML
  void DeleteLaborCharge(ActionEvent event) {
    model.deleteLaborCharge();
  }

  @FXML
  void PartChargeUpdateQuanity(ActionEvent event) {
    model.validatePartChargeUpdate();
  }

  @FXML
  void DeleteCurrentPartCharge(ActionEvent event) {
    model.deletePartCharge();
  }

  @FXML
  void navigateBack(ActionEvent event) {
    App.useBackPointer();
  }

  @FXML
  void toggleJobNameEdit() {
    model.toggleJobNameEdit();
  }

  @FXML
  void cancelJobNameChange(ActionEvent event) {
    if (model.toggleJobNameEdit()) {
      JobNameEditContainer.toFront();
    } else {
      JobNameLabel.toFront();
    }
  }

  @FXML
  void SaveJobNameChange(ActionEvent event) {
    model.saveJobName();
    if (model.toggleJobNameEdit()) {
      JobNameEditContainer.toFront();
    } else {
      JobNameLabel.toFront();
    }
  }

  @FXML
  void showStatusUpdate(ActionEvent event) {
    model.showStatusUpdatePopup();
  }

  @FXML
  void addNewPartCharge(ActionEvent event) {
    model.showNewPart();
    CloseChargeButton.toFront();
  }

  @FXML
  void addNewLaborCharge(ActionEvent event) {
    model.showNewLaborCharge();
    CloseChargeButton.toFront();
  }

  @FXML
  void toggleDeliveryEdit(ActionEvent event) {
    model.toggleJobNameEdit();
  }

  @FXML
  void saveDeliveryCost(ActionEvent event) {
    model.saveDeliveryCost();
  }

  @FXML
  void clearDeliveryCost(ActionEvent event) {
    model.cancelDeliveryEdit();
  }

  @FXML
  void closeEditField(ActionEvent event) {
    model.hideNewPart();
    model.hideNewLaborCharge();
    model.hidePartUpdate();
    model.hideLaborUpdate();
  }

  @FXML
  void AddNewPart(ActionEvent event) {
    model.validatePartCharge(
      PartNameComboBox.getValue(),
      PartNumberComboBox.getValue()
    );
  }

  @FXML
  void addLaborCharge(ActionEvent event) {
    model.validateLaborCharge();
  }

  @FXML
  void closeUpdateJob(ActionEvent event) {
    model.hideStatusUpdatePopup();
  }

  @FXML
  void updateJobStatus(ActionEvent event) {
    model.updateJobStatus();
  }

  @FXML
  void openSettings(ActionEvent event) {
    model.showJobDetailsSettings();
  }

  @FXML
  void deleteJob(ActionEvent event) {
    model.showDeleteWarning();
  }

  @FXML
  void confirmDeletion(ActionEvent event) {
    model.deleteJob();
    App.useBackPointer();
  }

  @FXML
  void cancelDeletion(ActionEvent event) {
    model.hideDeleteWarning();
  }

  @FXML
  void closeSettings(ActionEvent event) {
    model.hideJobDetailsSettings();
  }

  @FXML
  void SaveSettingsChanges(ActionEvent event) {
    model.updateJobSettings();
  }

  private JobsDetailsPageModel model;

  @FXML
  void initialize() {
    JobDAO job = (JobDAO) App.getSceneProperty("jobDAO");
    if (job == null) App.useBackPointer();
    model = new JobsDetailsPageModel(job);

    CompletableFuture<Void> future1 = CompletableFuture.runAsync(
      this::initalizeJobDetails
    );
    CompletableFuture<Void> future2 = CompletableFuture.runAsync(
      this::initalizeVisiblities
    );
    CompletableFuture<Void> future3 = CompletableFuture.runAsync(
      this::initalizePartCharge
    );
    CompletableFuture<Void> future4 = CompletableFuture.runAsync(
      this::initalizeLaborCharge
    );
    CompletableFuture<Void> future5 = CompletableFuture.runAsync(
      this::initalizeUpdateLaborCharge
    );
    CompletableFuture<Void> future6 = CompletableFuture.runAsync(
      this::initalizePartChargeUpdate
    );
    CompletableFuture<Void> future7 = CompletableFuture.runAsync(
      this::initalizeBillDetails
    );
    CompletableFuture<Void> future8 = CompletableFuture.runAsync(
      this::initalizeJobStatusProperties
    );
    CompletableFuture<Void> future9 = CompletableFuture.runAsync(
      this::initalizeJobSettings
    );

    CompletableFuture
      .allOf(
        future1,
        future2,
        future3,
        future4,
        future5,
        future6,
        future7,
        future8,
        future9
      )
      .join();
  }

  private void initalizeJobStatusProperties() {
    //-------------------Job Status Properties-------------------//
    JobStatusSelect.setItems(model.getJobStatuses());
    JobStatusSelect.valueProperty().bindBidirectional(model.getJobStatus());
  }

  private void initalizeJobSettings() {
    //--------------------Job Settings-------------------//
    defaultLaborNameField
      .textProperty()
      .bindBidirectional(model.getDefaultLaborChargeName());
    DefaultLaborRate
      .textProperty()
      .bindBidirectional(model.getDefaultLaborChargeRate());
    DefaultLaborRateError.textProperty().bind(model.getDefaultLaborRateError());
    DefaultLaborRateNameError
      .textProperty()
      .bind(model.getDefaultLaborNameError());
  }

  private void initalizeBillDetails() {
    //--------------------Bill Details Labels-------------------//
    BillTotalLabel.textProperty().bindBidirectional(model.getJobTotalCost());
    PartTotalLabel.textProperty().bindBidirectional(model.getJobPartsCost());
    LaborChargeLabel.textProperty().bindBidirectional(model.getJobLaborCost());
    DeliveryChargeLabel
      .textProperty()
      .bindBidirectional(model.getJobDeliveryCost());
    DeliveryChargeLabel
      .visibleProperty()
      .bind(model.getShowDeliveryCostEdit().not());
    DeliveryCostEditContainer
      .visibleProperty()
      .bind(model.getShowDeliveryCostEdit());
    DeliveryEditField
      .textProperty()
      .bindBidirectional(model.getDeliveryCostEdit());
    this.makeErrorListener(
        model.getDeliveryCostEditError(),
        DeliveryCostError,
        DeliveryEditField
      );
    DeliveryChargeLabel.setOnMouseClicked(event -> {
      model.toggleDeliveryEdit();
    });
  }

  private void initalizeJobDetails() {
    //--------------------Job Details Labels-------------------//
    JobNameLabel.textProperty().bindBidirectional(model.getJobName());
    JobNameField.textProperty().bindBidirectional(model.getJobNameEdit());
    MachineNameLabel.textProperty().bindBidirectional(model.getMachineName());
    JobDetailsLabel.textProperty().bindBidirectional(model.getJobDetails());
    JobDetailsLabel
      .focusedProperty()
      .addListener((obs, oldVal, newVal) -> {
        if (!newVal) {
          model.updateJobNotes();
        }
      });
    CreatedDateLabel
      .textProperty()
      .bindBidirectional(model.getJobCreatedDate());
    StartDateLabel.textProperty().bindBidirectional(model.getJobStartDate());
    EndDateLabel.textProperty().bindBidirectional(model.getJobEndDate());
    CreatedDateLabel
      .textProperty()
      .bindBidirectional(model.getJobCreatedDate());

    PartChargeList.setItems(model.getPartCharges());

    LaborChargeList.setItems(model.getLaborCharges());

    JobStatus.textProperty().bindBidirectional(model.getJobStatus());

    GoToInvoiceButton.visibleProperty().bind(model.getHasInvoice());
  }

  private void initalizeVisiblities() {
    //--------------------Bind mode visibility Properties-------------------//
    JobNameEditContainer
      .visibleProperty()
      .bindBidirectional(model.getJobDetailsEditProperty());
    JobNameLabel
      .visibleProperty()
      .bind(model.getJobDetailsEditProperty().not());

    PopupContentField
      .visibleProperty()
      .bind(
        model
          .getShowLaborCharge()
          .or(model.getShowPartCharge())
          .or(model.getShowPartChargeUpdate())
          .or(model.getShowLaborChargeUpdate())
      );
    PopupContentField
      .visibleProperty()
      .addListener((obs, oldVal, newVal) -> {
        if (newVal) {
          CloseChargeButton.toFront();
        }
      });
    AddAPartPane.visibleProperty().bind(model.getShowPartCharge());
    AddLaborChargePane.visibleProperty().bind(model.getShowLaborCharge());
    UpdatePartChargePane
      .visibleProperty()
      .bind(model.getShowPartChargeUpdate());
    UpdateLaborChargePane
      .visibleProperty()
      .bind(model.getShowLaborChargeUpdate());

    JobDetailsSettings.visibleProperty().bind(model.getJobDetailsSettings());
    JobStatusPopup.visibleProperty().bind(model.getShowStatusUpdate());
    DeleteWarning.visibleProperty().bind(model.getJobDeleteWarning());
  }

  private void initalizePartCharge() {
    //----- PartCharge Fields -----//
    PartNameComboBox.setItems(model.getPartNames());
    PartNumberComboBox.setItems(model.getPartNumbers());
    PartQuantityField.textProperty().bindBidirectional(model.getPartQuanity());
    QuantityErrorLabel
      .textProperty()
      .bindBidirectional(model.getPartQuanityError());

    PartNameComboBox
      .valueProperty()
      .addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
          PartNumberComboBox.setValue(null);
        }
      });
    PartNumberComboBox
      .valueProperty()
      .addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
          PartNameComboBox.setValue(null);
        }
      });
  }

  private void initalizeLaborCharge() {
    //----- LaborCharge Fields -----//
    LaborChargeNameField
      .textProperty()
      .bindBidirectional(model.getLaborChargeName());
    HoursField.textProperty().bindBidirectional(model.getLaborChargeHours());
    RateField.textProperty().bindBidirectional(model.getLaborChargeRate());
    LaborChargeNameErrorLabel
      .textProperty()
      .bindBidirectional(model.getLaborChargeNameError());
    HoursErrorLabel
      .textProperty()
      .bindBidirectional(model.getLaborChargeHoursError());
    RateErrorLabel
      .textProperty()
      .bindBidirectional(model.getLaborChargeRateError());
  }

  private void initalizeUpdateLaborCharge() {
    //----- Update Labor Charge Fields -----//
    UpdateLaborChargeNameField
      .textProperty()
      .bindBidirectional(model.getLaborChargeUpdateName());
    UpdateLaborChargeRateField
      .textProperty()
      .bindBidirectional(model.getLaborChargeUpdateRate());
    UpdateLaborChargeHoursField
      .textProperty()
      .bindBidirectional(model.getLaborChargeUpdateHours());
    UpdateLaborChargeNameErrorLabel
      .textProperty()
      .bindBidirectional(model.getLaborChargeUpdateNameError());
    UpdateLaborChargeRateErrorLabel
      .textProperty()
      .bindBidirectional(model.getLaborChargeUpdateRateError());
    UpdateLaborChargeHoursErrorLabel
      .textProperty()
      .bindBidirectional(model.getLaborChargeUpdateHoursError());
  }

  private void initalizePartChargeUpdate() {
    //----- Update PartCharge Fields -----//
    UpdatePartQuantityField
      .textProperty()
      .bindBidirectional(model.getPartChargeUpdateQuanity());
    UpdatePartQuantityErrorLabel
      .textProperty()
      .bindBidirectional(model.getPartChargeUpdateQuanityError());
  }

  public void makeErrorListener(
    StringProperty valueToListen,
    Tooltip errorToShow,
    Node nodeToAttatchTo
  ) {
    errorToShow.setOpacity(0);
    valueToListen.addListener((obs, oldVal, newVal) -> {
      if (newVal.isEmpty()) {
        errorToShow.hide();
      } else {
        errorToShow.setOpacity(1);
        Point2D point = nodeToAttatchTo.localToScene(
          nodeToAttatchTo.getLayoutBounds().getMaxX(),
          nodeToAttatchTo.getLayoutBounds().getMaxY()
        );
        errorToShow.show(nodeToAttatchTo, point.getX(), point.getY());
        errorToShow.setAutoHide(true);
        //errorToShow.setShowDuration(javafx.util.Duration.valueOf("15s"));
      }
    });
  }
}
