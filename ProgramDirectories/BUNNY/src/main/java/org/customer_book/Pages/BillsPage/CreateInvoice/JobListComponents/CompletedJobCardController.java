package org.customer_book.Pages.BillsPage.CreateInvoice.JobListComponents;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import org.customer_book.Database.JobsCollection.JobDAO;

public class CompletedJobCardController {

  @FXML
  private Label CustomerNameLabel;

  @FXML
  private AnchorPane CompletedJobCard;

  @FXML
  private Label JobNameLabel;

  @FXML
  private Circle SelectedCircle;

  @FXML
  private Label CompletedDateLabel;

  @FXML
  void toggleJobStatus() {
    model.toggleSelected();
  }

  private CompletedJobCardModel model;

  @FXML
  void initialize() {
    model = new CompletedJobCardModel();
    //----------------- Label Bindings -----------------//
    CustomerNameLabel.textProperty().bind(model.getCustomerNameProperty());
    JobNameLabel.textProperty().bind(model.getJobNameProperty());
    CompletedDateLabel.textProperty().bind(model.getCompletedDateProperty());

    //------------------Selected Bindings -----------------//
    SelectedCircle.fillProperty().bind(model.getSelectedCircleProperty());
  }

  public void setJobDAO(
    JobDAO jobDAO,
    ObjectProperty<JobDAO> JobToAdd,
    ObjectProperty<JobDAO> JobToRemove
  ) {
    model.setJobDAO(jobDAO, JobToAdd, JobToRemove);
  }
}
