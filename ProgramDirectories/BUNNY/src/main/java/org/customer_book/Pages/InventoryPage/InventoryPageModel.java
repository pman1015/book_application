package org.customer_book.Pages.InventoryPage;

import static com.mongodb.client.model.Filters.ne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
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
import javafx.scene.Scene;
import lombok.Getter;
import lombok.Setter;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;
import org.customer_book.Database.InventoryCollection.PartDAO;
import org.customer_book.Database.ReportsCollection.ReportDAO;
import org.customer_book.Popups.AddCompatibleEquipment.AddCompatibleEquipmentController;
import org.customer_book.Popups.InventoryFilter.InventoryFilterController;

@Getter
@Setter
public class InventoryPageModel {

  //-------------------Lists to store cards in view -------------------
  private ObservableList<Parent> partsCardsProperty;
  private ObservableList<Parent> equipmentCards;
  private ObservableList<String> expenseCategoriesProperty = FXCollections.observableArrayList();
  private ArrayList<PartDAO> partDAOs = new ArrayList<>();

  private ObjectProperty<Bson> partFilters = new SimpleObjectProperty<>();

  //Stores the currently selected part
  private ObjectProperty<PartDAO> selectedPartProperty;
  //Stores the edit mode for the part details
  private BooleanProperty editModeProperty = new SimpleBooleanProperty(false);

  //--------------------Error Properties--------------------
  private StringProperty PartNameErrorProperty = new SimpleStringProperty("");
  private StringProperty PartNumberErrorProperty = new SimpleStringProperty("");
  private StringProperty URLErrorProperty = new SimpleStringProperty("");
  private StringProperty CurrentPurchasePriceErrorProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty ChargePriceErrorProperty = new SimpleStringProperty(
    ""
  );
  private StringProperty QuantityErrorProperty = new SimpleStringProperty("");

  /**
   * Constructor for the InventoryPageModel
   * Initializes the ObservableLists and loads the parts
   * Then loads the avaliable expense categories from reports database
   */
  public InventoryPageModel() {
    partFilters.set(ne("_id", "null"));
    partsCardsProperty = FXCollections.observableArrayList();
    equipmentCards = FXCollections.observableArrayList();
    selectedPartProperty = new SimpleObjectProperty<PartDAO>(new PartDAO());
    loadPartDAOs(true);
    loadExpenseCategories();
  }

  /**
   * Toggles the edit mode for the part details
   */
  public void toggleEditMode() {
    editModeProperty.set(!editModeProperty.get());
  }

  /**
   * Resets the values of the selected part to the original values
   * as it is stored in the database
   */
  public void resetValues() {
    selectedPartProperty.get().resetChanges();
  }

  /**
   * Reloads the parts from the inventory collection
   */
  public void reloadParts() {
    this.partsCardsProperty.clear();
    loadPartDAOs(true);
  }

  /**
   * Load the daos based on the selected filters if clearExisting is true then clear all of
   * the existing daos and load the first 24 parts else add the new parts to the exiting list
   */
  public void loadPartDAOs(boolean clearExisting) {
    if (clearExisting) {
      partDAOs =
        DatabaseConnection.inventoryCollection.getFilteredParts(
          partFilters.get(),
          24,
          0
        );
    } else {
      partDAOs.addAll(
        DatabaseConnection.inventoryCollection.getFilteredParts(
          partFilters.get(),
          24,
          partDAOs.size()
        )
      );
    }
    loadCards();
  }

  /**
   * Loads a partCard for each loaded DAO
   */
  public void loadCards() {
    partsCardsProperty.clear();
    partDAOs.forEach(part -> {
      try {
        FXMLLoader partCardLoader = App.getLoader(
          "InventoryPage",
          "InventoryPartCard"
        );
        Parent partCard = partCardLoader.load();
        ((InventoryPartCardController) partCardLoader.getController()).setPart(
            part,
            selectedPartProperty
          );
        partsCardsProperty.add(partCard);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Shows the add part popup
   * Loads the CreatePart.fxml file and adds it to the popup stage
   * Then once the popup is closed it reloads the parts
   */
  public void showAddPart() {
    try {
      FXMLLoader addPartLoader = App.getLoader("Popups", "CreatePart");
      Parent addPart = addPartLoader.load();
      addPart
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
                loadPartDAOs(true);
              }
            }
          }
        );
      App.addPopup(addPart);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Validates the changes made to the selected part
   * the function validates all of the fields in the selected part
   * if all of the fields are valid it returns true
   * @return
   */
  public boolean validateChanges() {
    boolean valid = true;
    //Store the error message for each field
    String priceError = selectedPartProperty.get().validateCost();
    String chargeError = selectedPartProperty.get().validateCharge();
    String quantityError = selectedPartProperty.get().validateInStock();
    //Run validation checks on the fields and updates the error properties
    if (selectedPartProperty.get().getPartName().equals("")) {
      PartNameErrorProperty.set("Part Name cannot be empty");
      valid = false;
    } else {
      PartNameErrorProperty.set("");
    }
    if (selectedPartProperty.get().getPartNumber().equals("")) {
      PartNumberErrorProperty.set("Part Number cannot be empty");
      valid = false;
    } else {
      PartNumberErrorProperty.set("");
    }
    if (priceError != null) {
      CurrentPurchasePriceErrorProperty.set(priceError);
      valid = false;
    } else {
      CurrentPurchasePriceErrorProperty.set("");
    }
    if (chargeError != null) {
      ChargePriceErrorProperty.set(chargeError);
      valid = false;
    } else {
      ChargePriceErrorProperty.set("");
    }
    if (quantityError != null) {
      QuantityErrorProperty.set(quantityError);
      valid = false;
    } else {
      QuantityErrorProperty.set("");
    }
    return valid;
  }

  /**
   * Saves the changes made to the selected part
   * Then reloads the parts list
   */
  public void saveChanges() {
    selectedPartProperty.get().saveChanges();
    DatabaseConnection.inventoryCollection.updatePart(
      selectedPartProperty.get()
    );
    selectedPartProperty.get().resetChanges();
    reloadParts();
  }

  /**
   * Resets the error properties
   * When the selected part changes reset all of the errors
   */
  public void resetErrors() {
    PartNameErrorProperty.set("");
    PartNumberErrorProperty.set("");
    URLErrorProperty.set("");
    CurrentPurchasePriceErrorProperty.set("");
    ChargePriceErrorProperty.set("");
    QuantityErrorProperty.set("");
  }

  /**
   * Shows the add expense category popup
   * Loads the ExpenseCategoryCreate.fxml file and adds it to the popup stage
   * Then once the popup is closed it reloads the expense categories
   */
  public void showAddExpenseCategory() {
    try {
      FXMLLoader expenseCategoryCreate = App.getLoader(
        "Popups",
        "ExpenseCategoryCreate"
      );
      Parent expenseCategory = expenseCategoryCreate.load();
      expenseCategory
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
                loadExpenseCategories();
              }
            }
          }
        );
      App.addPopup(expenseCategory);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the expense categories from the reports database
   */
  public void loadExpenseCategories() {
    expenseCategoriesProperty.clear();
    ReportDAO categoryReport = DatabaseConnection.reportsCollection.getReport(
      "ExpenseCategories"
    );
    if (categoryReport != null) {
      expenseCategoriesProperty.addAll(categoryReport.getValues());
    }
  }

  /**
   * Shows the popup to add a new compatible equipment
   * When the popup closes reload the parts and the compatible equipment
   * for the selected part
   */
  public void addCompatibleEquipment() {
    try {
      FXMLLoader addCompatibleEquipmentLoader = App.getLoader(
        "Popups",
        "AddCompatibleEquipment"
      );
      Parent addCompatibleEquipment = addCompatibleEquipmentLoader.load();
      (
        (AddCompatibleEquipmentController) addCompatibleEquipmentLoader.getController()
      ).setPart(selectedPartProperty);
      addCompatibleEquipment
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
                selectedPartProperty.set(
                  DatabaseConnection.inventoryCollection.getPartByID(
                    selectedPartProperty.get().getId()
                  )
                );
                reloadParts();
                loadCompatibleEquipment();
              }
            }
          }
        );
      App.addPopup(addCompatibleEquipment);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the compatible equipment for the selected part
   * Create a card object and set a refrence to this model and
   * the equipment DAO for the equipment
   * Then add the card to the equipmentCards list to be displayed
   * in the view
   */
  public void loadCompatibleEquipment() {
    equipmentCards.clear();
    selectedPartProperty
      .get()
      .getCompatibleEquipmentList()
      .forEach(equipment -> {
        try {
          FXMLLoader equipmentCardLoader = App.getLoader(
            "InventoryPage",
            "CompatibleEquipmentCard"
          );
          Parent equipmentCard = equipmentCardLoader.load();
          (
            (InventoryCompatibleMachineCardController) equipmentCardLoader.getController()
          ).setEquipment(equipment);
          (
            (InventoryCompatibleMachineCardController) equipmentCardLoader.getController()
          ).setInventoryPageModel(this);
          equipmentCards.add(equipmentCard);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
  }

  /**
   * Removes the selected equipment from the compatible equipment list
   * Then updates the part in the database and reloads the compatible equipment
   * @param equipment
   */
  public void removeMachine(EquipmentDAO equipment) {
    DatabaseConnection.inventoryCollection.removeCompatibleEquipment(
      selectedPartProperty.get().getId(),
      equipment.getId()
    );
    DatabaseConnection.equipmentCollection.removeCompatiblePart(
      equipment.getId(),
      selectedPartProperty.get().getId()
    );
    selectedPartProperty.set(
      DatabaseConnection.inventoryCollection.getPartByID(
        selectedPartProperty.get().getId()
      )
    );
    reloadParts();
    loadCompatibleEquipment();
  }

  public void loadMoreParts() {
    loadPartDAOs(false);
  }

  public void showFilterOptions() {
    try {
      FXMLLoader popupLoader = App.getLoader("Popups", "InventoryFilter");
      Parent popup = popupLoader.load();
      ((InventoryFilterController) popupLoader.getController()).setFilter(
          partFilters
        );
      popup
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
                loadPartDAOs(true);
              }
            }
          }
        );
      App.addPopup(popup);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
