package org.customer_book.Pages.JobsListPage.Page;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

public class JobListController {
    @FXML
    private ListView<Parent> JobCardList;

    @FXML
    void showFilter(ActionEvent event) {
        model.showFilter();
    }

    @FXML
    void showAddJob(ActionEvent event) {
        model.showAddJob();
    }

    private JobListModel model;

    @FXML
    void initialize() {
        model = new JobListModel();
        JobCardList.setItems(model.getJobCardList());
        Platform.runLater(() -> {
            model.loadJobDAOs(true);
        });

    }
}
