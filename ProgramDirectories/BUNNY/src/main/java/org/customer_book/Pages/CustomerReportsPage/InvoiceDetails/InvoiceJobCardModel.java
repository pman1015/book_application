package org.customer_book.Pages.CustomerReportsPage.InvoiceDetails;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

import org.customer_book.App;
import org.customer_book.Database.InvoiceCollection.InvoiceEntry;
import org.customer_book.Database.JobsCollection.JobDAO;
import org.customer_book.Database.JobsCollection.PartChargeDAO;
import org.customer_book.Pages.CustomerReportsPage.InvoiceDetails.PartCards.InvoiceJobPartCardController;
import org.customer_book.Pages.CustomerReportsPage.InvoiceDetails.PartCards.InvoiceJobPartCardModel;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceJobCardModel {

    // -------------------- View Properties -------------//
    // Card Heading Properties
    private StringProperty machineNameProperty = new SimpleStringProperty("");
    private StringProperty jobNameProperty = new SimpleStringProperty("");
    private StringProperty startDateProperty = new SimpleStringProperty("");
    private StringProperty hourCountProperty = new SimpleStringProperty("");

    // Part List
    private ObservableList<Parent> partCardsProperty = FXCollections.observableArrayList();
    private ArrayList<PartChargeDAO> partCharges = new ArrayList<>();

    // JobNotes
    private StringProperty jobNotesProperty = new SimpleStringProperty("");
    private BooleanProperty jobNotesVisible = new SimpleBooleanProperty(false);

    // -------------------- Model Properties -------------//
    private InvoiceEntry entry;

    public void setInvoiceEntry(InvoiceEntry entry) {
        this.entry = entry;
        updateProperties();
    }

    private void updateProperties() {
        if (entry == null) {
            return;
        }
        machineNameProperty.set(entry.getEquipmentName());
        jobNameProperty.set(entry.getJobName());
        startDateProperty.set(entry.getStartDate());
        hourCountProperty.set(entry.getBill().getTotalHours());
        jobNotesProperty.set(entry.getNotes());
        jobNotesVisible.set(entry.isShowNotes());
    }

    public void loadCards() {
        if (entry == null) {
            System.out.println("Job is null");
            return;
        }
        partCardsProperty.clear();
        entry.getBill().getPartCharges().forEach(part -> {
            try {
                FXMLLoader cardLoader = App.getLoader("CustomerRecordsPage", "InvoiceJobCardPartCard");
                Parent card = cardLoader.load();
                ((InvoiceJobPartCardController) cardLoader.getController()).setPartCharge(part);
                partCardsProperty.add(card);
            }
            catch (IOException e) {
                System.out.println("Error loading part card: " + e.getMessage());
            }
        });
    }

    public void goToJob() {
        if (entry == null) {
            System.out.println("Job is null");
            return;
        }
        JobDAO job = entry.getJob();
        if (job == null) {
            System.out.println("Job not found");
            return;
        }
        App.setSceneProperty("jobDAO", job);
        App.setBackPointer("CustomerRecordsPage", "CustomerRecordsPage");
        try {
            App.setPage("CustomerJobDetailsPage", "CustomerJobDetailsPage");
        }
        catch (IOException e) {
            System.out.println("Error loading job details page: " + e.getMessage());
        }
    }

}
