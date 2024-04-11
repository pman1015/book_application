package org.customer_book.Pages.BillsPage.ViewInvoices.InvoicePreview.JobDetails.JobDetailsPartCard;



import org.customer_book.Database.JobsCollection.PartChargeDAO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class InvoiceJobDetailsPartCardModel {

    private StringProperty PartName = new SimpleStringProperty("");
    private StringProperty PartNumber = new SimpleStringProperty("");
    private StringProperty Price = new SimpleStringProperty("");
    private StringProperty Quanity = new SimpleStringProperty("");
    private StringProperty Charge = new SimpleStringProperty("");

    private PartChargeDAO part;

    public void setPart(PartChargeDAO part){
        this.part = part;
        PartName.set(part.getPartName());
        PartNumber.set(part.getPartNumber());
        Price.set(String.valueOf(part.getCharge()));
        Quanity.set(String.valueOf(part.getQuantity()));
        Charge.set(String.valueOf(part.getTotal()));
    }
    
}
