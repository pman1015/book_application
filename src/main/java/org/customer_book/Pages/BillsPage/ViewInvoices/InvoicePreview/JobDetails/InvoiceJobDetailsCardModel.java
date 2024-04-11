package org.customer_book.Pages.BillsPage.ViewInvoices.InvoicePreview.JobDetails;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;
import org.customer_book.Database.InvoiceCollection.InvoiceEntry;
import org.customer_book.Pages.BillsPage.ViewInvoices.InvoicePreview.JobDetails.JobDetailsPartCard.InvoiceJobDetailsPartCardController;

@Getter
@Setter
public class InvoiceJobDetailsCardModel {

  //-------------------- Displayed Properties for Job Details -----------------------//
  private SimpleBooleanProperty AddNotes = new SimpleBooleanProperty(false);
  private SimpleStringProperty JobNotes = new SimpleStringProperty("");
  private SimpleStringProperty JobDetailName = new SimpleStringProperty("");
  private SimpleStringProperty JobDetailJobTotal = new SimpleStringProperty("");
  private SimpleStringProperty JobDetailEquipmentName = new SimpleStringProperty(
    ""
  );
  private SimpleStringProperty JobDetailStartDate = new SimpleStringProperty(
    ""
  );
  //-------------------- List of Part Cards for the Job Details -----------------------//
  private ObservableList<Parent> JobDetailPartList = FXCollections.observableArrayList();

  //-------------------- Invoice Entry and Selected Invoice ---------------------------//
  private InvoiceEntry entry; //The current entry for the job details
  private ObjectProperty<InvoiceDAO> selectedInvoice; //The selected invoice for updates

  public void setInvoiceEntry(InvoiceEntry entry, ObjectProperty<InvoiceDAO> selectedInvoice) {
    //Initalise the current entry and the selected invoice
    this.entry = entry;
    this.selectedInvoice = selectedInvoice;

    //set the values for the job details
    JobDetailName.set(entry.getJobName());
    JobDetailJobTotal.set(entry.getJobTotal());
    JobDetailEquipmentName.set(entry.getEquipmentName());
    JobDetailStartDate.set(entry.getStartDate().toString());
    JobNotes.set(entry.getNotes());
    AddNotes.set(entry.isShowNotes());

    //Update the invoice entry when the show notes changes
    AddNotes.addListener((obs, oldVal, newVal) -> {
      entry.setShowNotes(newVal);
      DatabaseConnection.invoiceCollection.updateEntryOnInvoice(selectedInvoice.get(), entry);
    });
    //Load the part cards for the jobs
    JobDetailPartList.clear();
    if (entry.getBill().getPartCharges() == null) return;
    //Load a Part Card for each part charge in the bill
    entry
      .getBill()
      .getPartCharges()
      .forEach(part -> {
        try {
          FXMLLoader partCardLoader = App.getLoader(
            "BillsPage",
            "SelectedInvoiceJobDetailsPartCard"
          );
          Parent partCard = partCardLoader.load();
          (
            (InvoiceJobDetailsPartCardController) partCardLoader.getController()
          ).setPartChargeDAO(part);
          JobDetailPartList.add(partCard);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
  }
  /**
   * Update the notes for the current entry
   * -Fires when the focus changes on the notes text area
   * @param notes
   */
  public void updateNotes(String notes){
    if(entry.getNotes().equals(notes)) return;
    entry.setNotes(notes);
    DatabaseConnection.invoiceCollection.updateEntryOnInvoice(selectedInvoice.get(), entry);
  }
}
