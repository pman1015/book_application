package org.customer_book.Pages.CustomerJobsPage;

import java.io.IOException;

import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.CustomerCollection.CustomerDAO;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class CustomerJobsPageModel {
    private StringProperty customerName;
    private CustomerDAO customer;
    private ObservableList<Parent> jobCards;
    

    public CustomerJobsPageModel() {
        customer = (CustomerDAO) App.getSceneProperty("customerDAO");
        if(customer == null) goBack();
        customer = DatabaseConnection.customerCollection.findCustomerById(customer.getId());
        customerName = customer.getCustomerName();
        jobCards = FXCollections.observableArrayList();
        loadJobs();

    }
    private void loadJobs(){
        jobCards.clear();
        customer.getJobIDs().forEach(jobID -> {
            Parent jobCard;
            try {
                FXMLLoader jobCardLoader = App.getLoader("CustomerJobsPage","CustomerJobCard");
                jobCard = jobCardLoader.load();
                ((CustomerJobsPageCardController)jobCardLoader.getController()).setJobID(jobID);
                jobCards.add(jobCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
           
        });
    }


    private void goBack(){
        App.useBackPointer();
    }
}
