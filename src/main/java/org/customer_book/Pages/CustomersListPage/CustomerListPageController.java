package org.customer_book.Pages.CustomersListPage;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class CustomerListPageController {

  private CustomerListPageModel model;

  @FXML
  private AnchorPane Content;

  @FXML
  private ResourceBundle resources;

  @FXML
  private Button customerSearchButton;

  @FXML
  private TextField customerSearch;

  @FXML
  private ListView<Parent> customerList;

  @FXML
  private Button NewCustomer;

  @FXML
  void createNewCustomer(ActionEvent event) {
    model.createNewCustomer();
  }

  @FXML
  void searchByName(ActionEvent event) {}

  @FXML
  void initialize() {
    model = new CustomerListPageModel();
    customerList.setItems(model.getCustomerCards());
  }
}
