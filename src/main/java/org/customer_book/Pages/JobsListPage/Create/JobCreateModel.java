package org.customer_book.Pages.JobsListPage.Create;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.JobsCollection.BillDAO;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Database.MachinesCollection.MachineDAO;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class JobCreateModel {

    private static final String MachineDAO = null;
    // ----------------- View Properties ----------------------//
    // Customer Name
    private StringProperty customerName = new SimpleStringProperty("");
    private StringProperty customerNameError = new SimpleStringProperty("");

    // Equipment Name
    private StringProperty equipmentName = new SimpleStringProperty("");
    private StringProperty equipmentNameError = new SimpleStringProperty("");

    // Job Name
    private StringProperty jobName = new SimpleStringProperty("");
    private StringProperty jobNameError = new SimpleStringProperty("");

    // Job Description
    private StringProperty jobDescription = new SimpleStringProperty("");
    private StringProperty jobDescriptionError = new SimpleStringProperty("");

    // Job Status
    private Property<ObservableList<String>> customerNamesProperty = new SimpleObjectProperty<>(
            FXCollections.observableArrayList());
    private Property<ObservableList<String>> equipmentNamesProperty = new SimpleObjectProperty<>(
            FXCollections.observableArrayList());

    // ----------------- Model Properties ----------------------//
    private ArrayList<String> customerNameList = new ArrayList<>();
    private ArrayList<String> equipmentNameList = new ArrayList<>();
    private JobDAO newJob;
    private CustomerDAO customer;

    // ------------------- Constructor --------------------------//
    public JobCreateModel() {
        loadCustomerNames().start();
        customerName.addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                resetValues();
            } else {
                loadEquipmentNames().start();
            }

        });
    }

    // ------------------- View Functions ----------------------//
    public void createJob() {
        boolean valid = true;
        if (customerName.get() == null || customerName.get().isEmpty()) {
            customerNameError.set("Please select a customer");
            valid = false;
        } else {
            customerNameError.set("");
        }
        if (equipmentName.get() == null || equipmentName.get().isEmpty()) {
            equipmentNameError.set("Please select an equipment");
            valid = false;
        } else {
            equipmentNameError.set("");
        }
        if (jobName.get() == null || jobName.get().isEmpty()) {
            jobNameError.set("Please enter a job name");
            valid = false;
        } else {
            jobNameError.set("");
        }
        if (!valid) {
            return;
        }
        newJob = new JobDAO();
        MachineDAO machine = DatabaseConnection.machineCollection.getCustomerMachine(customer.getName(),
                equipmentName.get());
        if (machine == null) {
            System.out.println("Machine not found");
            return;
        }
        newJob.setBill(new BillDAO());
        newJob.setCustomerName(customerName.get());
        newJob.setDetails(jobDescription.get());
        newJob.setJobName(jobName.get());
        newJob.setMachineID(machine.getId());
        newJob.setEquipment(machine.getEquipmentId());
        newJob.setStartDate(
                java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toString());
        newJob.setEndDate("n/a");
        newJob.setStatus("InProgress");
        newJob.setCreated(new java.util.Date());
        ObjectId jobId = DatabaseConnection.jobCollection.addJob(newJob);
        DatabaseConnection.customerCollection.addJobToCustomer(customer.getId(), jobId);

        App.setBackPointer("JobsPage", "JobsPage");
        App.setSceneProperty("jobDAO", newJob);
        App.removePopup();
        App.setBackPointer("JobsPage", "JobsPage");
        App.setSceneProperty("jobDAO", newJob);
        try {
            App.setPage("CustomerJobDetailsPage", "CustomerJobDetailsPage");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeJobCreate() {
        App.removePopup();
    }

    public void resetCustomerName() {
        customerName.set("");
    }

    public void resetEquipmentName() {
        equipmentName.set("");
    }

    // ------------------- Model Functions ----------------------//
    /**
     * Refine values Takes in an ArrayList of Strings and an input String and
     * returns a new ArrayList of Strings that contain the input String returns null
     * if the input is empty or if the values contain the input
     * 
     * @param values - ArrayList of Strings to refine
     * @param input  - String to refine the values by
     * @return - ArrayList of Strings that contain the input String or null if the
     *         input is empty or if the values contain the input
     */
    public ArrayList<String> Refinevalues(ArrayList<String> values, String input) {
        if (values == null || values.contains(input)) {
            return null;
        }
        ArrayList<String> refinedValues = new ArrayList<>();
        if (input.equals(""))
            return values;
        for (String value : values) {
            if (value.toLowerCase().contains(input.toLowerCase())) {
                refinedValues.add(value);
            }
        }
        return refinedValues;
    }

    // Functions to update the values of the ObservableLists
    public void setCustomerNames(ArrayList<String> customerNames) {
        customerNamesProperty.setValue(FXCollections.observableArrayList(customerNames));
    }

    public void setEquipmentNames(ArrayList<String> refinedValues) {
        equipmentNamesProperty.setValue(FXCollections.observableArrayList(refinedValues));
    }

    /**
     * Reset the values of the model clears all eneterd values and resets the
     * available equipment
     */
    public void resetValues() {

        equipmentName.set("");
        equipmentNameList.clear();
        equipmentNamesProperty.setValue(FXCollections.observableArrayList());
        jobName.set("");
        jobDescription.set("");
    }

    // ------------------- Thread Function ----------------------//
    // Load Customer Names
    public Thread loadCustomerNames() {
        return new Thread(() -> {
            System.out.println("Loading Customer Names on " + Thread.currentThread().getName());
            customerNameList = DatabaseConnection.customerCollection.getAllNames();
            Platform.runLater(() -> {
                customerNamesProperty.setValue(FXCollections.observableArrayList(customerNameList));
            });
        });
    }

    // Load Equipment Names
    public Thread loadEquipmentNames() {
        return new Thread(() -> {
            System.out.println("Loading Equipment Names on " + Thread.currentThread().getName());
            customer = DatabaseConnection.customerCollection.findByName(customerName.get());
            if (customer == null) {
                return;
            }
            equipmentNameList = DatabaseConnection.customerCollection.getCustomerEquipment(customer);
            Platform.runLater(() -> {
                equipmentNamesProperty.setValue(FXCollections.observableArrayList(equipmentNameList));
            });
        });
    }

}
