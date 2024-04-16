package org.customer_book.Pages.CustomerEquipmentPage;

import org.customer_book.Database.MachinesCollection.MachineWorkDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EquipmentWorkHistoryController {


    @FXML
    private Label PartName;

    @FXML
    private Label PartNumber;

    @FXML
    private Label Date;

    @FXML
    void goToJob() {
        model.navigateToJob();
    }

    private EquipmentWorkHistoryModel model;
    @FXML
    void initialize() {

       model = new EquipmentWorkHistoryModel();

       //-----------------Bind the model to the view-----------------//
       PartName.textProperty().bindBidirectional(model.getPartNameProperty());
       PartNumber.textProperty().bindBidirectional(model.getPartNumberProperty());
       Date.textProperty().bindBidirectional(model.getDateProperty());
    }
    public void setMachineWork(MachineWorkDAO machineWork){
        model.setMachineWork(machineWork);
    }
}
