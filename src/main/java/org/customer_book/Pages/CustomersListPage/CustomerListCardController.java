package org.customer_book.Pages.CustomersListPage;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;

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

  //---------------------------------FXML Methods---------------------------------//

  /**
   * When the CustomerListCard is clicked, the customer details page is loaded with the customer
   * details stored in the customerDAO from the card.
   * @param event
   *
   */
  @FXML
  void loadCustomer() {
    model.loadCustomerDetailsPage();
  }

  @FXML
  void initialize() {}

  public void setModel(CustomerListCardModel model) {
    this.model = model;
  }

  public void setCustomer(CustomerDAO customer) {
    this.customer = customer;
    model.setCustomer(customer);
    CustomerName.textProperty().bind(customer.getCustomerName());
    CustomerNickName.textProperty().bind(customer.getCustomerNickName());
    CustomerPhoneNumber.textProperty().bind(customer.getCustomerPhoneNumber());
    CustomerRating.textProperty().bind(customer.getCustomerRating());
  }
}
