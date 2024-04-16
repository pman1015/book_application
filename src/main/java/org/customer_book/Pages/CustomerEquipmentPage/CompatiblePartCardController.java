package org.customer_book.Pages.CustomerEquipmentPage;

import org.customer_book.Database.InventoryCollection.PartDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CompatiblePartCardController {


    @FXML
    private Label PartName;

    @FXML
    private Label Quanity;

    @FXML
    void showPart() {
        model.showInventoryPopup();

    }

    private CompatiblePartCardModel model;
    @FXML
    void initialize() {
       model = new CompatiblePartCardModel();

       PartName.textProperty().bind(model.getPartName());
       Quanity.textProperty().bind(model.getQuantity());

    }
    public void setPartDAO(PartDAO part){
        model.setPart(part);
    }
}
