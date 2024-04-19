package org.customer_book.Pages.CustomerJobsPage;

import static com.mongodb.client.model.Filters.*;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import org.bson.conversions.Bson;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.JobDAO;

@Getter
@Setter
public class CustomerJobsPageModel {

  private StringProperty customerName;
  private CustomerDAO customer;

  //------------- Job Cards ----------------//
  private ObservableList<Parent> jobCards = FXCollections.observableArrayList();
  private ArrayList<JobDAO> jobList = new ArrayList<>();
  private Bson filter;
  private ObjectProperty<Bson> filterProperty = new SimpleObjectProperty<>();

  public CustomerJobsPageModel() {
    customer = (CustomerDAO) App.getSceneProperty("customerDAO");
    if (customer == null) goBack();
    customer =
      DatabaseConnection.customerCollection.findCustomerById(customer.getId());
    customerName = customer.getCustomerName();
    filter = eq("customerName", customerName.get());
    System.out.println("Filter: " + filter.toString());
    filterProperty.set(filter);
    filterProperty.addListener((observable, oldValue, newValue) -> {
      filter = newValue;
      System.out.println("New Filter:" + newValue.toString());
      loadJobs(true);
    });
    loadJobs(true);
  }

  private void loadJobs(boolean clear) {
    if (clear) {
      jobList =
        DatabaseConnection.jobCollection.getJobs(filter, 12, 0);
    } else {
      jobList.addAll(
        DatabaseConnection.jobCollection.getJobs(
          filter,
          12,
          jobList.size()
        )
      );
    }
    loadCards();
  }

  private void loadCards() {
    jobCards.clear();
    jobList.forEach(job -> {
      Parent jobCard;
      try {
        FXMLLoader jobCardLoader = App.getLoader(
          "CustomerJobsPage",
          "CustomerJobCard"
        );
        jobCard = jobCardLoader.load();
        (
          (CustomerJobsPageCardController) jobCardLoader.getController()
        ).setJobID(job.getId());
        jobCards.add(jobCard);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private void goBack() {
    App.useBackPointer();
  }

  public void showCreateNewJob() {
    try {
      FXMLLoader jobCreateLoader = App.getLoader("Popups", "CreateJob");
      Parent loadedPage = jobCreateLoader.load();
      (
        (org.customer_book.Popups.JobCreate.JobCreateController) jobCreateLoader.getController()
      ).setCustomer(customer);
      App.addPopup(loadedPage);
      loadedPage
        .sceneProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue == null) {
            customer =
              DatabaseConnection.customerCollection.findCustomerById(
                customer.getId()
              );
            loadJobs(true);
          }
        });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showFilterOptions() {
    try {
      FXMLLoader filterLoader = App.getLoader("Popups", "CustomerJobsFilter");
      Parent filter = filterLoader.load();
      (
        (org.customer_book.Popups.CustomerJobsFilter.CustomerJobsFilterController) filterLoader.getController()
      ).setCustomer(filterProperty, customer);
      App.addPopup(filter);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
