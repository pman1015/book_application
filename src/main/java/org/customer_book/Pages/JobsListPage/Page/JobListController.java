package org.customer_book.Pages.JobsListPage.Page;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollBar;

public class JobListController {
    @FXML
    private ProgressIndicator LoadingIndicator;

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
        LoadingIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        // Show the spinner until there are items in the list
        Task<Void> updateJobs = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            
                model.loadJobDAOs(true);
                return null;
            }
        };
        updateJobs.setOnSucceeded(e -> {
            LoadingIndicator.setVisible(false);
        });
        new Thread(updateJobs).start();

        
       

        JobCardList.setItems(model.getJobCardList());
      
      

        // ScrollListner
        JobCardList.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            Node n = JobCardList.lookup(".scroll-bar:vertical");
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                bar.valueProperty().addListener((obs2, oldValue, newValue) -> {
                    if (newValue.doubleValue() == bar.getMax()) {
                        model.loadJobDAOs(false);
                    }
                });
            }
        });

    }
}
