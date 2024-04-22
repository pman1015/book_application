package org.customer_book.Pages.HomePage.Content.Panes.RecentJobs;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

public class RecentJobsController {

  @FXML
  private ListView<Parent> RecentJobsList;

  @FXML
  void showAddNewJob(ActionEvent event) {
  }

  @FXML
  void showJobsFilter(ActionEvent event) {
  }

  private RecentJobsModel model;

  @FXML
  void initialize() {
    model = new RecentJobsModel();
    RecentJobsList.setItems(model.getRecentJobsList());
    RecentJobsList.widthProperty().addListener((obs, oldVal, newVal) -> {
      model.setJobCardWidth(newVal.doubleValue());
    });
    Platform.runLater(() -> {
      model.loadJobs(true);
    });
  }
}
