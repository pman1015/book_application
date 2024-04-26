package org.customer_book.Popups.AddCompatiblePart;


import org.customer_book.App;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class AddCompatiblePartController {

    @FXML
    private ChoiceBox<String> PartNames;

    @FXML
    void closeAdd(ActionEvent event) {
        App.removePopup();
    }

    @FXML
    void AddPart(ActionEvent event) {
        model.addPart(PartNames.getValue());
    }

    private AddCompatiblePartModel model;
    @FXML
    void initialize() {
        model = new AddCompatiblePartModel();
        PartNames.setItems(model.getPartNames());

    }
    public void setEquipment(EquipmentDAO equipment) {
        model.setEquipment(equipment);
    }
}
