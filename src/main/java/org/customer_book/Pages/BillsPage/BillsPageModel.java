package org.customer_book.Pages.BillsPage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.Database.DatabaseConnection;

@Setter
@Getter
public class BillsPageModel {

  //------------- Visibility properties --------------- //
  private BooleanProperty showJobsFilter = new SimpleBooleanProperty(false);

  //------------- Card List properties --------------- //
  private ObservableList<Parent> completedJobCards = FXCollections.observableArrayList();
  private ObservableList<Parent> addedJobCards = FXCollections.observableArrayList();

  //------------- Filter properties --------------- //
  private ArrayList<String> customerNames = new ArrayList<>();
  private Property<LocalDate> completedDateJobsFilterProperty = new SimpleObjectProperty<LocalDate>();
  private Property<LocalDate> createdDateJobsFilterProperty = new SimpleObjectProperty<LocalDate>();
  private StringProperty customerNameJobsFilterProperty = new SimpleStringProperty(
    ""
  );
  private ObservableList<String> availableNamesJobsFilterProperty = FXCollections.observableArrayList();

  /**
   * Function to refine the customer name list based on the filter
   * @param enteredValue
   */
  public void updateNames(String enteredValue) {
    availableNamesJobsFilterProperty.clear();
    for (String name : customerNames) {
      if (name.toLowerCase().contains(enteredValue.toLowerCase())) {
        availableNamesJobsFilterProperty.add(name);
      }
    }
  }

  public BillsPageModel() {
    customerNames = DatabaseConnection.customerCollection.getAllNames();
    availableNamesJobsFilterProperty.addAll(customerNames);
  }
  public void hideJobsFilter() {
    showJobsFilter.set(false);
  }
  public void showJobsFilter() {
    showJobsFilter.set(true);
  }
}
