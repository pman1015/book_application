package org.customer_book.Pages.CustomerReportsPage.InvoiceDetails;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.util.concurrent.CompletableFuture;

import org.customer_book.Database.InvoiceCollection.InvoiceEntry;

public class InvoiceJobCardController {
    @FXML
    private AnchorPane Card;

    @FXML
    private ListView<Parent> PartCardsList;

    @FXML
    private Label MachineNameLabel;

    @FXML
    private Label JobNameLabel;

    @FXML
    private Label StartDateLabel;

    @FXML
    private Label HourCountLabel;

    @FXML
    private TextArea JobNotes;

    @FXML
    private RadioButton ShowJobNotes;

    @FXML
    void goToJob() {
        model.goToJob();
    }

    private InvoiceJobCardModel model;

    @FXML
    void initialize() {

        model = new InvoiceJobCardModel();

        CompletableFuture<Void> initaliseTextFields = CompletableFuture.runAsync(this::initalizeTextFields);
        CompletableFuture<Void> initalizeJobNotes = CompletableFuture.runAsync(this::initalizeJobNotes);
        CompletableFuture<Void> initalizePartCards = CompletableFuture.runAsync(this::initializePartCards);
        
        CompletableFuture.allOf(initaliseTextFields,initalizeJobNotes,initalizePartCards).join();
    }

    /**
     * Initalizes the text fields with the correct data
     */
    private void initalizeTextFields() {
        MachineNameLabel.textProperty().bind(model.getMachineNameProperty());
        JobNameLabel.textProperty().bind(model.getJobNameProperty());
        StartDateLabel.textProperty().bind(model.getStartDateProperty());
        HourCountLabel.textProperty().bind(model.getHourCountProperty());
    }

    /**
     * Initalise the JobNotes text area to be binded to the model and the
     * ShowJobNotes radio button also add listners to resize the text area when the
     * visibility changes
     */
    private void initalizeJobNotes() {
        JobNotes.textProperty().bind(model.getJobNotesProperty());
        // Bind the JobNotes visibility to the model and set prefered height
        // When the visibility changes
        JobNotes.visibleProperty().bind(model.getJobNotesVisible());
        Card.setPrefHeight(450);
        JobNotes.visibleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                Card.setPrefHeight(550);
            } else {
                Card.setPrefHeight(450);
            }
        });
        // Bind the ShowJobNotes radio button to the model
        ShowJobNotes.selectedProperty().bindBidirectional(model.getJobNotesVisible());
    }

    /**
     * Initalise the PartCards list view with the correct data
     * add a thread to check if the model has an invoice Entry
     * if it does then load the cards
     */
    private void initializePartCards() {
        PartCardsList.setItems(model.getPartCardsProperty());
        new CompletableFuture<>();
        // Create a loop that checks if the invoice Dao is set inside the model
        CompletableFuture<Void> loadParCards = new CompletableFuture<>();
        Thread loadCards = new Thread (() -> {
            while(!loadParCards.isDone()){
                if(model.getEntry() != null){
                    Platform.runLater(() -> {
                        model.loadCards();
                    });
                    loadParCards.complete(null);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        loadCards.start();
    }
    public void setInvoiceEntry(InvoiceEntry entry) {
        model.setInvoiceEntry(entry);
    }

}
