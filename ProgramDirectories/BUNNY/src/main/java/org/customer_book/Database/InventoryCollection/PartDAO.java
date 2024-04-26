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
  private String expenseCategory;

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

  @BsonIgnore
  private StringProperty partExpenseCategoryProperty = new SimpleStringProperty();

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
  
  public void setExpenseCategory(String expenseCategory) {
    this.expenseCategory = expenseCategory;
    this.partExpenseCategoryProperty.set(expenseCategory);
  }
  public void resetChanges(){
    this.partNameProperty.set(partName);
    this.partNumberProperty.set(partNumber);
    this.partCostProperty.set(Double.toString(cost));
    this.partChargeProperty.set(Double.toString(charge));
    this.partInStockProperty.set(Integer.toString(inStock));
    this.partURLProperty.set(url);
    this.partExpenseCategoryProperty.set(expenseCategory);
  }
  public void saveChanges(){
    this.partName = this.partNameProperty.get();
    this.partNumber = this.partNumberProperty.get();
    this.cost = Double.parseDouble(this.partCostProperty.get());
    this.charge = Double.parseDouble(this.partChargeProperty.get());
    this.inStock = Integer.parseInt(this.partInStockProperty.get());
    this.url = this.partURLProperty.get();
    this.expenseCategory = this.partExpenseCategoryProperty.get();
  }

  public String validatePartName(){
    if(DatabaseConnection.inventoryCollection.partNameExists(partNameProperty.get())){
      return "Part Name already exists";
    }else{
      return null;
    }
  }
  public String validatePartNumber(){
    if(DatabaseConnection.inventoryCollection.partNumberExists(partNumberProperty.get())){
      return "Part Number already exists";
    }else{
      return null;
    }
  }
  public String validateCost(){
    if (!partCostProperty.get().matches("\\d+(\\.\\d+)?")) {
      return "Cost must be a number with no characters";
    } else {
      return null;
    }
  }
  public String validateCharge(){
    if (!partChargeProperty.get().matches("\\d+(\\.\\d+)?")) {
      return "Charge must be a number with no characters";
    } else {
      return null;
    }
  }
  public String validateInStock(){
    if (!partInStockProperty.get().matches("\\d+")) {
      return "In Stock must be a number with no characters";
    } else {
      return null;
    }
  }
  
}
