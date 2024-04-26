package org.customer_book.Pages.JobDetailsPage;

import org.customer_book.Database.JobsCollection.LaborChargeDAO;
import org.customer_book.Database.JobsCollection.PartChargeDAO;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ChargeCardController {
    @FXML
    private Label FirstLabel;

    @FXML
    private Label ThirdLabel;

    @FXML
    private Label SecondLabel;

    @FXML
    void updateCard() {
        model.updateCard();
    }

    private ChargeCardModel model;
    @FXML
    void initialize() {
        model = new ChargeCardModel();
        FirstLabel.textProperty().bind(model.getFirstLabelProperty());
        SecondLabel.textProperty().bind(model.getSecondLabelProperty());
        ThirdLabel.textProperty().bind(model.getThirdLabelProperty());
    }
    public void setLaborCharge(LaborChargeDAO laborCharge, ObjectProperty<LaborChargeDAO> selectedLaborCharge){
        model.setLaborCharge(laborCharge, selectedLaborCharge);
    }
    public void setPartCharge(PartChargeDAO partCharge, ObjectProperty<PartChargeDAO> selectedPartCharge){
        model.setPartCharge(partCharge, selectedPartCharge);
    }


}
