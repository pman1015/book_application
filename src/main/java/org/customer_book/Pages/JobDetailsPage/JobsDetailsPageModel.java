package org.customer_book.Pages.JobDetailsPage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.InventoryCollection.PartDAO;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Database.JobsCollection.LaborChargeDAO;
import org.customer_book.Database.JobsCollection.PartChargeDAO;
import org.customer_book.Database.MachinesCollection.MachineDAO;
import org.customer_book.Database.ReportsCollection.ReportDAO;

@Getter
@Setter
public class JobsDetailsPageModel {

  //-----------------Job Details Page Properties-----------------//
  private StringProperty jobName = new SimpleStringProperty("");
  private StringProperty jobStartDate = new SimpleStringProperty("");
  private StringProperty jobEndDate = new SimpleStringProperty("");
  private StringProperty jobStatus = new SimpleStringProperty("");
  private StringProperty jobDetails = new SimpleStringProperty("");
  private StringProperty jobCreatedDate = new SimpleStringProperty("");
  private StringProperty jobDeliveryCost = new SimpleStringProperty("");
  private StringProperty jobLaborCost = new SimpleStringProperty("");
  private StringProperty jobPartsCost = new SimpleStringProperty("");
  private StringProperty jobTotalCost = new SimpleStringProperty("");

  private StringProperty jobNameEdit = new SimpleStringProperty("");
  private StringProperty deliveryCostEdit = new SimpleStringProperty("");
  private StringProperty deliveryCostEditError = new SimpleStringProperty("");
  //-----------------Job Details Page Modes-----------------//
  private BooleanProperty jobDetailsEditProperty = new SimpleBooleanProperty(
    false
  );
  private BooleanProperty jobDeleteWarning = new SimpleBooleanProperty(false);
  private BooleanProperty jobDetailsSettings = new SimpleBooleanProperty(false);
  private BooleanProperty showPartCharge = new SimpleBooleanProperty(false);
  private BooleanProperty showLaborCharge = new SimpleBooleanProperty(false);
  private BooleanProperty showStatusUpdate = new SimpleBooleanProperty(false);
  private BooleanProperty showPartChargeUpdate = new SimpleBooleanProperty(
    false
  );
  private BooleanProperty showLaborChargeUpdate = new SimpleBooleanProperty(
    false
  );
  private BooleanProperty showDeliveryCostEdit = new SimpleBooleanProperty(
    false
  );

  private JobDAO job;

  //----------------- Job LaborCharge Edit fields -----------------//
  private StringProperty laborChargeName = new SimpleStringProperty("");
  private StringProperty laborChargeHours = new SimpleStringProperty("");
  private StringProperty laborChargeRate = new SimpleStringProperty("");

  private StringProperty laborChargeNameError = new SimpleStringProperty("");
  private StringProperty laborChargeHoursError = new SimpleStringProperty("");
  private StringProperty laborChargeRateError = new SimpleStringProperty("");

  //----------------- Job PartCharge Edit fields -----------------//
  private ObservableList<String> partNames = FXCollections.observableArrayList();
  private ObservableList<String> partNumbers = FXCollections.observableArrayList();

  private StringProperty partQuanity = new SimpleStringProperty("");
  private StringProperty partQuanityError = new SimpleStringProperty("");

  //----------------- Job Bill Charge Lists ---------------------//
  private ObservableList<Parent> laborCharges = FXCollections.observableArrayList();
  private ObservableList<Parent> partCharges = FXCollections.observableArrayList();

  private ObjectProperty<LaborChargeDAO> selectedLaborCharge = new SimpleObjectProperty<>();
  private ObjectProperty<PartChargeDAO> selectedPartCharge = new SimpleObjectProperty<>();

  //----------------- Part Charge Update Fields -----------------//
  private StringProperty partChargeUpdateQuanity = new SimpleStringProperty("");
  private StringProperty partChargeUpdateQuanityError = new SimpleStringProperty(
    ""
  );

  //----------------- Labor Charge Update Fields -----------------//
  private StringProperty laborChargeUpdateName = new SimpleStringProperty("");
  private StringProperty laborChargeUpdateHours = new SimpleStringProperty("");
  private StringProperty laborChargeUpdateRate = new SimpleStringProperty("");
  private StringProperty laborChargeUpdateNameError = new SimpleStringProperty(
    ""
  );
  private StringProperty laborChargeUpdateHoursError = new SimpleStringProperty(
    ""
  );
  private StringProperty laborChargeUpdateRateError = new SimpleStringProperty(
    ""
  );

  //------------------------ JobDetails Settings Properties ------------------------//
  private StringProperty DefaultLaborChargeName = new SimpleStringProperty("");
  private StringProperty DefaultLaborChargeRate = new SimpleStringProperty("");
  private StringProperty DefaultLaborNameError = new SimpleStringProperty("");
  private StringProperty DefaultLaborRateError = new SimpleStringProperty("");
  private ReportDAO settingsReport;
  //------------------------ JobStatus Update Properties ------------------------//
  private ObservableList<String> jobStatuses = FXCollections.observableArrayList(
    "Pending",
    "In Progress",
    "Awaiting Parts",
    "Awaiting Delivery",
    "Awaiting Payment",
    "Completed"
  );

  public JobsDetailsPageModel(JobDAO job) {
    this.job = job;
    initaliseFields();
    setProperties();
    loadLaborCharges();
    loadPartCharges();
    loadDefaultJobDetailsSettings();
  }

  /**
   * Load Default JobDetails settings
   */
  public void loadDefaultJobDetailsSettings() {
    settingsReport =
      DatabaseConnection.reportsCollection.getReport("JobSettings");
    if (settingsReport == null) {
      settingsReport = new ReportDAO();
      settingsReport.setReportName("JobSettings");
      settingsReport.addNewValue("DefaultLaborChargeName", "");
      settingsReport.addNewValue("DefaultLaborChargeRate", "");
      DatabaseConnection.reportsCollection.addNewReport(settingsReport);
    } else {
      DefaultLaborChargeName.set(
        settingsReport.getReportData().get("DefaultLaborChargeName")
      );
      DefaultLaborChargeRate.set(
        settingsReport.getReportData().get("DefaultLaborChargeRate")
      );
    }
  }

  /**
   * Set all the JobProperties
   * this function loads the string properties for the job details page
   * from the selected jobDAO
   */
  public void setProperties() {
    jobName.set(job.getJobName());
    jobStartDate.set(job.getStartDate());
    jobEndDate.set(job.getEndDate());
    jobStatus.set(job.getStatus());
    jobDetails.set(job.getDetails());
    jobCreatedDate.set(job.getCreated().toString());
    jobDeliveryCost.set(String.format("%.2f", job.getBill().getDeliveryCost()));
    jobLaborCost.set(job.getBill().getLaborCost());
    jobPartsCost.set(job.getBill().getPartsCost());
    jobTotalCost.set(String.format("%.2f", job.getBill().getBillTotal()));
    deliveryCostEdit.set(
      String.format("%.2f", job.getBill().getDeliveryCost())
    );
  }

  /**
   * Load all labor charges for the job
   * - This Fuction takes the current job and loads all of the laborChargeDAOs from the
   *   selected Job's bill and creates a new ChargeCard for each laborChargeDAO and
   *  adds it to the laborCharges ObservableList
   */
  public void loadLaborCharges() {
    laborCharges.clear();
    if (
      job.getBill() == null || job.getBill().getLaborCharges() == null
    ) return;
    job
      .getBill()
      .getLaborCharges()
      .forEach(laborCharge -> {
        LaborChargeDAO laborChargeDAO = laborCharge;
        try {
          FXMLLoader cardLoader = App.getLoader(
            "CustomerJobDetailsPage",
            "ChargeCard"
          );
          Parent card = cardLoader.load();
          ((ChargeCardController) cardLoader.getController()).setLaborCharge(
              laborChargeDAO,
              selectedLaborCharge
            );
          laborCharges.add(card);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
  }

  /**
   * Load all part charges for the job
   * - This Fuction takes the current job and loads all of the partChargeDAOs from the
   *   selected Job's bill and creates a new ChargeCard for each partChargeDAO and
   *   adds it to the partCharges ObservableList
   */
  public void loadPartCharges() {
    partCharges.clear();
    if (job.getBill() == null || job.getBill().getPartCharges() == null) return;
    job
      .getBill()
      .getPartCharges()
      .forEach(partCharge -> {
        PartChargeDAO partChargeDAO = partCharge;
        try {
          FXMLLoader cardLoader = App.getLoader(
            "CustomerJobDetailsPage",
            "ChargeCard"
          );
          Parent card = cardLoader.load();
          ((ChargeCardController) cardLoader.getController()).setPartCharge(
              partChargeDAO,
              selectedPartCharge
            );
          partCharges.add(card);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
  }

  /**
   * Initalise all the fields
   * - This function sets all the fields to their default values
   *  and sets the listeners for the selectedPartCharge and selectedLaborCharge
   *  so that when then selected Object changes from the ChargeCard the update
   *  fields are updated with the selected ChargeCard's values
   *  and the showPartChargeUpdate and showLaborChargeUpdate are set to true respectively
   */
  public void initaliseFields() {
    laborChargeHours.setValue("");
    laborChargeHoursError.setValue("");

    laborChargeName.setValue("");
    laborChargeNameError.setValue("");

    laborChargeRate.setValue("");
    laborChargeRateError.setValue("");
    //Listner for the selected Part charge changing
    selectedPartCharge.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        showPartChargeUpdate.setValue(true);
        partChargeUpdateQuanity.set(String.valueOf(newValue.getQuantity()));
      }
    });
    //Listner for the selected Labor charge changing
    selectedLaborCharge.addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        showLaborChargeUpdate.setValue(true);
        laborChargeUpdateName.set(newVal.getName());
        laborChargeUpdateHours.set(String.valueOf(newVal.getHours()));
        laborChargeUpdateRate.set(String.valueOf(newVal.getRate()));
      }
    });
  }

  private ArrayList<PartDAO> selectedJobParts = new ArrayList<>();

  /**
   * shows the add new part charge popup
   * - This function sets the showPartCharge to true
   *  and gets the selected equipment from the job and gets all the parts
   * from the selected equipment and adds the part names and part numbers to the
   */
  public void showNewPart() {
    hideNewLaborCharge();
    showPartCharge.setValue(true);
    EquipmentDAO selectedEquipment = DatabaseConnection.equipmentCollection.getEquipment(
      job.getEquipment()
    );
    if (selectedEquipment == null) return;
    selectedJobParts =
      DatabaseConnection.inventoryCollection.getSelectParts(
        selectedEquipment.getParts()
      );
    for (PartDAO part : selectedJobParts) {
      partNames.add(part.getPartName());
      partNumbers.add(part.getPartNumber());
    }
  }

  /**
   * Hides the add new part charge popup
   */
  public void hideNewPart() {
    showPartCharge.setValue(false);
  }

  /**
   * Shows the add new labor charge popup
   */
  public void showNewLaborCharge() {
    showLaborCharge.setValue(true);
    laborChargeName.set(DefaultLaborChargeName.get());
    laborChargeRate.set(DefaultLaborChargeRate.get());
    hideNewPart();
  }

  /**
   * Hides the add new labor charge popup
   */
  public void hideNewLaborCharge() {
    showLaborCharge.setValue(false);
  }

  /**
   * Toggles the job name edit
   * if the jobDetailsEditProperty is true then the jobNameEdit is set to the current jobName
   * @return
   */
  public boolean toggleJobNameEdit() {
    jobDetailsEditProperty.set(!jobDetailsEditProperty.get());
    if (jobDetailsEditProperty.get()) {
      jobNameEdit.set(jobName.getValue());
    }
    return jobDetailsEditProperty.get();
  }

  /**
   * Save the job name
   * - This function sets the jobName to the jobNameEdit value
   */
  public void saveJobName() {
    jobName.setValue(jobNameEdit.getValue());
    job.setJobName(jobNameEdit.getValue());
    DatabaseConnection.jobCollection.updateJob(job);
  }

  /**
   * Show the status update popup
   */
  public void showStatusUpdatePopup() {
    showStatusUpdate.setValue(true);
  }

  /**
   * Hide the status update popup
   */
  public void hideStatusUpdatePopup() {
    jobStatus.set(job.getStatus());
    showStatusUpdate.setValue(false);
  }

  /**
   * Show the JobDetails settings
   */
  public void showJobDetailsSettings() {
    jobDetailsSettings.setValue(true);
  }

  /**
   * Hide the JobDetails settings
   */
  public void hideJobDetailsSettings() {
    DefaultLaborChargeName.set(
      settingsReport.getReportData().get("DefaultLaborChargeName")
    );
    DefaultLaborChargeRate.set(
      settingsReport.getReportData().get("DefaultLaborChargeRate")
    );
    jobDetailsSettings.setValue(false);
  }

  /**
   * shows the DeleteWarning
   */
  public void showDeleteWarning() {
    jobDeleteWarning.setValue(true);
  }

  /**
   * Hides the delete warning
   */
  public void hideDeleteWarning() {
    jobDeleteWarning.setValue(false);
  }

  /**
   * Validates a new LaborCharge
   * - This function validates the laborChargeName, laborChargeHours and laborChargeRate
   *   and if they are valid then it adds a new LaborCharge to the job's bill
   *   and updates the jobDAO
   *   if Invalid then it sets the error messages for the invalid fields
   */
  public void validateLaborCharge() {
    boolean valid = true;
    if (laborChargeName.equals("")) {
      valid = false;
      laborChargeNameError.setValue("Labor Charge Name is required");
    } else {
      laborChargeNameError.setValue("");
    }
    if (Double.parseDouble(laborChargeHours.get()) <= 0) {
      valid = false;
      laborChargeHoursError.setValue(
        "Labor Charge Hours must be greater than 0"
      );
    } else {
      laborChargeHoursError.setValue("");
    }
    if ((Double.parseDouble(laborChargeRate.get()) <= 0)) {
      valid = false;
      laborChargeRateError.setValue("Labor Charge Rate must be greater than 0");
    } else {
      laborChargeRateError.setValue("");
    }
    if (!valid) return;
    job
      .getBill()
      .addNewLaborCharge(
        laborChargeName.get(),
        Double.parseDouble(laborChargeRate.get()),
        Double.parseDouble(laborChargeHours.get())
      );
    DatabaseConnection.jobCollection.updateJob(job);
    updateJobDAO();
    setProperties();
    loadLaborCharges();
    hideNewLaborCharge();
  }

  /**
   * Validates a new PartCharge
   * - This function validates the partQuanity and if it is valid then it adds a new PartCharge to the job's bill
   *  and updates the jobDAO
   *  if Invalid then it sets the error message for the invalid field
   *  it Checks if the part is in stock and if there is enough in stock
   * @param partName -PartName of the part to be added
   * @param partNumber -PartNumber of the part to be added
   */
  public void validatePartCharge(String partName, String partNumber) {
    if (partQuanity.getValue().isEmpty()) {
      partQuanityError.setValue("Part Quantity is required");
    } else {
      int quanity = Integer.parseInt(partQuanity.get());
      if (quanity <= 0) {
        partQuanityError.setValue("Part Quantity must be greater than 0");
      } else {
        boolean hasPartName = (partName != null);
        PartDAO chosenPart = new PartDAO();
        for (PartDAO part : selectedJobParts) {
          if (hasPartName) {
            if (part.getPartName().equals(partName)) {
              chosenPart = part;
              break;
            }
          } else {
            if (part.getPartNumber().equals(partNumber)) {
              chosenPart = part;
              break;
            }
          }
        }
        if (chosenPart.getId() == null) {
          partQuanityError.setValue("Part not found");
        } else {
          if (quanity > chosenPart.getInStock()) {
            partQuanityError.setValue("Not enough parts in stock");
          } else {
            partQuanityError.setValue("");
            chosenPart.setInStock(chosenPart.getInStock() - quanity);
            DatabaseConnection.inventoryCollection.updatePart(chosenPart);
            job.getBill().addNewPartCharge(chosenPart, quanity);
            DatabaseConnection.jobCollection.updateJob(job);
            updateJobDAO();
            setProperties();
            loadPartCharges();
            hideNewPart();
          }
        }
      }
    }
  }

  /**
   * Update the job DAO
   * - This function updates the jobDAO with the current jobDAO from the database
   */
  private void updateJobDAO() {
    job = DatabaseConnection.jobCollection.getDAO(job.getId());
  }

  /**
   * Validates the part charge update
   * - This function validates the partChargeUpdateQuanity and if it is valid then it updates the selectedPartCharge
   *   with the new quantity and updates the jobDAO and partDAO
   *  if Invalid then it sets the error message for the invalid field
   */
  public void validatePartChargeUpdate() {
    if (partChargeUpdateQuanity.get().isEmpty()) {
      partChargeUpdateQuanityError.setValue("Part Quantity is required");
      return;
    } else {
      if (Double.parseDouble(partChargeUpdateQuanity.get()) <= 0) {
        partChargeUpdateQuanityError.setValue(
          "Part Quantity must be greater than 0"
        );
        return;
      } else {
        PartDAO part = DatabaseConnection.inventoryCollection.getPartByID(
          selectedPartCharge.get().getPartId()
        );
        int newQuanity = Integer.parseInt(partChargeUpdateQuanity.get());
        if (selectedPartCharge.get().getQuantity() > newQuanity) {
          part.setInStock(
            part.getInStock() +
            (
              selectedPartCharge.get().getQuantity() -
              Integer.parseInt(partChargeUpdateQuanity.get())
            )
          );
        } else {
          if (newQuanity > part.getInStock()) {
            partChargeUpdateQuanityError.setValue("Not enough parts in stock");
            return;
          } else {
            part.setInStock(
              part.getInStock() -
              (selectedPartCharge.get().getQuantity() - newQuanity)
            );
          }
        }
        DatabaseConnection.inventoryCollection.updatePart(part);
        job
          .getBill()
          .updatePartCharge(
            selectedPartCharge.get(),
            Integer.parseInt(partChargeUpdateQuanity.get())
          );
        DatabaseConnection.jobCollection.updateJob(job);
        updateJobDAO();
        setProperties();
        loadPartCharges();
        showPartChargeUpdate.setValue(false);
        return;
      }
    }
  }

  /**
   * Deletes the selected part charge
   * - This function deletes the selected part charge from the job's bill
   *   and updates the jobDAO and the partDAO
   */
  public void deletePartCharge() {
    PartDAO part = DatabaseConnection.inventoryCollection.getPartByID(
      selectedPartCharge.get().getPartId()
    );
    part.setInStock(part.getInStock() + selectedPartCharge.get().getQuantity());
    DatabaseConnection.inventoryCollection.updatePart(part);
    job.getBill().removePartCharge(selectedPartCharge.get());
    DatabaseConnection.jobCollection.updateJob(job);
    updateJobDAO();
    setProperties();
    loadPartCharges();
    showPartChargeUpdate.setValue(false);
  }

  /**
   * hides the part charge update
   */
  public void hidePartUpdate() {
    showPartChargeUpdate.setValue(false);
  }

  /**
   * hides the labor charge update
   */
  public void hideLaborUpdate() {
    showLaborChargeUpdate.setValue(false);
  }

  /**
   * Validates the labor charge update
   *  - This function validates the laborChargeUpdateName, laborChargeUpdateHours and laborChargeUpdateRate
   *   and if they are valid then it updates the selectedLaborCharge with the new values
   *   and updates the jobDAO
   *   if Invalid then it sets the error messages for the invalid fields
   */
  public void updateLaborCharge() {
    boolean valid = true;
    if (laborChargeUpdateName.get().isEmpty()) {
      laborChargeUpdateNameError.setValue("Labor Charge Name is required");
      valid = false;
    } else {
      laborChargeUpdateNameError.setValue("");
    }
    if (
      laborChargeUpdateHours.get().isEmpty() ||
      Double.parseDouble(laborChargeUpdateHours.get()) <= 0
    ) {
      laborChargeUpdateHoursError.setValue(
        "Labor Charge Hours must be greater than 0"
      );
      valid = false;
    } else {
      laborChargeUpdateHoursError.setValue("");
    }
    if (
      laborChargeUpdateRate.get().isEmpty() ||
      Double.parseDouble(laborChargeUpdateRate.get()) <= 0
    ) {
      laborChargeUpdateRateError.setValue(
        "Labor Charge Rate must be greater than 0"
      );
      valid = false;
    } else {
      laborChargeUpdateRateError.setValue("");
    }
    if (!valid) return;
    job.getBill().removeLaborCharge(selectedLaborCharge.get());
    job
      .getBill()
      .addNewLaborCharge(
        laborChargeUpdateName.get(),
        Double.parseDouble(laborChargeUpdateRate.get()),
        Double.parseDouble(laborChargeUpdateHours.get())
      );
    DatabaseConnection.jobCollection.updateJob(job);
    updateJobDAO();
    setProperties();
    loadLaborCharges();
    showLaborChargeUpdate.setValue(false);
  }

  /**
   * Deletes the selected labor charge
   * - This function deletes the selected labor charge from the job's bill
   *   and updates the jobDAO
   */
  public void deleteLaborCharge() {
    job.getBill().removeLaborCharge(selectedLaborCharge.get());
    DatabaseConnection.jobCollection.updateJob(job);
    updateJobDAO();
    setProperties();
    loadLaborCharges();
    showLaborChargeUpdate.setValue(false);
  }

  /**
   * saveDeliveryCost:
   * - This function validates the deliveryCostEdit and if it is valid then it updates the job's bill with the new delivery cost
   *   and updates the jobDAO
   *   if Invalid then it sets the error message for the invalid field
   */
  public void saveDeliveryCost() {
    if (deliveryCostEdit.getValue().isEmpty()) {
      deliveryCostEditError.setValue("Delivery Cost is required");
      return;
    } else {
      if (deliveryCostEdit.getValue().matches("^\\d+(\\.\\d{1,2})?$")) {
        deliveryCostEditError.setValue("");
        job
          .getBill()
          .updateDeliveryCost(Double.parseDouble(deliveryCostEdit.getValue()));
        DatabaseConnection.jobCollection.updateJob(job);
        job = DatabaseConnection.jobCollection.getDAO(job.getId());
        setProperties();
        showDeliveryCostEdit.setValue(false);
      } else {
        deliveryCostEditError.setValue("Delivery Cost must be a number");
      }
    }
  }

  /**
   * cancelDeliveryEdit:
   * - This function hides the delivery cost edit popup
   */
  public void cancelDeliveryEdit() {
    showDeliveryCostEdit.setValue(false);
  }

  /**
   * toggleDeliveryEdit:
   * - This function toggles the delivery cost edit popup
   */
  public void toggleDeliveryEdit() {
    showDeliveryCostEdit.setValue(!showDeliveryCostEdit.get());
    deliveryCostEdit.setValue(
      String.format("%.2f", job.getBill().getDeliveryCost())
    );
  }

  /**
   * updateJobDefaults:
   *
   */
  public void updateJobSettings() {
    boolean valid = true;
    if (DefaultLaborChargeName.get().isEmpty()) {
      DefaultLaborNameError.set("Labor Charge Name is required");
      valid = false;
    } else {
      DefaultLaborNameError.set("");
    }
    if (
      DefaultLaborChargeRate.get().isEmpty() ||
      !DefaultLaborChargeRate.get().matches("^\\d+(\\.\\d{1,2})?$")
    ) {
      DefaultLaborRateError.set("Labor Charge Rate must be a number");
      valid = false;
    } else {
      DefaultLaborRateError.set("");
    }
    if (!valid) return;

    settingsReport.addNewValue(
      "DefaultLaborChargeName",
      DefaultLaborChargeName.get()
    );
    settingsReport.addNewValue(
      "DefaultLaborChargeRate",
      DefaultLaborChargeRate.get()
    );
    DatabaseConnection.reportsCollection.updateReport(settingsReport);
    hideJobDetailsSettings();
  }

  String[] completedStatus = { "Completed", "Awaiting Payment" };

  /**
   * update JobStatus:
   */
  public void updateJobStatus() {
    job.setStatus(jobStatus.get());
    boolean completed = false;
    //Check if the job status is in the list of completed statuses
    for (int i = 0; (i < completedStatus.length); i++) {
      if (job.getStatus().equals(completedStatus[i])) {
        completed = true;
        break;
      }
    }
    //if complete complete the job
    if (completed) {
      CompleteJob();
    }
    DatabaseConnection.jobCollection.updateJob(job);
    updateJobDAO();
    setProperties();
    hideStatusUpdatePopup();
  }

  /**
   * CompleteJob:
   * - This function completes the current job
   * and sets the end date and time to the current date and time
   * and updates the jobDAO
   * and updates the machineDAO for the job
   */
  private void CompleteJob() {
    //Update the job end date and time
    job.setEndDate(
        java.time.LocalDate
          .now()
          .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
          .toString()
      );
      job.setEndDateTime(new java.util.Date());

      //Update the machine DAO
      MachineDAO machine = DatabaseConnection.machineCollection.getMachineDAObyId(
        job.getMachineID()
      );
      machine.updateWork(job);
      DatabaseConnection.machineCollection.updateMachine(machine);
  }


  /**
   * delete the currentJob:
   * - This function deletes the current job from the database
   */
  public void deleteJob() {
    DatabaseConnection.jobCollection.deleteJob(job);
    DatabaseConnection.customerCollection.removeJob(job);
  }

  /**
   * update Job Notes
   */
  public void updateJobNotes() {
    if (
      jobDetails.get().isEmpty() || jobDetails.get().equals(job.getDetails())
    ) return;
    job.setDetails(jobDetails.get());
    DatabaseConnection.jobCollection.updateJob(job);
    updateJobDAO();
    setProperties();
  }
}
