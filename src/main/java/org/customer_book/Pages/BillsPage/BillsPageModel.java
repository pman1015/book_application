package org.customer_book.Pages.BillsPage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Launcher;

@Setter
@Getter
public class BillsPageModel {

  //------------- Settings Properties --------------------//
  private StringProperty downloadLocation = new SimpleStringProperty("");
  private BooleanProperty showSettings = new SimpleBooleanProperty(false);

  //------------- Completed Invoice Properties --------------//
  private ObservableList<Parent> completedInvoiceCards = FXCollections.observableArrayList();
  private ObjectProperty<InvoiceDAO> selectedInvoice = new SimpleObjectProperty<>();

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
  private Property<LocalDate> completedDateJobsFilterProperty = new SimpleObjectProperty<LocalDate>();
  private Property<LocalDate> createdDateJobsFilterProperty = new SimpleObjectProperty<LocalDate>();
  private StringProperty customerNameJobsFilterProperty = new SimpleStringProperty(
    ""
  );
  private ObservableList<String> availableNamesJobsFilterProperty = FXCollections.observableArrayList();

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

  public BillsPageModel() {
    customerNames = DatabaseConnection.customerCollection.getAllNames();
    availableNamesJobsFilterProperty.addAll(customerNames);
    loadCompletedJobs();
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
    try {
      URL resourceURL =
        App.class.getProtectionDomain().getCodeSource().getLocation();
      File DataFolder = new File(
        new File(resourceURL.getPath()).getParent() + "/Data"
      );
      if (!DataFolder.exists()) {
        DataFolder.mkdir();
      }
      InputStream is = new FileInputStream(
        new File(resourceURL.getPath()).getParent() +
        "/Data/DownloadLocation.txt"
      );
      System.out.println(
        "File Path: " +
        new File(resourceURL.getPath()).getParent() +
        "/Data/DownloadLocation.txt"
      );

      Scanner s = new Scanner(is).useDelimiter("\\A");
      downloadLocation.set(s.hasNext() ? s.next() : "");
      System.out.println("Download Location: " + downloadLocation.get());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String generateRandomNumber() {
    Random random = new Random();
    int randomNumber = random.nextInt(900000) + 100000;
    return String.valueOf(randomNumber);
  }

  private void loadBillCards() {
    double total = 0;
    if (InvoiceNumber.get().isEmpty()) {
      InvoiceNumber.set("INV" + generateRandomNumber());
    }
    addedJobCards.clear();
    for (JobDAO job : addedJobs) {
      total += job.getBill().getBillTotal();
      try {
        FXMLLoader cardLoader = App.getLoader("BillsPage", "JobInBillCard");
        Parent card = cardLoader.load();
        ((BillJobCardView) cardLoader.getController()).setJobDAO(
            job,
            jobToRemove
          );
        addedJobCards.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    InvoiceTotal.set("$" + total);
    jobToAdd.set(null);
    jobToRemove.set(null);
    if (addedJobs.isEmpty()) {
      CustomerName.set("");
      InvoiceNumber.set("");
    }
  }

  public void hideJobsFilter() {
    showJobsFilter.set(false);
  }

  public void showJobsFilter() {
    showJobsFilter.set(true);
  }

  public void loadCompletedJobs() {
    completedJobCards.clear();
    ArrayList<JobDAO> completedJobs = DatabaseConnection.jobCollection.getCompletedJobs();
    for (JobDAO job : completedJobs) {
      try {
        FXMLLoader cardLoader = App.getLoader("BillsPage", "CompletedJobCard");
        Parent card = cardLoader.load();
        ((CompletedJobCardView) cardLoader.getController()).setJobDAO(
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

  private ObjectId mostRecentInvoiceId;

  public void saveInvoice() {
    if (addedJobs.isEmpty()) {
      return;
    }
    InvoiceDAO invoice = new InvoiceDAO();
    invoice.newInvoice(InvoiceNumber.get(), addedJobs, CustomerName.get());
    mostRecentInvoiceId =
      DatabaseConnection.invoiceCollection.addInvoice(invoice);
    showGeneratedInvoicePopup.set(true);
  }

  public void hideGeneratedInvoicePopup() {
    showGeneratedInvoicePopup.set(false);
    loadCompletedJobs();
    addedJobs.clear();
    loadBillCards();
  }

  public void showGeneratedInvoicePopup() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'showGeneratedInvoicePopup'"
    );
  }

  public void showGenerateInvoicePane() {
    showGenerateInvoicePane.set(true);
    loadCompletedJobs();
    addedJobs.clear();
    loadBillCards();
  }

  public void showCompletedInvoicePane() {
    showGenerateInvoicePane.set(false);
  }

  public void showSettings() {
    showSettings.set(true);
  }

  public void hideSettings() {
    showSettings.set(false);
  }

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

  public void saveSettings() {
    // URL resourceURL = Launcher.class.getResource("Data/DownloadLocation.txt");
    URL resourceURL =
      App.class.getProtectionDomain().getCodeSource().getLocation();

    try {
      File f = new File(
        new File(resourceURL.getPath()).getParent() +
        "/Data/DownloadLocation.txt"
      );
      BufferedWriter writer = new BufferedWriter(new FileWriter(f, false));
      writer.write(downloadLocation.get());
      writer.close();
      hideSettings();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showInvoiceFilter() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'showInvoiceFilter'"
    );
  }

  public void loadCompletedInvoices() {
    completedInvoiceCards.clear();
    ArrayList<InvoiceDAO> invoices = DatabaseConnection.invoiceCollection.getAllInvoices();
    for (InvoiceDAO invoice : invoices) {
      try {
        FXMLLoader cardLoader = App.getLoader(
          "BillsPage",
          "CompletedInvoiceCard"
        );
        Parent card = cardLoader.load();
        ((InvoiceCardView) cardLoader.getController()).setInvoice(
            invoice,
            selectedInvoice
          );
        completedInvoiceCards.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
