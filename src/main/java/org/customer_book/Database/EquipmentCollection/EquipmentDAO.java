package org.customer_book.Database.EquipmentCollection;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
public class EquipmentDAO {

  //--------------------DAO atributs--------------------//
  private ObjectId id;
  private String modelNumber;
  private String notes;
  private ArrayList<ObjectId> parts;

  //--------------------FXML Attributes----------------//
  @BsonIgnore
  private StringProperty equipmentModelNumberProperty = new SimpleStringProperty();

  @BsonIgnore
  private StringProperty equipmentNotesProperty = new SimpleStringProperty();

  //--------------------Setters ----------------------//

  public void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
    this.equipmentModelNumberProperty.set(modelNumber);
  }

  public void setNotes(String notes) {
    this.notes = notes;
    this.equipmentNotesProperty.set(notes);
  }

  public EquipmentDAO() {
    equipmentModelNumberProperty.addListener(
      new ChangeListener<String>() {
        @Override
        public void changed(
          ObservableValue<? extends String> observable,
          String oldValue,
          String newValue
        ) {
          setModelNumber(newValue);
        }
      }
    );
    equipmentNotesProperty.addListener(
      new ChangeListener<String>() {
        @Override
        public void changed(
          ObservableValue<? extends String> observable,
          String oldValue,
          String newValue
        ) {
          setNotes(newValue);
        }
      }
    );
  }
}
