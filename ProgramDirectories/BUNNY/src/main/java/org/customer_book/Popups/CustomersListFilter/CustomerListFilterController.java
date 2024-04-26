package org.customer_book.Popups.CustomersListFilter;

import org.bson.conversions.Bson;
import org.customer_book.App;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CustomerListFilterController {

 
    @FXML
    private TextField PhoneNumberField;

    @FXML
    private ComboBox<String> NickNameField;

    @FXML
    private ChoiceBox<Integer> RatingChoiceBox;

    @FXML
    private ComboBox<String> CustomerNames;

    @FXML
    void closePopup(ActionEvent event) {
        App.removePopup();
    }

    @FXML
    void ApplyFilter(ActionEvent event) {
        if(model.applyFilter()){
            App.removePopup();
        }
        
    }

    @FXML
    void ClearFilter(ActionEvent event) {
        model.clearFilter();
    }


    private CustomerListFilterModel model;
    @FXML
    void initialize() {
       this.model = new CustomerListFilterModel();

       CustomerNames.setItems(model.getCustomerNames());
       CustomerNames.valueProperty().bindBidirectional(model.getSelectedCustomerName());

        PhoneNumberField.textProperty().bindBidirectional(model.getCustomerPhoneNumber());

       NickNameField.setItems(model.getCustomerNickNames());
       NickNameField.valueProperty().bindBidirectional(model.getSelectedCustomerNickName());


       RatingChoiceBox.setItems(model.getCustomerRatings());
       RatingChoiceBox.valueProperty().bindBidirectional(model.getSelectedCustomerRating());

    }

    public void setFilter(ObjectProperty<Bson> filter){
        model.setFilter(filter);

    }
}
