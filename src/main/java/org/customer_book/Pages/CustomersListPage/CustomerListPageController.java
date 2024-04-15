package org.customer_book.Pages.CustomersListPage;

import java.util.concurrent.CompletableFuture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CustomerListPageController {

  private CustomerListPageModel model;

  @FXML
  private AnchorPane Content;

  @FXML
  private ListView<Parent> customerList;

  @FXML
  private Button NewCustomer;

  @FXML
  void createNewCustomer(ActionEvent event) {
    model.createNewCustomer();
  }

 @FXML
  void showCustomerFilter(ActionEvent event) {
    model.showCustomerFilter();
  } 

  @FXML
  void initialize() {
    model = new CustomerListPageModel();

    CompletableFuture<Void> initiliseModel = CompletableFuture.runAsync(
      this::initaliseFilterPropertiesModel
    );

    CompletableFuture.allOf(initiliseModel).join();
  }

  private void initaliseFilterPropertiesModel() {
    customerList
      .heightProperty()
      .addListener((obs, oldVal, newVal) -> {
        Node n = customerList.lookup(".scroll-bar:vertical");
        if (n instanceof javafx.scene.control.ScrollBar) {
          javafx.scene.control.ScrollBar bar = (javafx.scene.control.ScrollBar) n;
          bar
            .valueProperty()
            .addListener((obs2, oldVal2, newVal2) -> {
              if (newVal2.doubleValue() == bar.getMax()) {
                model.loadCustomerDAOs(false);
              }
            });
        }
      });
    model.loadCustomerDAOs(true);
    //Bind the customer list cards to the card list from the model
    customerList.setItems(model.getCustomerCards());
  }
}
