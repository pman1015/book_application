package org.customer_book.Popups.DownloadInvoicePopup;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;
import org.customer_book.Utilities.InvoiceCreate;

@Getter
@Setter
public class DownloadInvoiceModel {

  private BooleanProperty complete = new SimpleBooleanProperty(false);

  private InvoiceDAO invoice;
  private StringProperty downloadLocation;

  private InvoiceCreate invoiceCreate;
  private int stepCount = 0;
  private int totalSteps = 5;
  private String message = "";

  public void setInitialData(
    InvoiceDAO invoice,
    StringProperty downloadLocation
  ) {
    this.invoice = invoice;
    this.downloadLocation = downloadLocation;

    invoiceCreate = new InvoiceCreate(invoice, downloadLocation.get());
  }

  public String downloadInvoice() {
    switch (stepCount) {
      case 0:
        invoiceCreate.updateCustomerInfo();
        stepCount++;
        break;
      case 1:
        invoiceCreate.updateInvoiceInfo();
        stepCount++;
        break;
      case 2:
        invoiceCreate.updateTotals();
        stepCount++;
        break;
      case 3:
        invoiceCreate.updateJobs();
        stepCount++;
        break;
      case 4:
        invoiceCreate.writeJobsToInvoice();
        stepCount++;
        break;
      case 5:
        invoiceCreate.saveInvoice();
        stepCount++;
        complete.set(true);
        break;
    }
   this.message = invoiceCreate.getMessage();
   System.out.println(message);
   return message;
  }
}
