package org.customer_book.Pages.BillsPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class BillsPageController {

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
