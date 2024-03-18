package org.customer_book.Pages.CustomerDetailsPage;

import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.JobDAO;

@Getter
@Setter
public class CustomerDetailsPageModel {

  //-----------------Customer Details Page Modes-----------------//
  private SimpleBooleanProperty customerDetailsEditProperty;
  private SimpleBooleanProperty customerNotesEditProperty;
  //-----------------Customer Details Error Messages-----------------//
  private StringProperty customerRatingError = new SimpleStringProperty("");
  private StringProperty customerNameError = new SimpleStringProperty("");
  private StringProperty addressError = new SimpleStringProperty("");
  private StringProperty nickNameError = new SimpleStringProperty("");
  private StringProperty phoneNumberError = new SimpleStringProperty("");
  //-----------------Customer Details-----------------//
  private CustomerDAO customer;
  private ArrayList<JobDAO> jobs;
  private JobDAO selectedJob;

  public CustomerDetailsPageModel() {
    customerDetailsEditProperty = new SimpleBooleanProperty(false);
    customerNotesEditProperty = new SimpleBooleanProperty(false);
  }

  public void setCustomerDetailsEditProperty(boolean value) {
    customerDetailsEditProperty.setValue(value);
  }
  public void setCustomerNotesEditProperty(boolean value) {
    customerNotesEditProperty.setValue(value);
  }

  public void loadJobs() {
    if (customer == null) return;
    jobs = DatabaseConnection.jobCollection.getDAOs(customer.getJobIDs());
    jobs.sort((a, b) -> b.getStartDate().compareTo(a.getStartDate()));
    if(jobs.size() > 0) {
      selectedJob = jobs.get(0);
      selectedJob.initializeFXProperties();
    }

  }
}
