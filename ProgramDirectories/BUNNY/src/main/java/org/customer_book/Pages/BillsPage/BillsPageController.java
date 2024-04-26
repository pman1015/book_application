package org.customer_book.Pages.BillsPage;


import java.util.concurrent.CompletableFuture;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

public class BillsPageController {

  @FXML
  private AnchorPane DeleteConfirmationPopup;

  @FXML
  private AnchorPane UpdateStatusPopup;

  @FXML
  private ChoiceBox<String> StatusUpdateChoiceBox;

  @FXML
  private Button SelectedInvoiceStatus;

  @FXML
  private Label SelectedInvoiceCustomerName;

  @FXML
  private Label SelectedInvoiceNumber;

  @FXML
  private Label SelectedInvoiceCustomerAddress;

  @FXML
  private Label SelectedInvoiceCustomerPhoneNumber;

  @FXML
  private Label SelectedInvoiceCreatedDate;

  @FXML
  private ListView<Parent> SelectedInvoiceJobDetails;

  @FXML
  private Label SelectedInvoiceLaborTotal;

  @FXML
  private Label SelectedInvoiceDeliveryTotal;

  @FXML
  private Label SelectedInvoiceChargeTotal;

  @FXML
  private ListView<Parent> CompletedInvoicesList;

  @FXML
  private AnchorPane BillsSettings;

  @FXML
  private TextField DownloadLocationField;

  @FXML
  private Tab CompletedJobsTab;

  @FXML
  private Tab CompletedInvoicesTab;

  @FXML
  private AnchorPane InvoiceGeneratedPopup;

  @FXML
  private AnchorPane ViewCreatedInvoicePane;

  @FXML
  private Label CustomerNameLabel;

  @FXML
  private Label InvoiceNumberLabel;

  @FXML
  private Label NewInvoiceTotalLabel;

  @FXML
  private AnchorPane AddJobError;

  @FXML
  private ListView<Parent> AddedJobs;

  @FXML
  private ListView<Parent> CompletedJobsList;

  @FXML
  private AnchorPane CreateInvoicePane;

  @FXML
  private AnchorPane jobsFilterPane;

  @FXML
  private ComboBox<String> customerNameJobsFilter;

  @FXML
  private ToggleGroup FilterType;

  @FXML
  private RadioButton DateCompleted;

  @FXML
  private RadioButton DateCreated;

  @FXML
  private DatePicker startDate;

  @FXML
  private DatePicker endDate;

  @FXML
  private Label FilterError;

  @FXML
  void clearFilter(ActionEvent event) {
    model.clearFilter();
  }

  @FXML
  void showStatusUpdate(ActionEvent event) {
    model.showStatusUpdate();
  }

  @FXML
  void DeleteSelectedInvoice(ActionEvent event) {
    model.deleteSelectedInvoice();
  }

  @FXML
  void DownloadSelectedInvoice(ActionEvent event) {
    model.downloadSelectedInvoice();
  }

  @FXML
  void showInvoiceFilter(ActionEvent event) {
    model.showInvoiceFilter();
  }

  @FXML
  void showDownloads(ActionEvent event) {
    model.showDownloadLocation();
  }

  @FXML
  void closeSettings(ActionEvent event) {
    model.hideSettings();
  }

  @FXML
  void saveSettings(ActionEvent event) {
    model.hideSettings();
  }

  @FXML
  void returnToBills(ActionEvent event) {
    model.hideGeneratedInvoicePopup();
  }

  @FXML
  void goToInvoice(ActionEvent event) {
    model.showGeneratedInvoicePopup();
  }

  @FXML
  void CancelAdd(ActionEvent event) {}

  @FXML
  void showJobsFilter(ActionEvent event) {
    model.showJobsFilter();
  }

  @FXML
  void showBillSettings(ActionEvent event) {
    model.showSettings();
  }

  @FXML
  void saveInvoice(ActionEvent event) {
    model.saveInvoice();
  }

  @FXML
  void closeJobsFilter(ActionEvent event) {
    model.hideJobsFilter();
  }

  @FXML
  void applyJobsFilter(ActionEvent event) {
    model.applyJobsFilter();
  }

  @FXML
  void saveInvoiceStatus(ActionEvent event) {
    model.saveInvoiceStatus(StatusUpdateChoiceBox.getValue());
  }

  @FXML
  void closeStatusPopup(ActionEvent event) {
    model.hideStatusUpdate();
  }

  @FXML
  void ConfirmInvoiceDelete(ActionEvent event) {
    model.confirmDeleteInvoice();
  }

  @FXML
  void CancelInvoiceDelete(ActionEvent event) {
    model.cancelInvoiceDelete();
  }

  private BillsPageModel model;

  @FXML
  void initialize() {
    // Create the model for the Bills Page
    model = new BillsPageModel();
    //------------------ Asynchronous Initalisation -----------------//
    CompletableFuture<Void> initaliseFilter = CompletableFuture.runAsync(
      this::initaliseFilterProperties
    );
    CompletableFuture<Void> initaliseCompletedJobs = CompletableFuture.runAsync(
      this::initaliseCompletedJobs
    );
    CompletableFuture<Void> initaliseCreateInvoice = CompletableFuture.runAsync(
      this::initaliseCreateInvoice
    );
    CompletableFuture<Void> initaliseViewCreatedInvoicePane = CompletableFuture.runAsync(
      this::initaliseViewCreatedInvoicePane
    );
    CompletableFuture<Void> initaliseSettingsPane = CompletableFuture.runAsync(
      this::initaliseSettingsPane
    );
    CompletableFuture<Void> initaliseCompletedInvoices = CompletableFuture.runAsync(
      this::initaliseCompletedInvoices
    );
    CompletableFuture<Void> initaliseInvoicePopups = CompletableFuture.runAsync(
      this::initaliseInvoicePopups
    );
    //Run all the initalisation tasks
    CompletableFuture
      .allOf(
        initaliseFilter,
        initaliseCompletedJobs,
        initaliseCreateInvoice,
        initaliseViewCreatedInvoicePane,
        initaliseSettingsPane,
        initaliseCompletedInvoices,
        initaliseInvoicePopups
      )
      .join();
  }

  /**
   * Initalise the Invoice Popups
   */
  private void initaliseInvoicePopups() {
    //------------------ Status Update Popup -----------------//
    StatusUpdateChoiceBox.setItems(model.getStatusUpdateOptions());
    StatusUpdateChoiceBox
      .valueProperty()
      .bindBidirectional(model.getStatusUpdateValue());
    UpdateStatusPopup.visibleProperty().bind(model.getShowStatusUpdate());

    //------------------ Delete Confirmation Popup -----------------//
    DeleteConfirmationPopup
      .visibleProperty()
      .bind(model.getShowDeleteConfirmation());
  }

  /**
   * Initalise the Completed Invoices Pane
   */
  private void initaliseCompletedInvoices() {
    CompletedInvoicesList.setItems(model.getCompletedInvoiceCards());
    CompletedInvoicesList .heightProperty()
    .addListener((obs, oldHeight, newHeight) -> {
      Node n = CompletedJobsList.lookup(".scroll-bar:vertical");
      if (n instanceof ScrollBar) {
        ScrollBar bar = (ScrollBar) n;
        bar
          .valueProperty()
          .addListener((obs2, oldVal, newVal) -> {
            if (newVal.doubleValue() == 1.0) {
              model.loadMoreInvoices();
            }
          });
      }
    });
    SelectedInvoiceCustomerName
      .textProperty()
      .bind(model.getSelectedCustomerName());
    SelectedInvoiceNumber.textProperty().bind(model.getSelectedInvoiceNumber());
    SelectedInvoiceCustomerAddress
      .textProperty()
      .bind(model.getSelectedInvoiceCustomerAddress());
    SelectedInvoiceCustomerPhoneNumber
      .textProperty()
      .bind(model.getSelectedInvoiceCustomerPhoneNumber());
    SelectedInvoiceCreatedDate
      .textProperty()
      .bind(model.getSelectedInvoiceCreatedDate());
    SelectedInvoiceJobDetails.setItems(model.getSelectedInvoiceJobDetails());
    SelectedInvoiceLaborTotal
      .textProperty()
      .bind(model.getSelectedInvoiceLaborTotal());
    SelectedInvoiceDeliveryTotal
      .textProperty()
      .bind(model.getSelectedInvoiceDeliveryTotal());
    SelectedInvoiceChargeTotal
      .textProperty()
      .bind(model.getSelectedInvoiceChargeTotal());
    SelectedInvoiceStatus.textProperty().bind(model.getSelectedInvoiceStatus());
  }

  /**
   * Initalise the Settings Pane
   */
  private void initaliseSettingsPane() {
    //------------------ Bind visiblity for the Settings Pane -----------------//
    BillsSettings.visibleProperty().bind(model.getShowSettings());
    DownloadLocationField
      .textProperty()
      .bindBidirectional(model.getDownloadLocation());
  }

  /**
   * Initalise the View Created Invoice Pane
   */
  private void initaliseViewCreatedInvoicePane() {
    //------------------ Add Listner for tab change -----//
    CompletedInvoicesTab
      .selectedProperty()
      .addListener((obs, oldTab, newTab) -> {
        if (newTab) {
          model.showCompletedInvoicePane();
        }
      });
    //------------------ Bind visiblity for the View Created Invoice Pane -----------------//
    ViewCreatedInvoicePane
      .visibleProperty()
      .bind(model.getShowCompletedInvoice());
  }

  /**
   * Initalise the Create Invoice Pane
   */
  private void initaliseCreateInvoice() {
    //------------------ Bind visiblity for the add Job error -----------------//
    AddJobError.visibleProperty().bind(model.getShowAddJobError());

    //------------------ Bind Values for the Create Invoice Pane -----------------//
    CustomerNameLabel.textProperty().bind(model.getCustomerName());
    InvoiceNumberLabel.textProperty().bind(model.getInvoiceNumber());
    NewInvoiceTotalLabel.textProperty().bind(model.getInvoiceTotal());
    AddedJobs.setItems(model.getAddedJobCards());

    //------------------ Bind visiblity for the Invoice Generated Popup -----------------//
    InvoiceGeneratedPopup
      .visibleProperty()
      .bind(model.getShowGeneratedInvoicePopup());
    CreateInvoicePane
      .visibleProperty()
      .bind(model.getShowGenerateInvoicePane());
    //--------------- Add Listner for tab change -----///
    CompletedJobsTab
      .selectedProperty()
      .addListener((obs, oldTab, newTab) -> {
        if (newTab) {
          model.showGenerateInvoicePane();
        }
      });
  }

  /**
   * Initalise the Completed Jobs Pane
   */
  private void initaliseCompletedJobs() {
    //----------------- CompletedJobCard bindings -------------------//
    CompletedJobsList.setItems(model.getCompletedJobCards());
    //----------------- Load more jobs when the scroll bar reaches the bottom -------------------//
    CompletedJobsList
      .heightProperty()
      .addListener((obs, oldHeight, newHeight) -> {
        Node n = CompletedJobsList.lookup(".scroll-bar:vertical");
        if (n instanceof ScrollBar) {
          ScrollBar bar = (ScrollBar) n;
          bar
            .valueProperty()
            .addListener((obs2, oldVal, newVal) -> {
              if (newVal.doubleValue() == 1.0) {
                model.loadMoreCompletedJobs();
              }
            });
        }
      });
  }

  /**
   * Initalise the Filter Properties
   */
  private void initaliseFilterProperties() {
    //----------------- Filter Properties -----------------//
    jobsFilterPane.visibleProperty().bind(model.getShowJobsFilter());
    FilterError.textProperty().bind(model.getFilterErrorMessage());
    DateCreated
      .selectedProperty()
      .bindBidirectional(model.getFilterByDateCreated());
    DateCompleted
      .selectedProperty()
      .bindBidirectional(model.getFilterByDateCompleted());
    startDate.valueProperty().bindBidirectional(model.getStartDateProperty());
    endDate.valueProperty().bindBidirectional(model.getEndDateProperty());

    customerNameJobsFilter
      .valueProperty()
      .bindBidirectional(model.getCustomerNameJobsFilterProperty());
    customerNameJobsFilter.setItems(
      model.getAvailableNamesJobsFilterProperty()
    );
    customerNameJobsFilter
      .getEditor()
      .textProperty()
      .addListener((obs, oldText, newText) -> {
        model.updateNames(newText);
      });
  }
}
