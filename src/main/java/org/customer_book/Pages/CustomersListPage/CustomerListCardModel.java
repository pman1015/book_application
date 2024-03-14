package org.customer_book.Pages.CustomersListPage;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import lombok.Getter;
import lombok.Setter;

import org.customer_book.Database.CustomerCollection.CustomerDAO;

@Getter
@Setter
public class CustomerListCardModel {

  private CustomerDAO customer;
 

  public CustomerListCardModel() {
   
  }
  
  
 
}
