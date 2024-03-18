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
    this.customerName = new SimpleStringProperty();
    this.customerNickName = new SimpleStringProperty();
    this.customerPhoneNumber = new SimpleStringProperty();
    this.customerRating = new SimpleStringProperty();
    this.customerAddress = new SimpleStringProperty();
    this.customerNotes = new SimpleStringProperty();
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
}
