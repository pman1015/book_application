package org.customer_book.Pages.BillsPage.ViewInvoices.InvoiceListCards;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;

public class InvoiceCardController {

  @FXML
  private Label completedDateLabel;

  @FXML
  private Label customerNameLabel;

  @FXML
  private ListView<Parent> EquipmentList;

  @FXML
  void selectInvoice() {
    model.selectInvoice();
  }

  private InvoiceCardModel model;

  @FXML
  void initialize() {
    model = new InvoiceCardModel();
    customerNameLabel.textProperty().bind(model.getCustomerName());
    completedDateLabel.textProperty().bind(model.getCompletedDate());
    EquipmentList.setItems(model.getEquipmentList());
  }

  public void setInvoice(
    InvoiceDAO dao,
    ObjectProperty<InvoiceDAO> selectedInvoice
  ) {
    model.setInvoice(dao, selectedInvoice);
  }
}
