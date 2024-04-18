package org.customer_book.Popups.CustomerJobsFilter;

import static com.mongodb.client.model.Filters.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;

@Getter
@Setter
@NoArgsConstructor
public class CustomerJobsFilterModel {

  //------------- View Properties ----------------//
  private StringProperty selectedEquipmentProperty = new SimpleStringProperty(
    ""
  );
  private ArrayList<String> equipmentNames;
  private ObservableList<String> equipmentList = FXCollections.observableArrayList();

  private StringProperty selectedStatusProperty = new SimpleStringProperty("");
  private ObservableList<String> statusList = FXCollections.observableArrayList();
  private ArrayList<String> statusNames = new ArrayList<String>() {
    {
      add("Completed");
      add("Awaiting Payment");
      add("Awaiting Delivery");
      add("Awaiting Parts");
      add("In Progress");
      add("Pending");
    }
  };

  private ObjectProperty<LocalDate> startDateProperty = new SimpleObjectProperty<LocalDate>();
  private ObjectProperty<LocalDate> completedDateProperty = new SimpleObjectProperty<LocalDate>();

  private StringProperty jobNameProperty = new SimpleStringProperty("");

  //------------- Model Properties ----------------//
  private ObjectProperty<Bson> filter;
  private ArrayList<String> equipment;
  private ArrayList<String> status;
  private CustomerDAO customer;

  public void setCustomer(CustomerDAO customer) {
    this.customer = customer;
    equipmentNames =
      DatabaseConnection.customerCollection.getCustomerEquipment(customer);
    equipmentList.addAll(equipmentNames);
    statusList.addAll(statusNames);
  }

  public void ClearFilter() {
    filter.set(eq("customerName", customer.getName()));
    selectedEquipmentProperty.set("");
    selectedStatusProperty.set("");
    startDateProperty.set(null);
    completedDateProperty.set(null);
    jobNameProperty.set("");
  }

  public void ApplyFilter() {
    filter.set(
      and(
        eq("customerName", customer.getName()),
        selectedEquipmentProperty.get().equals("")
          ? new BsonDocument()
          : eq("equipmentName", selectedEquipmentProperty.get()),
        selectedStatusProperty.get().equals("")
          ? new BsonDocument()
          : eq("status", selectedStatusProperty.get()),
        startDateProperty.get() == null
          ? new BsonDocument()
          : gte("created", startDateProperty.get()),
        completedDateProperty.get() == null
          ? new BsonDocument()
          : gte("endDateTime", completedDateProperty.get()),
        jobNameProperty.get().equals("")
          ? new BsonDocument()
          : regex("jobName", jobNameProperty.get())
      )
    );
    App.removePopup();
  }

  public void setFilter(ObjectProperty<Bson> filter) {
    this.filter = filter;
    BsonArray filterArray;
    if (
      filter.get().toBsonDocument().get("$and") != null &&
      filter
        .get()
        .toBsonDocument()
        .get("$and")
        .getClass()
        .equals(BsonArray.class)
    ) {
      filterArray = filter.get().toBsonDocument().get("$and").asArray();
    } else {
      return;
    }

    filterArray.forEach(b -> {
      try {
        BsonDocument bson = b.asDocument();
        if (bson.containsKey("equipmentName")) {
          selectedEquipmentProperty.set(
            bson.getString("equipmentName").getValue()
          );
        }
        if (bson.containsKey("status")) {
          selectedStatusProperty.set(bson.getString("status").getValue());
        }
        if (bson.containsKey("created")) {
          BsonValue innerValue = getInnerValue(bson);
          if (innerValue.isDateTime()) {
            startDateProperty.set(
              convertLongToLocalDate(innerValue.asDateTime().getValue())
            );
          }
        }
        if (bson.containsKey("endDateTime")) {
          BsonValue innerValue = getInnerValue(bson);
          if (innerValue.isDateTime()) {
            completedDateProperty.set(
              convertLongToLocalDate(innerValue.asDateTime().getValue())
            );
          }
        }
        if(bson.containsKey("jobName")) {
          BsonValue innerValue = getInnerValue(bson);
          System.out.println(innerValue);
          if (innerValue.isRegularExpression()) {
            jobNameProperty.set(innerValue.asRegularExpression().getPattern());
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  private BsonValue getInnerValue(BsonDocument doc) {
    BsonValue value = null;
    while (value == null) {
      try {
        doc = doc.get(doc.getFirstKey()).asDocument();
      } catch (Exception e) {
        value = doc.get(doc.getFirstKey());
      }
    }
    return value;
  }

  public LocalDate convertLongToLocalDate(long milliseconds) {
    Instant instant = Instant.ofEpochMilli(milliseconds);
    LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
    return localDate;
  }
}
