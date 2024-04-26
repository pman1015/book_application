package org.customer_book.Pages.CustomerReportsPage.InvoiceDetails.PartCards;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;

import org.customer_book.Database.JobsCollection.PartChargeDAO;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceJobPartCardModel {

    // -------------------- View Properties -------------//
    private StringProperty partNumberProperty = new SimpleStringProperty("");
    private StringProperty quantityProperty = new SimpleStringProperty("");
    private StringProperty partNameProperty = new SimpleStringProperty("");

    // --------------------- Model Properties -------------//
    private PartChargeDAO partCharge;


    //-------------------- Model Methods -------------//
    public void setPartCharge(PartChargeDAO partCharge) {
        this.partCharge = partCharge;
        updateProperties();
    }

    private void updateProperties() {
        if (partCharge == null) {
            return;
        }
        partNumberProperty.set(partCharge.getPartNumber());
        quantityProperty.set(String.valueOf(partCharge.getQuantity()));
        partNameProperty.set(partCharge.getPartName());
    }
}
