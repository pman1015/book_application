package org.customer_book.Pages.CustomerJobsPage;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.JobDAO;

@Setter
@Getter
public class CustomerJobsPageCardModel {

  private JobDAO job;
  private StringProperty jobNameProperty;
  private StringProperty jobStartDateProperty;
  private StringProperty jobEndDateProperty;
  private StringProperty jobStatusProperty;
  private StringProperty jobMachineNameProperty;

  public CustomerJobsPageCardModel() {
    jobNameProperty = new SimpleStringProperty("");
    jobStartDateProperty = new SimpleStringProperty("");
    jobEndDateProperty = new SimpleStringProperty("");
    jobStatusProperty = new SimpleStringProperty("");
    jobMachineNameProperty = new SimpleStringProperty("");
  }

  public void setJob(ObjectId jobID) {
    job = DatabaseConnection.jobCollection.getDAO(jobID);
    if (job == null) return;
    jobNameProperty.setValue(job.getJobName());
    jobStartDateProperty.setValue(job.getStartDate());
    jobEndDateProperty.setValue(job.getEndDate());
    jobStatusProperty.setValue(job.getStatus());
    jobMachineNameProperty.setValue(job.getEquipmentName());
  }

  public void loadJobDetails() {
    App.setBackPointer("CustomerJobsPage", "CustomerJobPage");
    try {
      App.setSceneProperty("jobDAO", job);
      App.setPage("CustomerJobDetailsPage", "CustomerJobDetailsPage");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
