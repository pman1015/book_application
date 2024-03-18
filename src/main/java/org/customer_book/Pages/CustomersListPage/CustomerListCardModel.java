package org.customer_book.Pages.CustomersListPage;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import lombok.Getter;
import lombok.Setter;

import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Pages.CustomerDetailsPage.CustomerDetailsPageController;
@Getter
@Setter
public class CustomerListCardModel {

  private CustomerDAO customer;

  public CustomerListCardModel() {}

  /**
   * When the CustomerListCard is clicked, the customer details page is loaded with the customer
   * @return
   */
  public void loadCustomerDetailsPage() {
    try {
      FXMLLoader customerDetailsLoader = App.getLoader("CustomerDetailsPage","CustomerDetailsPage" );
      App.setPage(customerDetailsLoader.load());
      ((CustomerDetailsPageController) customerDetailsLoader.getController()).setCustomerDAO(customer);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  
}
