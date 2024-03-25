package org.customer_book.Pages.InventoryPage;

import static com.mongodb.client.model.Filters.size;

import java.io.IOException;
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
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.InventoryCollection.PartDAO;
import org.customer_book.Database.ReportsCollection.ReportDAO;
import org.customer_book.Popups.AddCompatibleEquipment.AddCompatibleEquipmentController;

@Getter
@Setter
public class InventoryPageModel {

  private ObservableList<Parent> partsCardsProperty;
  private ObservableList<Parent> equipmentCards;
  private final int loadSize;

  private ObjectProperty<PartDAO> selectedPartProperty;
  private BooleanProperty editModeProperty = new SimpleBooleanProperty(false);
  private ObservableList<String> expenseCategoriesProperty = FXCollections.observableArrayList();

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

  public InventoryPageModel() {
    loadSize = 20;
    partsCardsProperty = FXCollections.observableArrayList();
    equipmentCards = FXCollections.observableArrayList();
    selectedPartProperty = new SimpleObjectProperty<PartDAO>(new PartDAO());
    loadCards();
    loadExpenseCategories();
  }

  public void toggleEditMode() {
    editModeProperty.set(!editModeProperty.get());
  }

  public void resetValues() {
    selectedPartProperty.get().resetChanges();
  }

  public void reloadParts() {
    this.partsCardsProperty.clear();
    loadCards();
  }

  public void loadCards() {
    int count = DatabaseConnection.inventoryCollection.getPartsCount();
    if (partsCardsProperty.size() > count) {
      return;
    }
    for (PartDAO part : DatabaseConnection.inventoryCollection.getParts(
      loadSize,
      partsCardsProperty.size()
    )) {
      //Generate the model and set the DAO to part
      InventoryPartCardModel partCardModel = new InventoryPartCardModel();
      partCardModel.setPart(part);
      partCardModel.setSelectedPart(selectedPartProperty);
      //Load the part card
      try {
        FXMLLoader cardLoader = App.getLoader(
          "InventoryPage",
          "InventoryPartCard"
        );
        Parent card = cardLoader.load();
        ((InventoryPartCardController) cardLoader.getController()).setModel(
            partCardModel
          );

        partsCardsProperty.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

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
                loadCards();
              }
            }
          }
        );
      App.addPopup(addPart);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean validateChanges() {
    boolean valid = true;
    String priceError = selectedPartProperty.get().validateCost();
    String chargeError = selectedPartProperty.get().validateCharge();
    String quantityError = selectedPartProperty.get().validateInStock();
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

  public void saveChanges() {
    selectedPartProperty.get().saveChanges();
    DatabaseConnection.inventoryCollection.updatePart(
      selectedPartProperty.get()
    );
    selectedPartProperty.get().resetChanges();
    reloadParts();
  }

  public void resetErrors() {
    PartNameErrorProperty.set("");
    PartNumberErrorProperty.set("");
    URLErrorProperty.set("");
    CurrentPurchasePriceErrorProperty.set("");
    ChargePriceErrorProperty.set("");
    QuantityErrorProperty.set("");
  }

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

  public void loadExpenseCategories() {
    expenseCategoriesProperty.clear();
    ReportDAO categoryReport = DatabaseConnection.reportsCollection.getReport(
      "ExpenseCategories"
    );
    if (categoryReport != null) {
      expenseCategoriesProperty.addAll(categoryReport.getValues());
    }
  }
  public void addCompatibleEquipment(){
    try {
      FXMLLoader addCompatibleEquipmentLoader = App.getLoader("Popups", "AddCompatibleEquipment");
      Parent addCompatibleEquipment = addCompatibleEquipmentLoader.load();
      ((AddCompatibleEquipmentController) addCompatibleEquipmentLoader.getController()).setPart(selectedPartProperty);
      addCompatibleEquipment.sceneProperty().addListener(new ChangeListener<Scene>() {
        @Override
        public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
          if(newValue == null){
            selectedPartProperty.set(DatabaseConnection.inventoryCollection.getPartByID(selectedPartProperty.get().getId()));
            reloadParts();
            loadCompatibleEquipment();
          }
        }
      });
      App.addPopup(addCompatibleEquipment);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void loadCompatibleEquipment(){
    equipmentCards.clear();
    selectedPartProperty.get().getCompatibleEquipmentList().forEach(equipment -> {
      try {
        FXMLLoader equipmentCardLoader = App.getLoader("InventoryPage", "CompatibleEquipmentCard");
        Parent equipmentCard = equipmentCardLoader.load();
        ((InventoryCompatibleMachineCardController)equipmentCardLoader.getController()).setEquipment(equipment);
        equipmentCards.add(equipmentCard);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }
}
