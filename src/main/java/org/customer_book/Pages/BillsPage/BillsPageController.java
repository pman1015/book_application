package org.customer_book.Pages.BillsPage;

import java.util.concurrent.CompletableFuture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class BillsPageController {
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
  private DatePicker completedDateJobsFilter;

  @FXML
  private DatePicker createdDateJobsFilter;

  @FXML
  private ComboBox<String> customerNameJobsFilter;

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
    model.saveSettings();
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
  void downloadInvoice(ActionEvent event) {}

  @FXML
  void closeJobsFilter(ActionEvent event) {
    model.hideJobsFilter();
  }

  @FXML
  void applyJobsFilter(ActionEvent event) {}

  private BillsPageModel model;

  @FXML
  void initialize() {
    model = new BillsPageModel();
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

    CompletableFuture
      .allOf(
        initaliseFilter,
        initaliseCompletedJobs,
        initaliseCreateInvoice,
        initaliseViewCreatedInvoicePane,
        initaliseSettingsPane,
        initaliseCompletedInvoices
      )
      .join();
  }
  private void initaliseCompletedInvoices(){
    CompletedInvoicesList.setItems(model.getCompletedInvoiceCards());
  }
  private void initaliseSettingsPane(){
    //------------------ Bind visiblity for the Settings Pane -----------------//
    BillsSettings.visibleProperty().bind(model.getShowSettings());
    DownloadLocationField.textProperty().bindBidirectional(model.getDownloadLocation());
  }

  private void initaliseViewCreatedInvoicePane() {
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
      .bind(model.getShowGenerateInvoicePane().not());
  }

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

  private void initaliseCompletedJobs() { //----------------- CompletedJobCard bindings -------------------//
    CompletedJobsList.setItems(model.getCompletedJobCards());
  }

  private void initaliseFilterProperties() {
    //----------------- Filter Properties -----------------//
    jobsFilterPane.visibleProperty().bind(model.getShowJobsFilter());

    completedDateJobsFilter
      .valueProperty()
      .bindBidirectional(model.getCompletedDateJobsFilterProperty());
    createdDateJobsFilter
      .valueProperty()
      .bindBidirectional(model.getCreatedDateJobsFilterProperty());
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
