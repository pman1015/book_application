package org.customer_book.Popups.AddCompatibleEquipment;


import org.customer_book.App;
import org.customer_book.Database.InventoryCollection.PartDAO;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class AddCompatibleEquipmentController {


    @FXML
    private ChoiceBox<String> EquipmentList;

    @FXML
    void close(ActionEvent event) {
        App.removePopup();
    }

    @FXML
    void addCompatible(ActionEvent event) {
        model.addCompatible();
    }

    private AddCompatibleEquipmentModel model;
    public void setPart(ObjectProperty<PartDAO> part) {
        model.setPart(part);
    }
    @FXML
    void initialize() {
        model = new AddCompatibleEquipmentModel();

        EquipmentList.setItems(model.getEquipmentList());
        EquipmentList.valueProperty().bindBidirectional(model.getSelectedProperty());

        assert EquipmentList != null : "fx:id=\"EquipmentList\" was not injected: check your FXML file 'AddCompatibleEquipment.fxml'.";

    }
}
