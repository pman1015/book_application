package org.customer_book.Pages.BillsPage.ViewInvoices.InvoicePreview.JobDetails;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;
import org.customer_book.Database.InvoiceCollection.InvoiceEntry;

public class InvoiceJobDetailsCardController {

  @FXML
  private AnchorPane JobDetailsCard;

  @FXML
  private Label JobDetailName;

  @FXML
  private Label JobDetailJobTotal;

  @FXML
  private Label JobDetailEquipmentName;

  @FXML
  private Label JobDetailStartDate;

  @FXML
  private ListView<Parent> JobDetailPartList;

  @FXML
  private RadioButton AddNotes;

  @FXML
  private TextArea JobNotes;

  private InvoiceJobDetailsCardModel model;

  @FXML
  void initialize() {
    // Create the model for the invoice job details card
    model = new InvoiceJobDetailsCardModel();

    //------------- Value Bindings ---------------//
    JobDetailName.textProperty().bind(model.getJobDetailName());
    JobDetailJobTotal.textProperty().bind(model.getJobDetailJobTotal());
    JobDetailEquipmentName
      .textProperty()
      .bind(model.getJobDetailEquipmentName());
    JobDetailStartDate.textProperty().bind(model.getJobDetailStartDate());
    JobDetailPartList.setItems(model.getJobDetailPartList());
    AddNotes.selectedProperty().bindBidirectional(model.getAddNotes());
    JobNotes.visibleProperty().bind(model.getAddNotes());
    JobNotes.textProperty().bindBidirectional(model.getJobNotes());
    
    // Set the initial height of the card
    JobDetailsCard.setPrefHeight(310);

    //-------------- Listners -------------------//
    // When the add notes radio button is selected
    // show the notes text area and increase the height of the card
    AddNotes
      .selectedProperty()
      .addListener((obs, oldVal, newVal) -> {
        if (newVal) {
          JobDetailsCard.setPrefHeight(400);
        } else {
          JobDetailsCard.setPrefHeight(310);
        }
      });

    // When the notes text area loses focus update the notes in the model
    JobNotes
      .focusedProperty()
      .addListener((obs, oldVal, newVal) -> {
        if (!newVal) {
          model.updateNotes(JobNotes.getText());
        }
      });
  }

  /**
   * Set the Invoice Entry and the selected invoice
   *  - the Entry is fill out the details and selected invoice is what
   *    invoice to update when the notes are changed
   * @param entry - InvoiceEntry
   * @param selectedInvoice - ObjectProperty<InvoiceDAO> the selected Invoice
   */
  public void setJobDetails(
    InvoiceEntry entry,
    ObjectProperty<InvoiceDAO> selectedInvoice
  ) {
    model.setInvoiceEntry(entry, selectedInvoice);
  }
}
