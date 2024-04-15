package org.customer_book.Popups.CustomersListFilter;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.conversions.Bson;
import org.customer_book.Database.DatabaseConnection;

@Getter
@Setter
public class CustomerListFilterModel {

  //-----------------------------Properties---------------------------------//
  private ObservableList<String> customerNames = FXCollections.observableArrayList();
  private StringProperty selectedCustomerName = new SimpleStringProperty("");

  private StringProperty customerPhoneNumber = new SimpleStringProperty("");

  private ObservableList<String> customerNickNames = FXCollections.observableArrayList();
  private StringProperty selectedCustomerNickName = new SimpleStringProperty(
    ""
  );

  private ObservableList<Integer> customerRatings = FXCollections.observableArrayList();
  private Property<Integer> selectedCustomerRating = new SimpleObjectProperty<>(
    0
  );

  private ObjectProperty<Bson> filter;

  private ArrayList<String> customerNamesList = new ArrayList<>();
  private ArrayList<String> customerNickNamesList = new ArrayList<>();

  @SuppressWarnings("unchecked")
  public CustomerListFilterModel() {
    Object[] namesAndNickNames = DatabaseConnection.customerCollection.getNamesandNickNames();

    customerNamesList = (ArrayList<String>) namesAndNickNames[0];
    customerNickNamesList = (ArrayList<String>) namesAndNickNames[1];

    customerNames.setAll(customerNamesList);
    customerNickNames.setAll(customerNickNamesList);

    loadRatings();
  }

  public boolean applyFilter() {
    boolean valid = true;
    Bson customerFilter = null;
    if (!selectedCustomerName.get().equals("")) {
      customerFilter = regex("name", selectedCustomerName.get());
    }
    if (!selectedCustomerNickName.get().equals("")) {
      if (customerFilter != null) {
        customerFilter =
          and(customerFilter, regex("alias", selectedCustomerNickName.get()));
      } else {
        customerFilter = regex("alias", selectedCustomerNickName.get());
      }
    }
    if (((ObjectPropertyBase<Integer>) selectedCustomerRating).get() != 0) {
      if (customerFilter != null) {
        customerFilter =
          and(
            customerFilter,
            eq(
              "rating",
              ((ObjectPropertyBase<Integer>) selectedCustomerRating).get()
            )
          );
      } else {
        customerFilter =
          eq(
            "rating",
            ((ObjectPropertyBase<Integer>) selectedCustomerRating).get()
          );
      }
    }
    if (!customerPhoneNumber.get().equals("")) {
      formatAsPhoneNumber();
      if (customerFilter != null) {
        customerFilter =
          and(customerFilter, eq("phoneNumber", customerPhoneNumber.get()));
      } else {
        customerFilter = eq("phoneNumber", customerPhoneNumber.get());
      }
    }
    if (customerFilter != null) {
      filter.set(customerFilter);
    }else{
      filter.set(ne("_id", "null"));
    }

    return valid;
  }

  private void formatAsPhoneNumber() {
    String number = customerPhoneNumber.get();
    if (number.length() == 10) {
      number =
        "(" +
        number.substring(0, 3) +
        ") " +
        number.substring(3, 6) +
        "-" +
        number.substring(6);
    }
    customerPhoneNumber.set(number);
  }

  public void clearFilter() {
    selectedCustomerName.set("");
    selectedCustomerNickName.set("");
    ((ObjectPropertyBase<Integer>) selectedCustomerRating).set(0);
    customerPhoneNumber.set("");
  }

  private void loadRatings() {
    customerRatings.clear();
    for (int i = 0; i <= 5; i++) {
      customerRatings.add(i);
    }
  }
}
