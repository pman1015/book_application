package org.customer_book.Pages.InventoryPage;

import org.customer_book.Database.InventoryCollection.PartDAO;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@NoArgsConstructor@ToString
public class InventoryPartCardModel {
    
    private PartDAO part;
    private ObjectProperty<PartDAO> selectedPart;

    //------------------- FXML Properties ------------------- //
    private StringProperty partName = new SimpleStringProperty("");
    private StringProperty partNumber = new SimpleStringProperty("");

    public void setSelfActive(){
        selectedPart.set(part);
    }
    public void setPart(PartDAO part, ObjectProperty<PartDAO> selectedPart){
        this.part = part;
        this.selectedPart = selectedPart;
        partName.set(part.getPartName());
        partNumber.set(part.getPartNumber());
    } 
}
