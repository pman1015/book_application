package org.customer_book.Pages.EquipmentPage;

import org.customer_book.Database.InventoryCollection.PartDAO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class EquipmentPartCardModel {
    private PartDAO part;
    private StringProperty partName;
    private StringProperty partNumber;

    public EquipmentPartCardModel(){
        partName = new SimpleStringProperty("");
        partNumber = new SimpleStringProperty("");
    }

    public void setPart(PartDAO part){
        this.part = part;
        partName.set(part.getPartName());
        partNumber.set(part.getPartNumber());
    }
}
