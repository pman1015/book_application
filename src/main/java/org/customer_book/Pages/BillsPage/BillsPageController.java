package org.customer_book.Pages.BillsPage;

import java.util.concurrent.CompletableFuture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class BillsPageController {

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
  void CancelAdd(ActionEvent event) {}

  @FXML
  void showCreateInvoice() {}

  @FXML
  void showJobsFilter(ActionEvent event) {
    model.showJobsFilter();
  }

  @FXML
  void showCompletedInvoices() {}

  @FXML
  void showBillSettings(ActionEvent event) {}

  @FXML
  void saveInvoice(ActionEvent event) {}

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

    CompletableFuture
      .allOf(initaliseFilter, initaliseCompletedJobs, initaliseCreateInvoice)
      .join();
  }

  private void initaliseCreateInvoice() {
    //------------------ Bind visiblity for the add Job error -----------------//
    AddJobError.visibleProperty().bind(model.getShowAddJobError());

    //------------------ Bind Values for the Create Invoice Pane -----------------//
    CustomerNameLabel.textProperty().bind(model.getCustomerName());
    InvoiceNumberLabel.textProperty().bind(model.getInvoiceNumber());
    NewInvoiceTotalLabel.textProperty().bind(model.getInvoiceTotal());
    AddedJobs.setItems(model.getAddedJobCards());
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
