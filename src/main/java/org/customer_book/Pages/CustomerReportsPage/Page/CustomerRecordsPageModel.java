package org.customer_book.Pages.CustomerReportsPage.Page;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.Pages.CustomerReportsPage.Cards.InvoiceCardController;
import org.bson.conversions.Bson;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;

@Getter
@Setter
public class CustomerRecordsPageModel {

  // -------------------- View Properties --------------------//
  // Heading
  private StringProperty CustomerNameProperty = new SimpleStringProperty("");

  // Visibilities
  private BooleanProperty InvoiceDetailsVisible = new SimpleBooleanProperty(false);
  private BooleanProperty StatusUpdateVisible = new SimpleBooleanProperty(false);

  // InvoiceList
  private ObservableList<Parent> InvoiceList = FXCollections.observableArrayList();
  private ArrayList<InvoiceDAO> invoiceDAOs = new ArrayList<>();
  private ObjectProperty<InvoiceDAO> selectedInvoice = new SimpleObjectProperty<>();

  // StatusUpdate Popup
  private ObservableList<String> StatusUpdateOptions = FXCollections.observableArrayList();
  private StringProperty SelectedStatusUpdate = new SimpleStringProperty("");

  // SelectedInvoiceDetails
  private StringProperty InvoiceNumberProperty = new SimpleStringProperty("");
  private StringProperty InvoiceCreatedProperty = new SimpleStringProperty("");
  private StringProperty InvoiceCompletedProperty = new SimpleStringProperty("");
  private StringProperty StatusProperty = new SimpleStringProperty("");

  private StringProperty LaborTotalProperty = new SimpleStringProperty("");
  private StringProperty DeliveryFeeTotalProperty = new SimpleStringProperty("");
  private StringProperty PartsTotalProperty = new SimpleStringProperty("");
  private StringProperty BillTotalProperty = new SimpleStringProperty("");

  private ObservableList<Parent> InvoiceJobsList = FXCollections.observableArrayList();

  // -------------------- Model Properties --------------------//
  private CustomerDAO customer;
  private Bson filter;
  private int loadSize = 10;

  //-------------------- Constructor --------------------//
  public CustomerRecordsPageModel() {
    // Set the initial status update options
    StatusUpdateOptions.addAll("Awaiting Payment", "Paid", "In Progress", "Completed");
    SelectedStatusUpdate.set(StatusUpdateOptions.get(0));

    // Listen for changes to the selected invoice
    selectedInvoice.addListener(new ChangeListener<InvoiceDAO>() {
      @Override
      public void changed(ObservableValue<? extends InvoiceDAO> observable, InvoiceDAO oldValue,
          InvoiceDAO newValue) {
       if(newValue != null){
         updateSelectedInvoiceDetails();
       }
      }
    });
  }
  

  // -------------------- View Methods --------------------//
  // Navigate back using the backPointer
  public void navigateBack() {
    App.useBackPointer();
  }

  // Make the invoice filter visible
  public void showInvoiceFilter() {
    // TODO: Show the invoice filter
  }

  // Make the status update popup visible
  public void showStatusUpdatePopup() {
    StatusUpdateVisible.set(true);
  }

  // Hide the status update popup
  public void hideStatusUpdatePopup() {
    StatusUpdateVisible.set(false);
  }

  // Save the staus update
  public void saveStatusUpdate() {
    if (selectedInvoice.get() != null) {
      selectedInvoice.get().setStatus(SelectedStatusUpdate.get());
    }
    DatabaseConnection.invoiceCollection.updateInvoice(selectedInvoice.get());
    updateSelectedInvoiceDetails();
    hideStatusUpdatePopup();
  }

  // ------------------------ Model Methods --------------------//
  private void updateSelectedInvoiceDetails() {
    if(selectedInvoice.get() != null){
      //Update Header Info
      InvoiceNumberProperty.set(selectedInvoice.get().getInvoiceNumber());
      InvoiceCreatedProperty.set(selectedInvoice.get().getGeneratedDate());
      StatusProperty.set(selectedInvoice.get().getStatus());
      
      //Update Totals
      LaborTotalProperty.set(selectedInvoice.get().getLaborTotal());
      DeliveryFeeTotalProperty.set(selectedInvoice.get().getDeliveryTotal());
      PartsTotalProperty.set(selectedInvoice.get().getPartsTotal());
      BillTotalProperty.set(selectedInvoice.get().getChargeTotal());

      //Load Job Cards for the invoice
      loadInvoiceJobs();
       //Update the visibility
      InvoiceDetailsVisible.set(true);
    }else{
      InvoiceDetailsVisible.set(false);
    }
  }

  private void loadInvoiceJobs() {
    // TODO Auto-generated method stub
  }


  public void loadCustomer() {
    try {
      customer = (CustomerDAO) App.getSceneProperty("customerDAO");
    }
    catch (Exception e) {
      System.out.println("Error loading customer: " + e.getMessage());
    }
    if (customer != null) {
      CustomerNameProperty.set(customer.getName());
      filter = eq("customerId", customer.getId());
      loadInvoices(true);
    }
  }

  private void loadInvoices(boolean clear) {
    if (clear) {
      invoiceDAOs = DatabaseConnection.invoiceCollection.getFilteredInvoices(filter, loadSize, 0);
    } else {
      invoiceDAOs.addAll(
          DatabaseConnection.invoiceCollection.getFilteredInvoices(filter, loadSize, invoiceDAOs.size()));
    }
    loadCards();
  }

  private void loadCards() {
    InvoiceList.clear();
    for (InvoiceDAO invoice : invoiceDAOs) {
      try {
        FXMLLoader loader = App.getLoader("CustomerRecordsPage", "InvoiceCard");
        Parent card = loader.load();
        ((InvoiceCardController) loader.getController()).setInvoice(invoice, selectedInvoice);
        InvoiceList.add(card);
      }
      catch (Exception e) {
        System.out.println("Error loading invoice: " + e.getMessage());
      }
    }
  }

}
