package org.customer_book.Pages.CustomerReportsPage.Cards;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javafx.beans.property.StringProperty;

import org.customer_book.Database.InvoiceCollection.InvoiceDAO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;

@Getter@Setter@NoArgsConstructor
public class InvoiceCardModel {
    //-------------------- View Properties --------------------//
    private StringProperty createdDateProperty = new SimpleStringProperty("");
    private StringProperty statusProperty = new SimpleStringProperty("");
    

    //-------------------- Model Properties --------------------//
    private InvoiceDAO invoice;
    private ObjectProperty<InvoiceDAO> selectedInvoice;


    //-------------------- View Methods --------------------//
    public void ShowSelectedInvoice(){
        if(invoice != null){
            selectedInvoice.set(invoice);
        }
    }

    //-------------------- Model Methods --------------------//
    //Set the initial invoice and selected invoice
    public void setInvoice(InvoiceDAO invoice , ObjectProperty<InvoiceDAO> selectedInvoice){
        this.invoice = invoice;
        this.selectedInvoice = selectedInvoice;
        updateProperties();

        //TODO: Add listener to the invoice to update the properties on change
    }

    //Update the properties of the invoice
    private void updateProperties(){
        if(invoice == null){
            return;
        }
        createdDateProperty.set(invoice.getGeneratedDate().toString());
        statusProperty.set(invoice.getStatus());
    }
}
