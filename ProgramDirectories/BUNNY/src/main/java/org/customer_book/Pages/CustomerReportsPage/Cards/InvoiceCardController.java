package org.customer_book.Pages.CustomerReportsPage.Cards;

import org.customer_book.Database.InvoiceCollection.InvoiceDAO;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InvoiceCardController {

    @FXML
    private Label CreatedDateLabel;

    @FXML
    private Label StatusLabel;

  
    @FXML
    void ShowSelectedInvoice() {
        model.ShowSelectedInvoice();
    }

    private InvoiceCardModel model;

    @FXML
    void initialize() {
        model = new InvoiceCardModel();

        Platform.runLater(() -> {
            CreatedDateLabel.textProperty().bind(model.getCreatedDateProperty());
            StatusLabel.textProperty().bind(model.getStatusProperty());
        });
    }

    public void setInvoice(InvoiceDAO invoice, ObjectProperty<InvoiceDAO> selectedInvoice) {
        model.setInvoice(invoice, selectedInvoice);
    }

}
