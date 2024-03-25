package org.customer_book.Pages.InventoryPage;

import org.customer_book.Database.InventoryCollection.PartDAO;

import javafx.beans.property.ObjectProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@NoArgsConstructor@ToString
public class InventoryPartCardModel {
    private PartDAO part;
    private ObjectProperty<PartDAO> selectedPart;

    public void setSelfActive(){
      
        selectedPart.set(part);
    }

    
}
