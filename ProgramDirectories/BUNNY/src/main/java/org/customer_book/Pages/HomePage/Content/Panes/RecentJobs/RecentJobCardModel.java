package org.customer_book.Pages.HomePage.Content.Panes.RecentJobs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Paint;

import java.io.IOException;

import org.customer_book.App;
import org.customer_book.Database.JobsCollection.JobDAO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

@Getter
@Setter
@NoArgsConstructor
public class RecentJobCardModel {

    // ---------------------- View Properties ----------------------//
    private StringProperty jobNameProperty = new SimpleStringProperty("");
    private StringProperty customerNameProperty = new SimpleStringProperty("");
    private StringProperty createdDateProperty = new SimpleStringProperty("");
    private StringProperty statusNameProperty = new SimpleStringProperty("");
    private ObjectProperty<Paint> statusColorProperty = new SimpleObjectProperty<>();

    // ---------------------- Model Properties ----------------------//
    private JobDAO job;

    // ---------------------- View Methods ----------------------//
    public void goToJob() {
        try {
            App.setBackPointer("HomePage", "HomePageBaseLayout");
            App.setSceneProperty("jobDAO", job);
            App.setPage("CustomerJobDetailsPage", "CustomerJobDetailsPage");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setJob(JobDAO job) {
        this.job = job;
        jobNameProperty.set(job.getJobName());
        customerNameProperty.set(job.getCustomerName());
        createdDateProperty.set(job.getStartDate());
        statusNameProperty.set(job.getStatus());
        statusColorProperty.set(job.resolveColor(job.getStatus()));
    }

    public void setPaneNumber(int paneNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPaneNumber'");
    }

}
