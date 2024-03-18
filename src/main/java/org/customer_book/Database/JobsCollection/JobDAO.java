package org.customer_book.Database.JobsCollection;

import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.customer_book.Database.JobsCollection.LaobrChargeDAO;

@Getter
@Setter
@ToString
public class JobDAO {

  //-----------------Job DAO Serilizable Values-----------------//
  private ObjectId id;
  private BillDAO bill;
  private Date created;
  private String customerName;
  private String details;
  private String startDate;
  private String endDate;
  private String status;
  private ObjectId machineIDs;
  private ObjectId equipment;
  private String jobName;

  //-----------------Job DAO JavaFX Properties-----------------//
  @BsonIgnore
  private StringProperty jobNameProperty;

  @BsonIgnore
  private StringProperty equipmentNameProperty;

  @BsonIgnore
  private StringProperty currentHoursProperty;

  @BsonIgnore
  private StringProperty currentCostProperty;

  @BsonIgnore
  private StringProperty jobNotesProperty;

  @BsonIgnore
  private ObjectProperty<Paint> jobStatusProperty;

  public void initializeFXProperties() {
    jobNameProperty = new SimpleStringProperty(jobName);
    equipmentNameProperty = new SimpleStringProperty(equipment.toString());
    jobNotesProperty = new SimpleStringProperty(details);
    currentCostProperty =
      new SimpleStringProperty(String.valueOf(bill.getBillTotal()));
    double hours = 0;
    for (LaobrChargeDAO labor : bill.getLaborCharges()) {
      hours += labor.getHours();
    }
    currentHoursProperty = new SimpleStringProperty(String.valueOf(hours));
    jobStatusProperty = new SimpleObjectProperty<Paint>(resolveColor(status));
  }

  public Paint resolveColor(String status) {
    switch (status) {
      case "InProgress":
        return Paint.valueOf("#FFD700");
      default:
        return Paint.valueOf("#00FF00");
    }
  }
}
