package org.customer_book.Database.CustomerCollection;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.customer_book.Database.DatabaseConnection;

@Getter
@Setter
@ToString
public class CustomerDAO {

  private ObjectId id;
  private String name;
  private String alias;
  private String notes;
  private String phoneNumber;
  private String address;
  private int rating;
  private ArrayList<ObjectId> jobIDs;
  private ArrayList<ObjectId> machineIDs;

  @BsonIgnore
  private StringProperty customerName;

  @BsonIgnore
  private StringProperty customerNickName;

  @BsonIgnore
  private StringProperty customerPhoneNumber;

  @BsonIgnore
  private StringProperty customerRating;

  @BsonIgnore
  private StringProperty customerAddress;

  @BsonIgnore
  private StringProperty customerNotes;

  public CustomerDAO() {
    this.customerName = new SimpleStringProperty("");
    this.customerNickName = new SimpleStringProperty("");
    this.customerPhoneNumber = new SimpleStringProperty("");
    this.customerRating = new SimpleStringProperty("");
    this.customerAddress = new SimpleStringProperty("");
    this.customerNotes = new SimpleStringProperty("");
  }

  public void setName(String name) {
    this.name = name;
    this.customerName.set(name);
  }

  public void setAlias(String alias) {
    this.alias = alias;
    this.customerNickName.set(alias);
  }

  public void setNotes(String notes) {
    this.notes = notes;
    this.customerNotes.set(notes);
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    this.customerPhoneNumber.set(phoneNumber);
  }

  public void setAddress(String address) {
    this.address = address;
    this.customerAddress.set(address);
  }

  public void setRating(int rating) {
    this.rating = rating;
    this.customerRating.set(String.valueOf(rating) + "/5");
  }

  public void addMachine(ObjectId addedMachineID) {
    this.machineIDs.add(addedMachineID);
  }

  @BsonIgnore
  public void initaliseDAO() {
    name = customerName.get();
    alias = customerNickName.get();
    notes = customerNotes.get();
    phoneNumber = customerPhoneNumber.get();
    address = customerAddress.get();
    rating = 3;
    jobIDs = new ArrayList<>();
    machineIDs = new ArrayList<>();
  }

  @BsonIgnore
  public String validateName() {
    if (customerName.get().isEmpty()) {
      return "Name cannot be empty";
    }
    return (DatabaseConnection.customerCollection.findByName(name) == null)
      ? ""
      : "Name already exists";
  }

  @BsonIgnore
  public String validateAlias() {
    if (customerNickName.get().isEmpty()) {
      return "Alias cannot be empty";
    }
    return "";
  }

  @BsonIgnore
  public String validatePhoneNumber() {
    if (customerPhoneNumber.get().isEmpty()) {
      return "Phone number cannot be empty";
    }
    return "";
  }

  @BsonIgnore
  public String validateAddress() {
    if (customerAddress.get().isEmpty()) {
      return "Address cannot be empty";
    }
    return "";
  }
}
