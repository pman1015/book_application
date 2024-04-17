package org.customer_book.Popups.JobCreate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.JobsCollection.BillDAO;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Database.MachinesCollection.MachineDAO;

@Getter
@Setter
public class JobCreateModel {

  private StringProperty jobName;
  private StringProperty machineName;
  private StringProperty jobDescription;

  private StringProperty jobNameError;

  private StringProperty machineError;

  private ObservableList<String> machineNames;
  private CustomerDAO customer;
  private HashMap<String, ObjectId[]> machineMap;

  public JobCreateModel() {
    jobName = new SimpleStringProperty("");
    machineName = new SimpleStringProperty("");
    jobDescription = new SimpleStringProperty("");

    machineNames = FXCollections.observableArrayList();

    jobNameError = new SimpleStringProperty("");

    machineError = new SimpleStringProperty("");
    machineMap = new HashMap<>();
  }

  public void setCustomer(CustomerDAO customer) {
    this.customer = customer;
    for (ObjectId machine : customer.getMachineIDs()) {
      MachineDAO machineDAO = DatabaseConnection.machineCollection.getMachineDAObyId(
        machine
      );
      if (!machineDAO.isArchived()) {
        ObjectId equipmentID = machineDAO.getEquipmentId();
        EquipmentDAO equipment = DatabaseConnection.equipmentCollection.getEquipment(
          equipmentID
        );
        machineNames.add(equipment.getModelNumber());
        machineMap.put(
          equipment.getModelNumber(),
          new ObjectId[] { machine, equipmentID }
        );
      }
    }
  }

  public void addNewJob() {
    if (verifyJob()) {
      JobDAO newJob = new JobDAO();
      newJob.setBill(new BillDAO());
      newJob.setCustomerName(customer.getName());
      newJob.setDetails(jobDescription.get());
      newJob.setJobName(jobName.get());
      newJob.setMachineID(machineMap.get(machineName.get())[0]);
      newJob.setEquipment(machineMap.get(machineName.get())[1]);
      newJob.setStartDate(
        java.time.LocalDate
          .now()
          .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
          .toString()
      );
      newJob.setEndDate("n/a");
      newJob.setStatus("InProgress");
      newJob.setCreated(new java.util.Date());
      ObjectId jobId = DatabaseConnection.jobCollection.addJob(newJob);
      DatabaseConnection.customerCollection.addJobToCustomer(
        customer.getId(),
        jobId
      );
      App.removePopup();
    }
  }

  public boolean verifyJob() {
    boolean valid = true;
    if (jobName.get().isEmpty()) {
      jobNameError.set("Job Name is required");
      valid = false;
    } else {
      jobNameError.set("");
    }
    if (machineName.get().isEmpty()) {
      machineError.set("Machine Name is required");
      valid = false;
    } else {
      machineError.set("");
    }
    return valid;
  }
}
