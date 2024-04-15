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

  //Customer Card Properties
  private StringProperty customerName = new SimpleStringProperty("");
  private StringProperty customerNickName = new SimpleStringProperty("");
  private StringProperty customerPhoneNumber = new SimpleStringProperty("");
  private StringProperty customerRating = new SimpleStringProperty("");

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

  /**
   * update the string propertes of the card with the customerDAO
   */
  public void setCustomer(CustomerDAO customerDAO){
    this.customer = customerDAO;
    customerName.set(customer.getCustomerName().get());
    customerNickName.set(customer.getCustomerNickName().get());
    customerPhoneNumber.set(customer.getCustomerPhoneNumber().get());
    customerRating.set(customer.getCustomerRating().get());
  }

  
}
