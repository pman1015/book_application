package org.customer_book.Pages.EquipmentPage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;

@Getter
@Setter
public class EquipmentModelCardController {

  @FXML
  private Label ModelName;

  @FXML
  void ChangeSelectedModel() {
    pageModel.setSelectedModel(this.dao);
  }

  private EquipmentModelCardModel model;
  private EquipmentDAO dao;
  private EquipmentPageModel pageModel;

  @FXML
  void initialize() {
    model = new EquipmentModelCardModel();
    assert ModelName !=
    null : "fx:id=\"ModelName\" was not injected: check your FXML file 'ModelCard.fxml'.";
  }

  public void setDAO(EquipmentDAO dao) {
    this.dao = dao;
    ModelName.textProperty().bind(this.dao.getEquipmentModelNumberProperty());
  }
}
