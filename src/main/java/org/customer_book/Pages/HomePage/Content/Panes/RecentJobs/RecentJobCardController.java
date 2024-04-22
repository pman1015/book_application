package org.customer_book.Pages.HomePage.Content.Panes.RecentJobs;

import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import org.customer_book.Database.JobsCollection.JobDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Circle;

public class RecentJobCardController {

    @FXML
    private Circle StatusIndicator;

    @FXML
    private Label JobName;

    @FXML
    private Label CreatedDate;

    @FXML
    private Tooltip StatusName;

    @FXML
    private Label CustomerName;

    @FXML
    void goToJob() {
        model.goToJob();
    }

    private RecentJobCardModel model;

    @FXML
    void initialize() {
        model = new RecentJobCardModel();
        CompletableFuture<Void> setBindings = CompletableFuture.runAsync(() -> {
            setBindings();
        });
        CompletableFuture.allOf(setBindings).join();
    }

    private void setBindings() {
        JobName.textProperty().bind(model.getJobNameProperty());
        CustomerName.textProperty().bind(model.getCustomerNameProperty());
        CreatedDate.textProperty().bind(model.getCreatedDateProperty());
        StatusName.textProperty().bind(model.getStatusNameProperty());
        StatusIndicator.fillProperty().bind(model.getStatusColorProperty());
    }

    public void setJob(JobDAO job) {
        model.setJob(job);
    }

}
