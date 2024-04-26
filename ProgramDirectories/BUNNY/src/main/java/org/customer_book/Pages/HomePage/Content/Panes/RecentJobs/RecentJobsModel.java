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
import org.customer_book.Pages.HomePage.Content.Panes.PaneModel;

@Getter
@Setter
public class RecentJobsModel extends PaneModel {

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
    if (clear) {
      jobs = DatabaseConnection.jobCollection.getJobs(filter, 12, 0);
    } else {
      jobs.addAll(DatabaseConnection.jobCollection.getJobs(filter, 12, jobs.size()));
    }

    Platform.runLater(() -> {
      loadJobCards();
    });

  }

  public void loadJobCards() {
    RecentJobsList.clear();

    for (JobDAO job : jobs) {

      try {
        FXMLLoader cardLoader = App.getLoader("HomePage/PaneContent/RecentJobs", "RecentJobCard");
        Parent card = cardLoader.load();
        ((HBox) card).prefWidthProperty().bind(jobCardWidth);
        ((RecentJobCardController) cardLoader.getController()).setJob(job);
        RecentJobsList.add(card);
      }
      catch (Exception e) {
        e.printStackTrace();
      }

    }
  }

  public void setJobCardWidth(double width) {
    jobCardWidth.set(width - 32);
  }

}
