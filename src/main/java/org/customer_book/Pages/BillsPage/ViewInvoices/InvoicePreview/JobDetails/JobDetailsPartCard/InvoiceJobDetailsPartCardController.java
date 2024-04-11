package org.customer_book.Pages.BillsPage.ViewInvoices.InvoicePreview.JobDetails.JobDetailsPartCard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.customer_book.Database.JobsCollection.PartChargeDAO;

public class InvoiceJobDetailsPartCardController {

  @FXML
  private Label PartName;

  @FXML
  private Label PartNumber;

  @FXML
  private Label Price;

  @FXML
  private Label Quanity;

  @FXML
  private Label Charge;

  private InvoiceJobDetailsPartCardModel model;

  @FXML
  void initialize() {
    model = new InvoiceJobDetailsPartCardModel();
    PartName.textProperty().bind(model.getPartName());
    PartNumber.textProperty().bind(model.getPartNumber());
    Price.textProperty().bind(model.getPrice());
    Quanity.textProperty().bind(model.getQuanity());
    Charge.textProperty().bind(model.getCharge());
  }

  public void setPartChargeDAO(PartChargeDAO part) {
    model.setPart(part);
  }
}
