package org.customer_book.Pages.HomePage.Content.Panes.RecentJobs;

import org.customer_book.Pages.HomePage.Content.Panes.PaneController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;

public class RecentJobsController extends PaneController {
  @FXML
  private ProgressIndicator LoadingIndicator;

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
    Thread updateJobs = new Thread(() -> {
      long minimum_load_time = 1000;
      long start_time = System.currentTimeMillis();

      model.loadJobs(true);
      if (minimum_load_time > System.currentTimeMillis() - start_time) {
        try {
          Thread.sleep(minimum_load_time - (System.currentTimeMillis() - start_time));
        }
        catch (InterruptedException e) {
        }
      }
      Platform.runLater(() -> {
        LoadingIndicator.setVisible(false);
      });
    });
    updateJobs.start();

  }

  public void setPaneNumber(int paneNumber) {
    model.setPaneInfo(paneNumber);
  }

}
