package org.customer_book.Pages.CustomerJobsPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.bson.types.ObjectId;

public class CustomerJobsPageCardController {

  @FXML
  private Label JobNameLabel;

  @FXML
  private Label JobEndDateLabel;

  @FXML
  private Label JobStatusLabel;

  @FXML
  private Label JobStartDateLabel;

  @FXML
  void loadJobDetails() {
    model.loadJobDetails();
  }

  private CustomerJobsPageCardModel model;

  @FXML
  void initialize() {
    model = new CustomerJobsPageCardModel();

    JobNameLabel.textProperty().bind(model.getJobNameProperty());
    JobStartDateLabel.textProperty().bind(model.getJobStartDateProperty());
    JobEndDateLabel.textProperty().bind(model.getJobEndDateProperty());
    JobStatusLabel.textProperty().bind(model.getJobStatusProperty());
  }

  public void setJobID(ObjectId jobID) {
    model.setJob(jobID);
  }
}
