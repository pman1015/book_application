package org.customer_book.Pages.CustomersListPage;

import java.util.ResourceBundle;

import org.customer_book.Database.CustomerCollection.CustomerDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

@Setter
public class CustomerListCardController {

  CustomerListCardModel model;
  CustomerDAO customer;

  @FXML
  private ResourceBundle resources;

  @FXML
  private Label CustomerPhoneNumber;

  @FXML
  private Label CustomerRating;

  @FXML
  private Label CustomerName;

  @FXML
  private Label CustomerNickName;

  @FXML
  void loadCustomer(ActionEvent event) {}

  @FXML
  void initialize() {}

  public void setModel(CustomerListCardModel model) {
    this.model = model;
  
  }
  public void setCustomer(CustomerDAO customer) {
    this.customer = customer;
    CustomerName.textProperty().bind(customer.getCustomerName());
    CustomerNickName.textProperty().bind(customer.getCustomerNickName());
    CustomerPhoneNumber.textProperty().bind(customer.getCustomerPhoneNumber());
    CustomerRating.textProperty().bind(customer.getCustomerRating());
  }
}
