package org.customer_book.Pages.BillsPage;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.Database.JobsCollection.JobDAO;

@Getter
@Setter
public class CompletedJobCardModel {

  //-------------------Properties for the label and state of the card --------------//
  private StringProperty CustomerNameProperty = new SimpleStringProperty("");
  private StringProperty JobNameProperty = new SimpleStringProperty("");
  private StringProperty CompletedDateProperty = new SimpleStringProperty("");
  private ObjectProperty<Paint> SelectedCircleProperty = new SimpleObjectProperty<Paint>(
    Color.TRANSPARENT
  );
  private BooleanProperty SelectedProperty = new SimpleBooleanProperty(false);

  //------------------- DAO for the card -----------------//
  private JobDAO jobDAO;

  //------------------- ObjectProperty for adding and removing DAO -----------------//
  private ObjectProperty<JobDAO> JobToAdd;
  private ObjectProperty<JobDAO> JobToRemove;

  public CompletedJobCardModel() {
    //Add a listner to a change on the selected property
    SelectedProperty.addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        SelectedCircleProperty.set(Color.rgb(0, 255, 0));
      } else {
        SelectedCircleProperty.set(Color.TRANSPARENT);
      }
    });
  }

  public void setJobDAO(
    JobDAO jobDAO,
    ObjectProperty<JobDAO> JobToAdd,
    ObjectProperty<JobDAO> JobToRemove
  ) {
    this.jobDAO = jobDAO;
    this.JobToAdd = JobToAdd;
    this.JobToRemove = JobToRemove;

    CustomerNameProperty.set(jobDAO.getCustomerName());
    JobNameProperty.set(jobDAO.getJobName());
    CompletedDateProperty.set(jobDAO.getEndDate());

  }

public void toggleSelected() {
    if(SelectedProperty.get()){
        SelectedProperty.set(false);
        JobToRemove.set(jobDAO);
    }else{
        SelectedProperty.set(true);
        JobToAdd.set(jobDAO);
    }
}
}
