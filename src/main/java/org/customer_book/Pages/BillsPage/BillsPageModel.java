package org.customer_book.Pages.BillsPage;

import static com.mongodb.client.model.Filters.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.Setter;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;
import org.customer_book.Database.InvoiceCollection.InvoiceEntry;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Launcher;
import org.customer_book.Pages.BillsPage.CreateInvoice.JobListComponents.CompletedJobCardController;
import org.customer_book.Pages.BillsPage.CreateInvoice.NewInvoiceComponents.BillJobCardController;
import org.customer_book.Pages.BillsPage.ViewInvoices.InvoiceListCards.InvoiceCardController;
import org.customer_book.Pages.BillsPage.ViewInvoices.InvoiceListCards.InvoiceCardModel;
import org.customer_book.Pages.BillsPage.ViewInvoices.InvoicePreview.JobDetails.InvoiceJobDetailsCardController;
import org.customer_book.Popups.DownloadInvoicePopup.DownloadInvoicController;
import org.customer_book.Popups.DownloadInvoicePopup.DownloadInvoicController;
import org.customer_book.Utilities.DownloadDestination;
import org.customer_book.Utilities.InvoiceCreate;

@Setter
@Getter
public class BillsPageModel {

  ///------------- Delete Confirmation Properties ---------------//
  private BooleanProperty showDeleteConfirmation = new SimpleBooleanProperty(
    false
  );

  //------------- Status Update Properties -----------------//
  private StringProperty statusUpdateValue = new SimpleStringProperty("");
  private ObservableList<String> statusUpdateOptions = FXCollections.observableArrayList(
    "UnPaid",
    "Paid",
    "Cancelled"
  );
  private BooleanProperty showStatusUpdate = new SimpleBooleanProperty(false);

  //------------- Settings Properties --------------------//
  private StringProperty downloadLocation = new SimpleStringProperty("");
  private BooleanProperty showSettings = new SimpleBooleanProperty(false);
  private DownloadDestination downloadDestinationUtility;

  //------------- Completed Invoice Properties --------------//
  private ObservableList<Parent> completedInvoiceCards = FXCollections.observableArrayList();
  private ObjectProperty<InvoiceDAO> selectedInvoice = new SimpleObjectProperty<>();
  private BooleanProperty showCompletedInvoice = new SimpleBooleanProperty(
    false
  );

  //------------- Selected Invoice Properties ---------------//
  private ObservableList<Parent> selectedInvoiceJobDetails = FXCollections.observableArrayList();
  private StringProperty selectedCustomerName = new SimpleStringProperty("");
  private StringProperty selectedInvoiceNumber = new SimpleStringProperty("");
  private StringProperty selectedInvoiceCustomerAddress = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedInvoiceCustomerPhoneNumber = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedInvoiceCreatedDate = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedInvoiceLaborTotal = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedInvoiceDeliveryTotal = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedInvoiceChargeTotal = new SimpleStringProperty(
    ""
  );
  private StringProperty selectedInvoiceStatus = new SimpleStringProperty("");

  //------------- Create Invoice Properties ---------------//
  private StringProperty InvoiceNumber = new SimpleStringProperty("");
  private StringProperty CustomerName = new SimpleStringProperty("");
  private StringProperty InvoiceTotal = new SimpleStringProperty("");
  private BooleanProperty showGeneratedInvoicePopup = new SimpleBooleanProperty(
    false
  );
  private BooleanProperty showGenerateInvoicePane = new SimpleBooleanProperty(
    true
  );

  //------------- Visibility properties --------------- //
  private BooleanProperty showJobsFilter = new SimpleBooleanProperty(false);
  private BooleanProperty showAddJobError = new SimpleBooleanProperty(false);
  //------------- Card List properties --------------- //
  private ObservableList<Parent> completedJobCards = FXCollections.observableArrayList();
  private ObservableList<Parent> addedJobCards = FXCollections.observableArrayList();

  //-------------- AddingAndRemovingJobs properties --------------- //
  private ObjectProperty<JobDAO> jobToAdd = new SimpleObjectProperty<>();
  private ObjectProperty<JobDAO> jobToRemove = new SimpleObjectProperty<>();

  private ArrayList<JobDAO> addedJobs = new ArrayList<>();

  //------------- Filter properties --------------- //
  private ArrayList<String> customerNames = new ArrayList<>();
  private BooleanProperty filterByDateCreated = new SimpleBooleanProperty(
    false
  );
  private BooleanProperty filterByDateCompleted = new SimpleBooleanProperty(
    false
  );
  private Property<LocalDate> startDateProperty = new SimpleObjectProperty<LocalDate>();
  private Property<LocalDate> endDateProperty = new SimpleObjectProperty<LocalDate>();
  private StringProperty customerNameJobsFilterProperty = new SimpleStringProperty(
    ""
  );
  private ObservableList<String> availableNamesJobsFilterProperty = FXCollections.observableArrayList();
  private ArrayList<JobDAO> completedJobDAOS = new ArrayList<>();
  private ArrayList<InvoiceDAO> completedInvoices = new ArrayList<>();

  private Bson completedJobsFilter = ne("_id", null);
  private StringProperty filterErrorMessage = new SimpleStringProperty("");

  /**
   * Constructor for the Bills Page Model
   * - Load the customer names
   */
  public BillsPageModel() {
    //Load the customer names and add all the names to the available names filter
    customerNames = DatabaseConnection.customerCollection.getAllNames();
    availableNamesJobsFilterProperty.addAll(customerNames);

    //Load the completed jobs and invoices
    updateJobDAOs(true);
    loadCompletedJobs();
    loadCompletedInvoiceDAOs(true);
    loadCompletedInvoices();

    //Add Listner for when toAdd or toRemove is changed
    jobToAdd.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        if (
          newValue.getCustomerName().equals(CustomerName.get()) ||
          CustomerName.get().isEmpty()
        ) {
          CustomerName.set(newValue.getCustomerName());
          addedJobs.add(newValue);
          loadBillCards();
        } else {
          showAddJobError.set(true);
          jobToRemove.set(newValue);
        }
      }
    });
    jobToRemove.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        if (addedJobs.remove(newValue)) {
          loadBillCards();
        }
      }
    });

    //Try to load the default download location
    downloadDestinationUtility = new DownloadDestination(downloadLocation);
  }

  /**
   * Function to refine the customer name list based on the filter
   * @param enteredValue
   */
  public void updateNames(String enteredValue) {
    availableNamesJobsFilterProperty.clear();
    for (String name : customerNames) {
      if (name.toLowerCase().contains(enteredValue.toLowerCase())) {
        availableNamesJobsFilterProperty.add(name);
      }
    }
  }

  /**
   * generateRandomNumber
   *  - This function is used to generate a random number for the invoice number
   * @return
   */
  private String generateRandomNumber() {
    Random random = new Random();
    int randomNumber = random.nextInt(900000) + 100000;
    return String.valueOf(randomNumber);
  }

  /**
   * Load the bill cards
   *  - This function is used to load the bill cards for the added jobs
   */
  private void loadBillCards() {
    //Store the total of the invoice
    double total = 0;
    //If there is no current invoice Number generate a random one
    if (InvoiceNumber.get().isEmpty()) {
      InvoiceNumber.set("INV" + generateRandomNumber());
    }
    //Load the job cards for the added jobs
    addedJobCards.clear();
    for (JobDAO job : addedJobs) {
      total += job.getBill().getBillTotal();
      try {
        //Load the JobCard FXMLLoader
        FXMLLoader cardLoader = App.getLoader("BillsPage", "JobInBillCard");
        Parent card = cardLoader.load();
        //Set the jobDAO for the card as well as a refrence to the to remove job
        ((BillJobCardController) cardLoader.getController()).setJobDAO(
            job,
            jobToRemove
          );
        addedJobCards.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    //Set the total of the invoice
    InvoiceTotal.set("$" + total);

    //clear the to remove and add placeholders
    jobToAdd.set(null);
    jobToRemove.set(null);

    //If there are no current jobs clear the customer name and invoice number
    if (addedJobs.isEmpty()) {
      CustomerName.set("");
      InvoiceNumber.set("");
    }
  }

  /**
   * Hide the jobs filter
   */
  public void hideJobsFilter() {
    showJobsFilter.set(false);
  }

  /**
   * Show the jobs filter
   */
  public void showJobsFilter() {
    showJobsFilter.set(true);
  }

  /**
   * Update JobDAOS
   */
  private void updateJobDAOs(boolean clearExisting) {
    //Load all completed Jobs from the database
    if (clearExisting) {
      completedJobDAOS =
        DatabaseConnection.jobCollection.getCompletedJobs(
          completedJobsFilter,
          4,
          0
        );
    } else {
      completedJobDAOS.addAll(
        DatabaseConnection.jobCollection.getCompletedJobs(
          completedJobsFilter,
          2,
          completedJobDAOS.size()
        )
      );
    }
  }

  /**
   * Load the completed jobs
   *   - This function is used to load the completed jobs into the completedJobCards list
   */
  public void loadCompletedJobs() {
    completedJobCards.clear();

    //Create a Jobcard and add it for each completed Job
    for (JobDAO job : completedJobDAOS) {
      try {
        //Load the FXMLLoader for the CompletedJobCard
        FXMLLoader cardLoader = App.getLoader("BillsPage", "CompletedJobCard");
        Parent card = cardLoader.load();
        //Add the jobDAO and the refrence to the toAdd and toRemove jobs
        ((CompletedJobCardController) cardLoader.getController()).setJobDAO(
            job,
            jobToAdd,
            jobToRemove
          );
        completedJobCards.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  //Refrence to most recent invoice for navigating to the invoice
  private ObjectId mostRecentInvoiceId;

  /**
   * Generate and save the invoice in the database
   */
  public void saveInvoice() {
    //If there are no added jobs return
    if (addedJobs.isEmpty()) {
      return;
    }
    InvoiceDAO invoice = new InvoiceDAO();
    invoice.newInvoice(InvoiceNumber.get(), addedJobs, CustomerName.get());
    mostRecentInvoiceId =
      DatabaseConnection.invoiceCollection.addInvoice(invoice);
    showGeneratedInvoicePopup.set(true);
  }

  /**
   * Hide the generated invoice popup
   *  also reload the completed Jobs and the bill cards
   */
  public void hideGeneratedInvoicePopup() {
    showGeneratedInvoicePopup.set(false);
    clearFilter();
    addedJobs.clear();
    loadBillCards();
  }

  /**
   * Show the generated invoice popup
   */
  public void showGeneratedInvoicePopup() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'showGeneratedInvoicePopup'"
    );
  }

  /**
   * Show the generate invoice pane
   * and reload the completed jobs and the bill cards
   */
  public void showGenerateInvoicePane() {
    showGenerateInvoicePane.set(true);
    showCompletedInvoice.set(false);
    selectedInvoice.set(null);
    selectedInvoice.removeListener(selectedInvoiceListner);
    clearFilter();
    addedJobs.clear();
    loadBillCards();
  }

  /**
   * show the completed invoice pane and hide the generate invoice pane
   */
  public void showCompletedInvoicePane() {
    showGenerateInvoicePane.set(false);
    showCompletedInvoice.set(true);
    if (selectedInvoice.get() == null) {
      showCompletedInvoice.set(false);
      selectedInvoice.addListener(selectedInvoiceListner);
    }
    loadCompletedInvoiceDAOs(true);
    loadCompletedInvoices();
  }

  private ChangeListener<InvoiceDAO> selectedInvoiceListner = new ChangeListener<InvoiceDAO>() {
    @Override
    public void changed(
      ObservableValue<? extends InvoiceDAO> observable,
      InvoiceDAO oldValue,
      InvoiceDAO newValue
    ) {
      if (newValue != null) {
        showCompletedInvoice.set(true);
        selectedInvoice.removeListener(this);
      }
    }
  };

  /**
   * show the settings popup
   */
  public void showSettings() {
    showSettings.set(true);
  }

  /**
   * hide the settings popup
   */
  public void hideSettings() {
    showSettings.set(false);
  }

  /**
   * Show the download location
   *  - This function is used to show the download location for the invoices
   */
  public void showDownloadLocation() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Download Location");
    directoryChooser.setInitialDirectory(
      downloadLocation.get().isEmpty()
        ? new File(System.getProperty("user.home"))
        : new File(downloadLocation.get())
    );
    File selectedFile = directoryChooser.showDialog(App.getStage());
    if (selectedFile == null) return;
    downloadLocation.set(selectedFile.getAbsolutePath());
  }

  /**
   * Show the invoice filter
   */
  public void showInvoiceFilter() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'showInvoiceFilter'"
    );
  }

  public void loadCompletedInvoiceDAOs(boolean clearExisting) {
    if (clearExisting) {
      completedInvoices =
        DatabaseConnection.invoiceCollection.getCompletedInvoices(
          completedJobsFilter,
          4,
          0
        );
    } else {
      completedInvoices.addAll(
        DatabaseConnection.invoiceCollection.getCompletedInvoices(
          completedJobsFilter,
          2,
          completedInvoices.size()
        )
      );
    }
  }

  /**
   * Load all of the completed invoices
   * - This function is used to load all of the completed invoices into the completedInvoiceCards list
   */
  public void loadCompletedInvoices() {
    //clear the current list
    completedInvoiceCards.clear();
    //get an ArrayList of all completed invoices
    for (InvoiceDAO invoice : completedInvoices) {
      try {
        FXMLLoader cardLoader = App.getLoader(
          "BillsPage",
          "CompletedInvoiceCard"
        );
        Parent card = cardLoader.load();
        ((InvoiceCardController) cardLoader.getController()).setInvoice(
            invoice,
            selectedInvoice
          );
        completedInvoiceCards.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    selectedInvoice.addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        updateSelectedInvoice();
      }
    });
  }

  /**
   * Update selected Value when the selected invoice changes
   */
  public void updateSelectedInvoice() {
    if (selectedInvoice.get() == null) return;
    selectedCustomerName.set(selectedInvoice.get().getCustomerName());
    selectedInvoiceNumber.set(selectedInvoice.get().getInvoiceNumber());
    selectedInvoiceCustomerAddress.set(
      selectedInvoice.get().getCustomerAddress()
    );
    selectedInvoiceCustomerPhoneNumber.set(
      selectedInvoice.get().getCustomerPhoneNumber()
    );
    selectedInvoiceCreatedDate.set(selectedInvoice.get().getGeneratedDate());
    selectedInvoiceLaborTotal.set("$" + selectedInvoice.get().getLaborTotal());
    selectedInvoiceDeliveryTotal.set(
      "$" + selectedInvoice.get().getDeliveryTotal()
    );
    selectedInvoiceChargeTotal.set(
      "$" + selectedInvoice.get().getChargeTotal()
    );
    selectedInvoiceStatus.set(selectedInvoice.get().getStatus());
    updateJobDetails(selectedInvoice.get());
    statusUpdateValue.set(selectedInvoice.get().getStatus());
  }

  /**
   * Update the Job details list with a card for each bill for a job
   * within the selected invoice
   * @param dao - InvoiceDAO
   */
  public void updateJobDetails(InvoiceDAO dao) {
    selectedInvoiceJobDetails.clear();
    for (InvoiceEntry entry : dao.getBills()) {
      try {
        FXMLLoader cardLoader = App.getLoader(
          "BillsPage",
          "SelectedInvoiceJobDetails"
        );
        Parent card = cardLoader.load();
        (
          (InvoiceJobDetailsCardController) cardLoader.getController()
        ).setJobDetails(entry, selectedInvoice);
        selectedInvoiceJobDetails.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Download the selected invoice
   */
  public void downloadSelectedInvoice() {
    try {
      FXMLLoader downloadPopup = App.getLoader("Popups", "DownloadingInvoice");
      Parent downloadPopupParent = downloadPopup.load();
      ((DownloadInvoicController) downloadPopup.getController()).setProperties(
          selectedInvoice.get(),
          downloadLocation
        );
      App.addPopup(downloadPopupParent);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Show the delete confirmation on press of delete button
   */
  public void deleteSelectedInvoice() {
    showDeleteConfirmation.set(true);
  }

  /**
   * Show the status update
   */
  public void showStatusUpdate() {
    if (selectedInvoice.get() != null) {
      showStatusUpdate.set(true);
    }
  }

  /**
   * Save the invoice status
   *   - Update the invoice status of the selected invoice
   * @param value
   */
  public void saveInvoiceStatus(String value) {
    if (
      selectedInvoice.get() != null &&
      !value.equals(selectedInvoice.get().getStatus())
    ) {
      selectedInvoice.get().setStatus(value);
      DatabaseConnection.invoiceCollection.updateInvoice(selectedInvoice.get());
      updateSelectedInvoice();
    }
    showStatusUpdate.set(false);
  }

  /**
   * Hide the status update
   */
  public void hideStatusUpdate() {
    showStatusUpdate.set(false);
  }

  /**
   * Cancel the invoice delete
   */
  public void cancelInvoiceDelete() {
    showDeleteConfirmation.set(false);
  }

  /**
   * Confirm the invoice delete
   *  remove the selected invoice from the database
   */
  public void confirmDeleteInvoice() {
    if (selectedInvoice.get() != null) {
      DatabaseConnection.invoiceCollection.deleteInvoice(selectedInvoice.get());
      loadCompletedInvoiceDAOs(true);
      loadCompletedInvoices();
      showCompletedInvoice.set(false);
      showDeleteConfirmation.set(false);
    }
  }

  public void applyJobsFilter() {
    if (!filterByDateCompleted.get() && !filterByDateCreated.get()) {
      filterErrorMessage.set("Please select a filter");
      return;
    } else {
      filterErrorMessage.set("");
    }

    completedJobsFilter =
      and(
        showGenerateInvoicePane.get()
          ? and(
            gt(
              filterByDateCreated.get() ? "created" : "endDateTime",
              startDateProperty.getValue() != null
                ? startDateProperty.getValue().atStartOfDay()
                : new Date(Long.MIN_VALUE)
            ),
            lt(
              filterByDateCreated.get() ? "created" : "endDateTime",
              endDateProperty.getValue() != null
                ? endDateProperty.getValue().atStartOfDay()
                : new Date(Long.MAX_VALUE)
            )
          )
          : (
            and(
              gt(
                "generatedDateTime",
                startDateProperty.getValue() != null
                  ? startDateProperty.getValue().atStartOfDay()
                  : new Date(Long.MIN_VALUE)
              ),
              lt(
                "generatedDateTime",
                endDateProperty.getValue() != null
                  ? endDateProperty.getValue().atStartOfDay()
                  : new Date(Long.MAX_VALUE)
              )
            )
          ),
        customerNameJobsFilterProperty.get().equals("")
          ? ne("_id", null)
          : eq("customerName", customerNameJobsFilterProperty.get())
      );
    if (showGenerateInvoicePane.get()) {
      updateJobDAOs(true);
      loadCompletedJobs();
    } else {
      loadCompletedInvoiceDAOs(true);
      loadCompletedInvoices();
    }
    System.out.println(completedJobsFilter);
  }

  public void loadMoreCompletedJobs() {
    updateJobDAOs(false);
    loadCompletedJobs();
  }

  public void clearFilter() {
    completedJobsFilter = ne("_id", null);
    startDateProperty.setValue(null);
    endDateProperty.setValue(null);
    customerNameJobsFilterProperty.set("");
    if (showGenerateInvoicePane.get()) {
      updateJobDAOs(true);
      loadCompletedJobs();
    } else {
      loadCompletedInvoiceDAOs(false);
      loadCompletedInvoices();
    }
  }

  public void loadMoreInvoices() {
    loadCompletedInvoiceDAOs(false);
    loadCompletedInvoices();
  }
}
