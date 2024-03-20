package org.customer_book.Pages.EquipmentPage;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;

@Getter
@Setter
public class EquipmentPageModel {

  //List of Equipment Cards loaded from the DB
  private ObservableList<Parent> equipmentCards;
  //Currently selected model
  private EquipmentDAO selectedModel;
  //boolean to show the details section
  private SimpleBooleanProperty selectedModelVisible;

  //--------------------FXML Attributes from DAO----------------//
  private SimpleStringProperty modelNumber = new SimpleStringProperty();
  private SimpleStringProperty notes = new SimpleStringProperty();

  /**
   * Constructor for the EquipmentPageModel
   * This sets the list of Equipment cards by loading the equipment from the DB
   * then creating new cards for each and setting a refrence to their own DAO
   */
  public EquipmentPageModel() {
    equipmentCards = FXCollections.observableArrayList();
    selectedModelVisible = new SimpleBooleanProperty(false);
    selectedModel = new EquipmentDAO();
    loadEquipment();
  }

  /**
   * loadEquipment:
   * -this function access the database and loads a list of the first 20 equipment
   * -Once loaded it goes through each equipment and creates a new card and sets the DAO
   * -It then adds the card to the list of equipment cards to be displayed
   */
  private void loadEquipment() {
    ArrayList<EquipmentDAO> equipment = DatabaseConnection.equipmentCollection.getEquipmentList(
      20,
      0
    );
    FXMLLoader cardLoader;
    for (EquipmentDAO e : equipment) {
      try {
        cardLoader = App.getLoader("EquipmentPage", "ModelCard");
        equipmentCards.add(cardLoader.load());
        ((EquipmentModelCardController) cardLoader.getController()).setDAO(e);
        (
          (EquipmentModelCardController) cardLoader.getController()
        ).setPageModel(this);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * setSelectedModel:
   * -This function sets the selected model to the DAO passed in
   * -It then sets the selectedModelVisible to true
   * -Finally it updates all bindings to the selected model
   * @param dao
   */
  public void setSelectedModel(EquipmentDAO dao) {
    //Update the dao refrence
    selectedModel = dao;
    //Set the selected model to visible
    selectedModelVisible.set(true);
    //ReBind the selected model to the FXML attributes
    modelNumber.bind(selectedModel.getEquipmentModelNumberProperty());
    notes.set(selectedModel.getNotes());
  }

  /**
   * pushChanges:
   * Takes in the updated notes and pushes the changes to the database object
   */
  public void pushChanges(String Notes) {
    System.out.println(Notes);
    selectedModel.setNotes(Notes);
    DatabaseConnection.equipmentCollection.updateEquipment(selectedModel);
  }

  /**
   *
   */
  public void reloadEquipment() {
    equipmentCards.clear();
    loadEquipment();
  }

  /**
   *
   */
  public void showNewEquipment() {
    try {
      Parent createPage = App.loadPage("Popups", "CreateEquipment");
      createPage
        .sceneProperty()
        .addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
                if(newValue == null) {
                    reloadEquipment();
                }
            }
            
        });
      App.addPopup(createPage);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
