package org.customer_book.Pages.CustomerEquipmentPage;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.InventoryCollection.PartDAO;
import org.customer_book.Popups.CompatiblePartCustomerEquipment.CompatiblePartPopupController;

@Getter
@Setter
@NoArgsConstructor
public class CompatiblePartCardModel {

  //------------------------View Properties------------------------//
  private StringProperty partName = new SimpleStringProperty("");
  private StringProperty quantity = new SimpleStringProperty("");

  //------------------------Model Properties------------------------//
  private PartDAO part;

  public void setPart(PartDAO part) {
    this.part = part;
    partName.set(part.getPartName());
    quantity.set(String.valueOf(part.getInStock()));
  }

  public void showInventoryPopup() {
    try {
      FXMLLoader popupLoder = App.getLoader("Popups", "CompatiblePartPopup");
      Parent popup = popupLoder.load();
      ((CompatiblePartPopupController) popupLoder.getController()).setPartDAO(
          part
        );
      App.addPopup(popup);
      popup
        .sceneProperty()
        .addListener((obs, oldVal, newVal) -> {
          if (newVal == null) {
            PartDAO newPart = DatabaseConnection.inventoryCollection.getPartByID(
              part.getId()
            );
            setPart(newPart);
          }
        });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
