package org.customer_book.Pages.JobsListPage.Create;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class JobCreateController {

    @FXML
    private VBox EquipmentInput;

    @FXML
    private Label DescriptionError;

    @FXML
    private VBox JobDescriptionInput;

    @FXML
    private VBox CustomerNameInput;

    @FXML
    private Label EquipmentNameError;

    @FXML
    private VBox JobNameInput;

    @FXML
    private ComboBox<String> EquipmentNameComboBox;

    @FXML
    private TextArea JobDescriptionArea;

    @FXML
    private TextField JobNameTextField;

    @FXML
    private ComboBox<String> CustomerNameComboBox;

    @FXML
    private Label CustomerNameError;

    @FXML
    private Label JobNameError;

    @FXML
    void closeCreateJob(ActionEvent event) {
        model.closeJobCreate();
    }

    @FXML
    void createJob(ActionEvent event) {
        model.createJob();

    }

    private JobCreateModel model;

    @FXML
    void initialize() {
        model = new JobCreateModel();
        Platform.runLater(() -> {
            bindValues();
        });
        CustomerNameComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !newValue.equals(oldValue)) {
                ArrayList<String> refinedValues = model.Refinevalues(model.getCustomerNameList(), newValue);
                if (refinedValues != null) {
                    model.setCustomerNames(refinedValues);
                    model.resetCustomerName();
                    CustomerNameComboBox.getEditor().textProperty().set(newValue);
                }
            }
        });

        EquipmentNameComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                ArrayList<String> refinedValues = model.Refinevalues(model.getEquipmentNameList(), newValue);
                if (refinedValues != null) {
                    model.setEquipmentNames(refinedValues);
                    model.resetEquipmentName();
                    EquipmentNameComboBox.getEditor().textProperty().set(newValue);
                }
            }
        });

    }

    private void bindValues() {
        CustomerNameComboBox.itemsProperty().bindBidirectional(model.getCustomerNamesProperty());
        EquipmentNameComboBox.itemsProperty().bindBidirectional(model.getEquipmentNamesProperty());
        CustomerNameComboBox.valueProperty().bindBidirectional(model.getCustomerName());
        EquipmentNameComboBox.valueProperty().bindBidirectional(model.getEquipmentName());
        JobNameTextField.textProperty().bindBidirectional(model.getJobName());
        JobDescriptionArea.textProperty().bindBidirectional(model.getJobDescription());
        CustomerNameError.textProperty().bind(model.getCustomerNameError());
        EquipmentNameError.textProperty().bind(model.getEquipmentNameError());
        JobNameError.textProperty().bind(model.getJobNameError());
        DescriptionError.textProperty().bind(model.getJobDescriptionError());
    }
}
