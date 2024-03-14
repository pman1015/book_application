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
}
