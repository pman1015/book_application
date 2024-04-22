package org.customer_book.Pages.HomePage.Content.Panes.RecentJobs;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javax.sound.midi.Soundbank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.conversions.Bson;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.JobDAO;

@Getter
@Setter
public class RecentJobsModel {

  // ---------------------- View Properties ----------------------//
  private ObservableList<Parent> RecentJobsList = FXCollections.observableArrayList();
  private DoubleProperty jobCardWidth = new SimpleDoubleProperty();

  // ---------------------- Model Properties ----------------------//
  private ArrayList<JobDAO> jobs = new ArrayList<>();
  private Bson filter = ne("_id", null);

  public RecentJobsModel() {
    jobCardWidth.addListener((obs, oldVal, newVal) -> {
      loadJobCards();
    });
  }

  public void loadJobs(boolean clear) {
    Platform.runLater(() -> {
      if (clear) {
        //long jobloadStart = System.currentTimeMillis();
        //System.out.println("Loading Jobs...");
        jobs = DatabaseConnection.jobCollection.getJobs(filter, 12, 0);
        //System.out.println(
        //  "Jobs Loaded in: " + (System.currentTimeMillis() - jobloadStart) + "ms");
      } else {
        jobs.addAll(
          DatabaseConnection.jobCollection.getJobs(filter, 12, jobs.size())
        );
      }
      loadJobCards();
    });
  }

  public void loadJobCards() {
    RecentJobsList.clear();
    long jobcardLoadStart = System.currentTimeMillis();
    for (JobDAO job : jobs) {
      Platform.runLater(() -> {
        try {
          FXMLLoader cardLoader = App.getLoader(
            "HomePage/PaneContent/RecentJobs",
            "RecentJobCard"
          );
          Parent card = cardLoader.load();
          ((HBox) card).prefWidthProperty().bind(jobCardWidth);
          ((RecentJobCardController) cardLoader.getController()).setJob(job);
          RecentJobsList.add(card);
        } catch (Exception e) {
          e.printStackTrace();
        }
        System.out.println(
          "Job Card Loaded in: " +
          (System.currentTimeMillis() - jobcardLoadStart) +
          "ms"
        );
      });
    }
  }

  public void setJobCardWidth(double width) {
    jobCardWidth.set(width - 32);
  }
}
