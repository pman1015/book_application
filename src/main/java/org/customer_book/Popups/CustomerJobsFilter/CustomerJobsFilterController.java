package org.customer_book.Popups.CustomerJobsFilter;

import org.bson.conversions.Bson;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class CustomerJobsFilterController {


    @FXML
    private DatePicker StartDatePicker;

    @FXML
    private DatePicker CompletedDatePicker;

    @FXML
    private ComboBox<String> EquipmentComboBox;

    @FXML
    private ComboBox<String> StatusComboBox;

    @FXML
    void closePopup(ActionEvent event) {
        App.removePopup();
    }

    @FXML
    void ApplyFilter(ActionEvent event) {
        model.ApplyFilter();
    }

    @FXML
    void ClearFilter(ActionEvent event) {
        model.ClearFilter();

    }

    private CustomerJobsFilterModel model;
    @FXML
    void initialize() {
        model = new CustomerJobsFilterModel();

        EquipmentComboBox.valueProperty().bindBidirectional(model.getSelectedEquipmentProperty());
        EquipmentComboBox.setItems(model.getEquipmentList());

        StatusComboBox.valueProperty().bindBidirectional(model.getSelectedStatusProperty());
        StatusComboBox.setItems(model.getStatusList());

        StartDatePicker.valueProperty().bindBidirectional(model.getStartDateProperty());

        CompletedDatePicker.valueProperty().bindBidirectional(model.getCompletedDateProperty());
    }

    public void setCustomer(ObjectProperty<Bson>filter, CustomerDAO customer){
        model.setCustomer(customer);
        model.setFilter(filter);
    }
}
