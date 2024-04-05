package org.customer_book.Pages.BillsPage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.JobDAO;

@Setter
@Getter
public class BillsPageModel {

  //------------- Create Invoice Properties ---------------//
  private StringProperty InvoiceNumber = new SimpleStringProperty("");
  private StringProperty CustomerName = new SimpleStringProperty("");
  private StringProperty InvoiceTotal = new SimpleStringProperty("");

  //------------- Visibility properties --------------- //
  private BooleanProperty showJobsFilter = new SimpleBooleanProperty(false);
  private BooleanProperty showAddJobError = new SimpleBooleanProperty(false);
  //------------- Card List properties --------------- //
  private ObservableList<Parent> completedJobCards = FXCollections.observableArrayList();
  private ObservableList<Parent> addedJobCards = FXCollections.observableArrayList();

  //-------------- AddingAndRemovingJobs properties --------------- //
  private ObjectProperty<JobDAO> jobToAdd = new SimpleObjectProperty<>();
  private ObjectProperty<JobDAO> jobToRemove = new SimpleObjectProperty<>();

  private ArrayList<JobDAO> addedJobs = new ArrayList<>();

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
    loadCompletedJobs();
    //Add Listner for when toAdd or toRemove is changed
    jobToAdd.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        if (
          newValue.getCustomerName().equals(CustomerName.get()) ||
          CustomerName.get().isEmpty()
        ) {
          CustomerName.set(newValue.getCustomerName());
          addedJobs.add(newValue);
          loadBillCards();
        } else {
          showAddJobError.set(true);
          jobToRemove.set(newValue);
        }
      }
    });
    jobToRemove.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        if (addedJobs.remove(newValue)) {
          loadBillCards();
        }
      }
    });
  }

  private String generateRandomNumber() {
    Random random = new Random();
    int randomNumber = random.nextInt(900000) + 100000;
    return String.valueOf(randomNumber);
  }

  private void loadBillCards() {
    double total = 0;
    if (InvoiceNumber.get().isEmpty()) {
      InvoiceNumber.set("INV" + generateRandomNumber());
    }
    addedJobCards.clear();
    for (JobDAO job : addedJobs) {
      total += job.getBill().getBillTotal();
      try {
        FXMLLoader cardLoader = App.getLoader("BillsPage", "JobInBillCard");
        Parent card = cardLoader.load();
        ((BillJobCardView) cardLoader.getController()).setJobDAO(
            job,
            jobToRemove
          );
        addedJobCards.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    InvoiceTotal.set("$" + total);
    jobToAdd.set(null);
    jobToRemove.set(null);
  }

  public void hideJobsFilter() {
    showJobsFilter.set(false);
  }

  public void showJobsFilter() {
    showJobsFilter.set(true);
  }

  public void loadCompletedJobs() {
    completedJobCards.clear();
    ArrayList<JobDAO> completedJobs = DatabaseConnection.jobCollection.getCompletedJobs();
    for (JobDAO job : completedJobs) {
      try {
        FXMLLoader cardLoader = App.getLoader("BillsPage", "CompletedJobCard");
        Parent card = cardLoader.load();
        ((CompletedJobCardView) cardLoader.getController()).setJobDAO(
            job,
            jobToAdd,
            jobToRemove
          );
        completedJobCards.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
