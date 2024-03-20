package org.customer_book.Pages.EquipmentPage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class EquipmentPageController {

  @FXML
  private AnchorPane Content;

  @FXML
  private ListView<?> CompatiblePartCardList;

  @FXML
  private Label SelectedModelName;

  @FXML
  private ListView<Parent> EquipmentCardList;

  @FXML
  private VBox SelectedModelDetails;

  @FXML
  private TextArea selectedEquipmentNotes;

  @FXML
  void AddNewModel(ActionEvent event) {
    model.showNewEquipment();
  }

  @FXML
  void CloseSelectedModel(ActionEvent event) {}

  @FXML
  void addPart(ActionEvent event) {}

  @FXML
  void createAndAddPart(ActionEvent event) {}

  private EquipmentPageModel model;

  @FXML
  void initialize() {
    //-----------------initialise model-----------------
    model = new EquipmentPageModel();

    //-----------------Error handeling for undetected fxID's-----------------
    assert CompatiblePartCardList !=
    null : "fx:id=\"CompatiblePartCardList\" was not injected: check your FXML file 'EquipmentPage.fxml'.";
    assert SelectedModelName !=
    null : "fx:id=\"SelectedModelName\" was not injected: check your FXML file 'EquipmentPage.fxml'.";
    assert EquipmentCardList !=
    null : "fx:id=\"EquipmentCardList\" was not injected: check your FXML file 'EquipmentPage.fxml'.";
    assert SelectedModelDetails !=
    null : "fx:id=\"SelectedModelDetails\" was not injected: check your FXML file 'EquipmentPage.fxml'.";
    assert selectedEquipmentNotes !=
    null : "fx:id=\"selectedEquipmentNotes\" was not injected: check your FXML file 'EquipmentPage.fxml'.";

    //-----------------initialise view-----------------
    EquipmentCardList.setItems(model.getEquipmentCards());
    SelectedModelDetails
      .visibleProperty()
      .bind(model.getSelectedModelVisible());
    selectedEquipmentNotes.textProperty().bindBidirectional(model.getNotes());
    SelectedModelName.textProperty().bind(model.getModelNumber());

    //-----------------initialise listeners-----------------
    //This listenr pushes changes to the model notes when the text area loses focus
    selectedEquipmentNotes
    .focusedProperty()
    .addListener(
      (obs, oldVal, newVal) -> {
        if (!newVal) {
          model.pushChanges(selectedEquipmentNotes.getText());
        }
      }
    );
  }
}
