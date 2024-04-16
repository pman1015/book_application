package org.customer_book.Pages.CustomerEquipmentPage;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Database.MachinesCollection.MachineWorkDAO;

import javafx.beans.property.SimpleStringProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class EquipmentWorkHistoryModel {

    //--------------- View Properties ------------------//
    private StringProperty PartNameProperty = new SimpleStringProperty("");
    private StringProperty PartNumberProperty = new SimpleStringProperty("");
    private StringProperty DateProperty = new SimpleStringProperty("");

    //--------------- Model Properties ------------------//
    private MachineWorkDAO machineWork;
    

    public void setMachineWork(MachineWorkDAO machineWork){
        this.machineWork = machineWork;
        PartNameProperty.set(machineWork.getPartName());
        PartNumberProperty.set(machineWork.getPartNumber());
        DateProperty.set(machineWork.getJobDate().toString());
    }

    public void navigateToJob(){
        try {
            JobDAO job = DatabaseConnection.jobCollection.getDAO(machineWork.getJobId());
            App.setSceneProperty("jobDAO", job);
            App.setBackPointer("CustomerEquipmentPage", "CustomerEquipmentPage");
            App.setPage("CustomerJobDetailsPage", "CustomerJobDetailsPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
