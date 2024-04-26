package org.customer_book.Pages.BillsPage.CreateInvoice.NewInvoiceComponents.PartCard;

import org.customer_book.Database.JobsCollection.PartChargeDAO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter@Setter@NoArgsConstructor
public class BillJobPartCardModel {
    private StringProperty PartNameProperty = new SimpleStringProperty("");
    private StringProperty PartNumberProperty = new SimpleStringProperty("");
    private StringProperty QuantityProperty = new SimpleStringProperty("");
    private StringProperty CostProperty = new SimpleStringProperty("");
    private PartChargeDAO partChargeDAO;

    public void setPartChargeDAO(PartChargeDAO partChargeDAO) {
        this.partChargeDAO = partChargeDAO;
        PartNameProperty.set(partChargeDAO.getPartName());
        PartNumberProperty.set(partChargeDAO.getPartNumber());
        QuantityProperty.set(String.valueOf(partChargeDAO.getQuantity()));
        CostProperty.set("$" + partChargeDAO.getCost());
    }
}
