package org.customer_book.Popups.CustomerCreate;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;

@Getter
@Setter
@NoArgsConstructor
public class CreateCustomerModel {

  //---------- DAO to add -----------------//
  private CustomerDAO customerDAO = new CustomerDAO();

  //------------ Fields -------------------//
  private StringProperty customerName = customerDAO.getCustomerName();
  private StringProperty customerNickname = customerDAO.getCustomerNickName();
  private StringProperty customerAddress = customerDAO.getCustomerAddress();
  private StringProperty customerPhoneNumber = customerDAO.getCustomerPhoneNumber();

  //----------- Error Properties ------------//
  private StringProperty customerNameError = new SimpleStringProperty("");
  private StringProperty customerNicknameError = new SimpleStringProperty("");
  private StringProperty customerAddressError = new SimpleStringProperty("");
  private StringProperty customerPhoneNumberError = new SimpleStringProperty(
    ""
  );

  ArrayList<StringProperty> errors = new ArrayList<StringProperty>(
    Arrays.asList(
      customerNameError,
      customerNicknameError,
      customerAddressError,
      customerPhoneNumberError
    )
  );

  public void closeAddNewCustomer() {
    App.removePopup();
  }

  public void addNewCustomer() {
    boolean isValid = true;
    //Validate Fields
    customerNameError.set(customerDAO.validateName());
    customerNicknameError.set(customerDAO.validateAlias());
    customerAddressError.set(customerDAO.validateAddress());
    customerPhoneNumberError.set(customerDAO.validatePhoneNumber());
    for(StringProperty error : errors){
      if(!error.get().equals("")){
        isValid = false;
      }
    }
    if(isValid){
      customerDAO.initaliseDAO();
      DatabaseConnection.customerCollection.insertCustomer(customerDAO);
      App.removePopup();
    }
  }
}
