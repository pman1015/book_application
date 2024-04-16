package org.customer_book.Database.MachinesCollection;

import java.util.ArrayList;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentCollection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.InventoryCollection.PartDAO;
import org.customer_book.Database.JobsCollection.JobDAO;

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
public class MachineDAO {
    //--------------------DAO atributs--------------------//
    private ObjectId id;
    private String customerName;
    private ObjectId equipmentId;
    private String lastWorkedOn;
    private String notes;
    private ArrayList<MachineWorkDAO> workHistory;

    //--------------------FXML Attributes----------------//
    @BsonIgnore
    private StringProperty machineCustomerNameProperty = new SimpleStringProperty();
    @BsonIgnore
    private StringProperty lastWorkedOStringProperty = new SimpleStringProperty();
    @BsonIgnore
    private StringProperty machineNotesProperty = new SimpleStringProperty();

    //--------------------Computed Values ---------------//
    @BsonIgnore
    private StringProperty modelNumberStringProperty = new SimpleStringProperty();
    @BsonIgnore
    private ArrayList<PartDAO> parts = new ArrayList<PartDAO>();


    //--------------------Setters ----------------------//
    public void setEquipmentId(ObjectId equipmentId) {
        this.equipmentId = equipmentId;
        EquipmentDAO  eq = DatabaseConnection.equipmentCollection.getEquipment(equipmentId);
        //Exit early if equipment is not found
        if(eq == null) return;
        this.modelNumberStringProperty.set(eq.getModelNumber());
        //TODO: Add parts to machine

    }
    public void setLastWorkedOn(String lastWorkedOn) {
        this.lastWorkedOn = lastWorkedOn;
        this.lastWorkedOStringProperty.set(lastWorkedOn);
    }

    public void setNotes(String notes) {
        this.notes = notes;
        this.machineNotesProperty.set(notes);
    }

    @BsonIgnore
    public void updateWork(JobDAO job){
        //Initialise the work history if it is null
        if(this.workHistory == null){
            this.workHistory = new ArrayList<MachineWorkDAO>();
        }
        job.getBill().getPartCharges().forEach(charge -> {
            MachineWorkDAO work = new MachineWorkDAO();
            work.setJobDate(job.getEndDateTime());
            work.setJobId(job.getId());
            work.setPartName(charge.getPartName());
            work.setPartNumber(charge.getPartNumber());
            this.workHistory.add(work);
        });
       this.lastWorkedOn = job.getEndDate();
    }


}
