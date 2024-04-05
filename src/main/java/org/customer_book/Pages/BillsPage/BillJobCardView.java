package org.customer_book.Pages.BillsPage;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import org.customer_book.Database.JobsCollection.JobDAO;

public class BillJobCardView {

 

  @FXML
  private Label PartsCostTotalLabel;

  @FXML
  private Label JobNameLabel;

  @FXML
  private ListView<Parent> PartsUsedList;

  @FXML
  private Label DeliveryFeeLabel;

  @FXML
  private Label EquipmentNameLabel;

  @FXML
  private Label JobCostLabel;

  @FXML
  private Label LaborCostTotalLabel;

  @FXML
  void removeJob(ActionEvent event) {}

  private BillJobCardModel model;

  @FXML
  void initialize() {
    model = new BillJobCardModel();
    //----------------- Label Bindings -----------------//
    JobNameLabel.textProperty().bind(model.getJobNameProperty());
    EquipmentNameLabel.textProperty().bind(model.getEquipmentNameProperty());
    PartsCostTotalLabel.textProperty().bind(model.getPartsCostTotalProperty());
    LaborCostTotalLabel.textProperty().bind(model.getLaborCostTotalProperty());
    DeliveryFeeLabel.textProperty().bind(model.getDeliveryFeeProperty());
    JobCostLabel.textProperty().bind(model.getJobCostProperty());

    //------------------ List Bindings -----------------//
    PartsUsedList.setItems(model.getPartsUsedList());
  }

  public void setJobDAO(JobDAO jobDAO, ObjectProperty<JobDAO> toRemove) {
    model.setJobDAO(jobDAO, toRemove);
  }
}
