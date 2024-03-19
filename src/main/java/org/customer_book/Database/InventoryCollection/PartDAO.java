package org.customer_book.Database.InventoryCollection;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PartDAO {

  //--------------------DAO atributs--------------------//
  private ObjectId id;
  private double charge;
  private int inStock;
  private double cost;
  private String partNumber;
  private String partName;
  private String url;
  private ArrayList<ObjectId> compatibleEquipment;

  //--------------------FXML Attributes----------------//
  @BsonIgnore
  private StringProperty partNameProperty = new SimpleStringProperty();

  @BsonIgnore
  private StringProperty partNumberProperty = new SimpleStringProperty();

  @BsonIgnore
  private StringProperty partCostProperty = new SimpleStringProperty();

  @BsonIgnore
  private StringProperty partChargeProperty = new SimpleStringProperty();

  @BsonIgnore
  private StringProperty partInStockProperty = new SimpleStringProperty();

  @BsonIgnore
  private StringProperty partURLProperty = new SimpleStringProperty();

  //--------------------Calculated --------------------//
  @BsonIgnore
  private ArrayList<EquipmentDAO> compatibleEquipmentList = new ArrayList<EquipmentDAO>();

  //--------------------Setters ----------------------//
  public void setPartName(String partName) {
    this.partName = partName;
    this.partNameProperty.set(partName);
  }

  public void setPartNumber(String partNumber) {
    this.partNumber = partNumber;
    this.partNumberProperty.set(partNumber);
  }

  public void setCost(double cost) {
    this.cost = cost;
    this.partCostProperty.set(Double.toString(cost));
  }

  public void setCharge(double charge) {
    this.charge = charge;
    this.partChargeProperty.set(Double.toString(charge));
  }

  public void setInStock(int inStock) {
    this.inStock = inStock;
    this.partInStockProperty.set(Integer.toString(inStock));
  }

  public void setUrl(String url) {
    this.url = url;
    this.partURLProperty.set(url);
  }

  public void setCompatibleEquipment(ArrayList<ObjectId> compatibleEquipment) {
    this.compatibleEquipment = compatibleEquipment;
    compatibleEquipmentList =
      DatabaseConnection.equipmentCollection.getEquipmentList(
        compatibleEquipment
      );
  }
}
