package org.customer_book.Pages.CustomersListPage;

import static com.mongodb.client.model.Filters.ne;

import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import org.bson.conversions.Bson;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Popups.CustomersListFilter.CustomerListFilterController;
import org.customer_book.Popups.CustomersListFilter.CustomerListFilterModel;

@Getter
@Setter
public class CustomerListPageModel {

  private ObservableList<Parent> customerCards;
  private ArrayList<CustomerDAO> customers = new ArrayList<>();
  private ObjectProperty<Bson> filter = new SimpleObjectProperty<>();

  public CustomerListPageModel() {
    customerCards = FXCollections.observableArrayList();
    filter.set(ne("_id", "null"));
  }

  public void loadCustomerDAOs(boolean clear) {
    if (clear) {
      customers =
        DatabaseConnection.customerCollection.getFilteredCustomers(
          filter.get(),
          12,
          0
        );
    } else {
      customers.addAll(
        DatabaseConnection.customerCollection.getFilteredCustomers(
          filter.get(),
          12,
          customers.size()
        )
      );
    }
    loadCustomers();
  }

  public void loadCustomers() {
    customerCards.clear();
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

  public void showCustomerFilter() {
    try {
      FXMLLoader customerLoader = App.getLoader("Popups", "CustomerFilter");
      Parent popup = customerLoader.load();
      ((CustomerListFilterController) customerLoader.getController()).setFilter(
          filter
        );
      App.addPopup(popup);
      popup
        .sceneProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue == null) {
            loadCustomerDAOs(true);
          }
        });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
