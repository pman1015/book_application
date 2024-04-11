package org.customer_book.Pages.BillsPage.CreateInvoice.NewInvoiceComponents.PartCard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.customer_book.Database.JobsCollection.PartChargeDAO;

public class BillJobPartCardController {

  @FXML
  private Label CostLabel;

  @FXML
  private Label PartNumberLabel;

  @FXML
  private Label QuanityLabel;

  @FXML
  private Label PartNameLabel;

  private BillJobPartCardModel model;

  @FXML
  void initialize() {
    model = new BillJobPartCardModel();
    //----------------- Label Bindings -----------------//
    PartNameLabel.textProperty().bind(model.getPartNameProperty());
    PartNumberLabel.textProperty().bind(model.getPartNumberProperty());
    QuanityLabel.textProperty().bind(model.getQuantityProperty());
    CostLabel.textProperty().bind(model.getCostProperty());
  }

  public void setPartChargeDAO(PartChargeDAO partChargeDAO) {
    model.setPartChargeDAO(partChargeDAO);
  }
}
