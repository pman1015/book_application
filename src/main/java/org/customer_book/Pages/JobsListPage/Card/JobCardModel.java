package org.customer_book.Pages.JobsListPage.Card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.customer_book.Database.JobsCollection.JobDAO;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Getter
@Setter
@NoArgsConstructor
public class JobCardModel {
    // ----------------- View Properties ----------------------//
    private StringProperty StartDate = new SimpleStringProperty("");
    private StringProperty Status = new SimpleStringProperty("");
    private StringProperty JobName = new SimpleStringProperty("");
    private StringProperty CustomerName = new SimpleStringProperty("");
    private StringProperty EquipmentName = new SimpleStringProperty("");

    // ----------------- Model Properties ----------------------//
    private JobDAO jobDAO;
    private ObjectProperty<JobDAO> selectedJobDAO;

    public void setJobDAO(JobDAO jobDAO, ObjectProperty<JobDAO> selectedJobDAO) {
        this.jobDAO = jobDAO;
        this.selectedJobDAO = selectedJobDAO;
        Platform.runLater(() -> {
            this.StartDate.set(jobDAO.getStartDate());
            this.Status.set(jobDAO.getStatus());
            this.JobName.set(jobDAO.getJobName());
            this.CustomerName.set(jobDAO.getCustomerName());
            this.EquipmentName.set(jobDAO.getEquipmentName());
        });
    }
    public void loadJob(){
        selectedJobDAO.set(jobDAO);
    }

}
