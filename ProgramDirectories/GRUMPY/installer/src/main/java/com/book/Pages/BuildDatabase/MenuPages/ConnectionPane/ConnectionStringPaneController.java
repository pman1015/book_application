package com.book.Pages.BuildDatabase.MenuPages.ConnectionPane;

import com.book.Pages.BuildDatabase.MenuPages.PageControllerAbstract;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class ConnectionStringPaneController extends PageControllerAbstract{

    @FXML
    private TextField UserNameField;

    @FXML
    private VBox ConnectionStringPane;

    @FXML
    private ToggleGroup ConnectionType;

    @FXML
    private TextField HostField;

    @FXML
    private TextField PortField;

    @FXML
    private TextField ConnectionStringField;

    @FXML
    private VBox BuildStringPane;

    @FXML
    private Label PasswordError;

    @FXML
    private Label UserNameError;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private Label HostError;

    @FXML
    private Label PortError;

    @FXML
    private Label ConnectionStringError;


    @FXML
    void initialize() {
        model = new ConnectionStringPaneModel();

        // Add listner to the toggleGroup
        ConnectionType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            ((ConnectionStringPaneModel) model).toggleConnectionType();
        });
        // Bind the visibility of the panes
        ConnectionStringPane.visibleProperty().bind(((ConnectionStringPaneModel) model).getHasConnectionString());
        BuildStringPane.visibleProperty().bind(((ConnectionStringPaneModel) model).getHasConnectionString().not());

        // Bind the text fields
        ConnectionStringField.textProperty().bindBidirectional(((ConnectionStringPaneModel) model).getConnectionString());
        HostField.textProperty().bindBidirectional(((ConnectionStringPaneModel) model).getHost());
        PortField.textProperty().bindBidirectional(((ConnectionStringPaneModel) model).getPort());
        PasswordField.textProperty().bindBidirectional(((ConnectionStringPaneModel) model).getPassword());
        UserNameField.textProperty().bindBidirectional(((ConnectionStringPaneModel) model).getUsername());

        // Bind the error labels
        ConnectionStringError.textProperty().bind(((ConnectionStringPaneModel) model).getConnectionStringError());
        HostError.textProperty().bind(((ConnectionStringPaneModel) model).getHostError());
        PortError.textProperty().bind(((ConnectionStringPaneModel) model).getPortError());
        PasswordError.textProperty().bind(((ConnectionStringPaneModel) model).getPasswordError());
        UserNameError.textProperty().bind(((ConnectionStringPaneModel) model).getUsernameError());

    }
   
}
