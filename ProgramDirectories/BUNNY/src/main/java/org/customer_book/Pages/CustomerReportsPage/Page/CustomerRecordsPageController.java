package org.customer_book.Pages.CustomerReportsPage.Page;

import org.customer_book.App;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;
import org.customer_book.Database.JobsCollection.LaborChargeDAO;
import java.util.concurrent.CompletableFuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class CustomerRecordsPageController {
    @FXML
    private Label CustomerNameLabel;

    @FXML
    private AnchorPane InvoiceDetails;

    @FXML
    private AnchorPane InvoiceJobs;

    @FXML
    private Label InvoiceNumberLabel;

    @FXML
    private Label LaborTotalLabel;

    @FXML
    private Button StatusButton;

    @FXML
    private Label DeliveryFeeTotalLabel;

    @FXML
    private ListView<Parent> InvoiceList;

    @FXML
    private ChoiceBox<String> StatusUpdateChoiceBox;

    @FXML
    private Label PartsTotalLabel;

    @FXML
    private AnchorPane UpdateStatusPopup;

    @FXML
    private ListView<Parent> InvoiceJobsList;

    @FXML
    private Label InvoiceCreatedLabel;

    @FXML
    private Label BillTotalLabel;

    @FXML
    void navigateBack(ActionEvent event) {
        model.navigateBack();
    }

    @FXML
    void showInvoiceFilter(ActionEvent event) {
        model.showInvoiceFilter();
    }

    @FXML
    void showUpdateStatus(ActionEvent event) {
        model.showStatusUpdatePopup();
    }

    @FXML
    void closeStatusPopup(ActionEvent event) {
        model.hideStatusUpdatePopup();
    }

    @FXML
    void saveInvoiceStatus(ActionEvent event) {
        model.saveStatusUpdate();
    }

    private CustomerRecordsPageModel model;

    @FXML
    void initialize() {

        model = new CustomerRecordsPageModel();

        if (!App.hasBackPointer()) {
            App.setBackPointer("CustomerDetailsPage", "CustomerDetailsPage");
        }
        if (App.getSceneProperty("selectedInvoice") != null) {
            model.setInvoice((InvoiceDAO) App.getSceneProperty("selectedInvoice"));
            App.removeSceneProperty("selectedInvoice");
        }

        CompletableFuture<Void> loadCustomer = CompletableFuture.runAsync(() -> model.loadCustomer());
        // Run the following code after the view has been initialized
        CompletableFuture<Void> initalizeCustomerName = CompletableFuture
                .runAsync(this::initializeCustomerName);
        CompletableFuture<Void> initalizeVisibleProperties = CompletableFuture
                .runAsync(this::initializeVisibleProperties);
        CompletableFuture<Void> initalizeInvoiceList = CompletableFuture
                .runAsync(this::initializeInvoiceList);
        CompletableFuture<Void> initalizeStatusUpdateOptions = CompletableFuture
                .runAsync(this::initializeStatusUpdateOptions);
        CompletableFuture<Void> initalizeSelectedInvoiceDetails = CompletableFuture
                .runAsync(this::initializeSelectedInvoiceDetails);

        CompletableFuture.allOf(loadCustomer, initalizeCustomerName, initalizeVisibleProperties,
                initalizeInvoiceList, initalizeStatusUpdateOptions, initalizeSelectedInvoiceDetails).join();

    }

    private void initializeCustomerName() {
        CustomerNameLabel.textProperty().bind(model.getCustomerNameProperty());
    }

    private void initializeVisibleProperties() {
        InvoiceDetails.visibleProperty().bind(model.getInvoiceDetailsVisible());
        UpdateStatusPopup.visibleProperty().bind(model.getStatusUpdateVisible());
    }

    private void initializeInvoiceList() {
        InvoiceList.setItems(model.getInvoiceList());
    }

    private void initializeStatusUpdateOptions() {
        StatusUpdateChoiceBox.setItems(model.getStatusUpdateOptions());
    }

    private void initializeSelectedInvoiceDetails() {
        // Hearder of Selected Invoice
        InvoiceNumberLabel.textProperty().bind(model.getInvoiceNumberProperty());
        InvoiceCreatedLabel.textProperty().bind(model.getInvoiceCreatedProperty());
        StatusButton.textProperty().bind(model.getStatusProperty());

        // JobDetails of Selected Invoice
        InvoiceJobsList.setItems(model.getInvoiceJobsList());

        // Totals of Selected Invoice
        PartsTotalLabel.textProperty().bind(model.getPartsTotalProperty());
        LaborTotalLabel.textProperty().bind(model.getLaborTotalProperty());
        DeliveryFeeTotalLabel.textProperty().bind(model.getDeliveryFeeTotalProperty());
        BillTotalLabel.textProperty().bind(model.getBillTotalProperty());
    }
}
