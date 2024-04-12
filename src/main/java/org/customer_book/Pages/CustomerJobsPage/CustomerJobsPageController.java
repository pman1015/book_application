package org.customer_book.Pages.CustomerJobsPage;

import static com.mongodb.client.model.Filters.in;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.customer_book.App;

public class CustomerJobsPageController {

  @FXML
  private Label CustomerNameLabel;

  @FXML
  private ListView<Parent> JobCardList;

  @FXML
  void showCreateNewJob(ActionEvent event) {
    model.showCreateNewJob();
  }

  @FXML
  void showFilterOptions(ActionEvent event) {}

  @FXML
  void navigateBack(ActionEvent event) {
    App.useBackPointer();
  }

  private CustomerJobsPageModel model;

  @FXML
  void initialize() {
    if (App.getSceneProperty("customerDAO") == null) navigateBack(null);
    App.setBackPointer("CustomerDetailsPage", "CustomerDetailsPage");
    model = new CustomerJobsPageModel();
    CustomerNameLabel.textProperty().bind(model.getCustomerName());
    JobCardList.setItems(model.getJobCards());
  }
}
