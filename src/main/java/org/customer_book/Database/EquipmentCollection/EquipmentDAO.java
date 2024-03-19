package org.customer_book.Database.EquipmentCollection;

import java.util.ArrayList;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
    
    
}
