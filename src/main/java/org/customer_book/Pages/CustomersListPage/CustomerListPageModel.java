package org.customer_book.Pages.CustomersListPage;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;

@Getter
@Setter
public class CustomerListPageModel {

  private ObservableList<Parent> customerCards;

  public CustomerListPageModel() {
    customerCards = FXCollections.observableArrayList();
    loadCustomers();
  }

  private void loadCustomers() {
    customerCards.clear();
    ArrayList<CustomerDAO> customers = DatabaseConnection.customerCollection.getCustomers(
      20,
      0
    );
    customers.forEach(customer -> {
      try {
        CustomerListCardModel cardModel = new CustomerListCardModel();
        cardModel.setCustomer(customer);
        FXMLLoader customerLoader = App.getLoader(
          "CustomerListPage",
          "CustomerListCard"
        );
        customerCards.add(customerLoader.load());

        ((CustomerListCardController) customerLoader.getController()).setModel(
            cardModel
          );
        (
          (CustomerListCardController) customerLoader.getController()
        ).setCustomer(customer);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  public void createNewCustomer() {
    try {
      FXMLLoader customerLoader = App.getLoader("Popups", "CreateCustomer");
      Parent popup = customerLoader.load();
      App.addPopup(popup);
      popup
        .sceneProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue == null) {
            loadCustomers();
          }
        });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
