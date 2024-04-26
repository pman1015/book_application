package org.customer_book.Pages.JobsListPage.Card;

import org.customer_book.Database.JobsCollection.JobDAO;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class JobCardController {

    @FXML
    private Label StartDate;

    @FXML
    private Label Status;

    @FXML
    private Label JobName;

    @FXML
    private Label CustomerName;

    @FXML
    private Label EquipmentName;

    @FXML
    void LoadJob() {
        model.loadJob();
    }

    private JobCardModel model;

    @FXML
    void initialize() {
        model = new JobCardModel();
        Platform.runLater(() -> {
            StartDate.textProperty().bind(model.getStartDate());
            Status.textProperty().bind(model.getStatus());
            JobName.textProperty().bind(model.getJobName());
            CustomerName.textProperty().bind(model.getCustomerName());
            EquipmentName.textProperty().bind(model.getEquipmentName());

        });
    }

    public void setJob(JobDAO jobDAO, ObjectProperty<JobDAO> selectedJob) {
        model.setJobDAO(jobDAO, selectedJob);
    }
}
