package org.customer_book.Pages.JobsListPage.Page;

import org.bson.conversions.Bson;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.JobDAO;

import com.mongodb.internal.connection.tlschannel.impl.TlsChannelImpl.EofException;

import static com.mongodb.client.model.Filters.*;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import java.util.ArrayList;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.Pages.JobsListPage.Card.JobCardController;

@Getter
@Setter
public class JobListModel {

    // ----------------- View Properties ----------------------//
    private ObservableList<Parent> JobCardList = FXCollections.observableArrayList();

    // ----------------- Model Properties ----------------------//
    private ObjectProperty<JobDAO> selectedJobDAO = new SimpleObjectProperty<>();
    private ArrayList<JobDAO> jobsList = new ArrayList<>();
    private Bson filter = ne("_id", null);
    private int loadSize = 25;

    // -------------------- Constructor -------------------------//
    public JobListModel() {
        selectedJobDAO.addListener((obs, oldJob, newJob) -> {
            if (newJob != null) {
                try {
                    App.setBackPointer("JobsPage", "JobsPage");
                    App.setSceneProperty("jobDAO", newJob);
                    App.setPage("CustomerJobDetailsPage", "CustomerJobDetailsPage");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ------------------- View Functions ----------------------//
    public void showFilter() {
        // TODO implement here
    }

    public void showAddJob() {
        // TODO implement here
    }

    // ------------------- Model Functions ----------------------//
    /**
     * Load the jobDAOs from the database
     * 
     * @param clear - if true, clear the current list of jobs
     */
    public void loadJobDAOs(boolean clear) {

        if (clear) {
            jobsList = DatabaseConnection.jobCollection.getJobs(filter, loadSize, 0);
        } else {
            jobsList.addAll(DatabaseConnection.jobCollection.getJobs(filter, loadSize, jobsList.size()));
        }
        Platform.runLater(() -> {
            loadJobCards();
        });
    }

    /**
     * Load a card and add it to the list for each job in the jobsList
     */
    private void loadJobCards() {
        JobCardList.clear();
        for (JobDAO jobDAO : jobsList) {
            try {
                FXMLLoader cardLoader = App.getLoader("JobsPage", "JobCard");
                Parent card = cardLoader.load();
                ((JobCardController) cardLoader.getController()).setJob(jobDAO, selectedJobDAO);
                JobCardList.add(card);
            }
            catch (Exception e) {
                System.out.println("Error loading job card: " + e.getMessage());
            }
        }
    }

}