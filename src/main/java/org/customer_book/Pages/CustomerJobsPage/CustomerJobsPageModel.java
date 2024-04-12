package org.customer_book.Pages.CustomerJobsPage;

import java.io.IOException;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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
public class CustomerJobsPageModel {

  private StringProperty customerName;
  private CustomerDAO customer;
  private ObservableList<Parent> jobCards;

  public CustomerJobsPageModel() {
    customer = (CustomerDAO) App.getSceneProperty("customerDAO");
    if (customer == null) goBack();
    customer =
      DatabaseConnection.customerCollection.findCustomerById(customer.getId());
    customerName = customer.getCustomerName();
    jobCards = FXCollections.observableArrayList();
    loadJobs();
  }

  private void loadJobs() {
    jobCards.clear();
    customer
      .getJobIDs()
      .forEach(jobID -> {
        Parent jobCard;
        try {
          FXMLLoader jobCardLoader = App.getLoader(
            "CustomerJobsPage",
            "CustomerJobCard"
          );
          jobCard = jobCardLoader.load();
          (
            (CustomerJobsPageCardController) jobCardLoader.getController()
          ).setJobID(jobID);
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
            loadJobs();
          }
        });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
