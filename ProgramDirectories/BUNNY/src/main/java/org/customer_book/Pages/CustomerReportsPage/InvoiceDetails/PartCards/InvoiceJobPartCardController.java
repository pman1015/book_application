package org.customer_book.Pages.CustomerReportsPage.InvoiceDetails.PartCards;

import org.customer_book.Database.JobsCollection.PartChargeDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InvoiceJobPartCardController {

    @FXML
    private Label PartNumberLabel;

    @FXML
    private Label QuanityLabel;

    @FXML
    private Label PartNameLabel;

    private InvoiceJobPartCardModel model;

    public void setPartCharge(PartChargeDAO partCharge) {
        model.setPartCharge(partCharge);
    }

    @FXML
    void initialize() {
        model = new InvoiceJobPartCardModel();
        //Bindings
        PartNumberLabel.textProperty().bind(model.getPartNumberProperty());
        QuanityLabel.textProperty().bind(model.getQuantityProperty());
        PartNameLabel.textProperty().bind(model.getPartNameProperty());
    }
}
