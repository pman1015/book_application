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
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.InventoryCollection.PartDAO;
import org.customer_book.Popups.AddCompatiblePart.AddCompatiblePartController;

@Getter
@Setter
public class EquipmentPageModel {

  //List of Equipment Cards loaded from the DB
  private ObservableList<Parent> equipmentCards;
  private ObservableList<Parent> compatiblePartCards;
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
    compatiblePartCards = FXCollections.observableArrayList();
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
   * load the compatible parts for the selected model
   * @param dao
   */
  public void loadCompatibleParts(EquipmentDAO dao) {
    compatiblePartCards.clear();
    ArrayList<PartDAO> partsToLoad = DatabaseConnection.inventoryCollection.getSelectParts(
      dao.getParts()
    );
    if (partsToLoad.size() == 0) {
      return;
    }
    for (PartDAO part : partsToLoad) {
      try {
        FXMLLoader cardLoader = App.getLoader("EquipmentPage", "PartCard");
        compatiblePartCards.add(cardLoader.load());
        ((EquipmentPartCardController) cardLoader.getController()).setDAO(part);
      } catch (IOException e) {
        e.printStackTrace();
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
    //load the compatible parts
    loadCompatibleParts(dao);
  }

  /**
   * closeSelectedModel:
   * This function sets the visibility of the selected model to false
   * this hides the details section
   * @param Notes
   */
  public void closeSelectedModel() {
    selectedModelVisible.set(false);
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
   *reload the equipment list from the database
   */
  public void reloadEquipment() {
    equipmentCards.clear();
    loadEquipment();
  }

  /**
   *This Function opens the create equipment popup
   *it also adds a listener to the scene property of the popup
   *so that when the popup is closed it reloads the equipment
   */
  public void showNewEquipment() {
    try {
      //Load the create equipment popup node
      Parent createPage = App.loadPage("Popups", "CreateEquipment");
      //Add an event listenr for when the popup is closed
      createPage
        .sceneProperty()
        .addListener(
          new ChangeListener<Scene>() {
            @Override
            public void changed(
              ObservableValue<? extends Scene> observable,
              Scene oldValue,
              Scene newValue
            ) {
              if (newValue == null) {
                reloadEquipment();
              }
            }
          }
        );
      App.addPopup(createPage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showAddPart() {
    try {
      FXMLLoader loader = App.getLoader("Popups", "AddCompatiblePart");
      Parent root = loader.load();
      ((AddCompatiblePartController) loader.getController()).setEquipment(
          selectedModel
        );

      App.addPopup(root);
      root
        .sceneProperty()
        .addListener(
          new ChangeListener<Scene>() {
            @Override
            public void changed(
              ObservableValue<? extends Scene> observable,
              Scene oldValue,
              Scene newValue
            ) {
              if (newValue == null) {
                reloadEquipment();
                setSelectedModel(
                  DatabaseConnection.equipmentCollection.getEquipment(
                    selectedModel.getId()
                  )
                );
              }
            }
          }
        );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
