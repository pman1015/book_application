package org.customer_book.Database.JobsCollection;

import org.bson.types.ObjectId;
import org.customer_book.Database.InventoryCollection.PartDAO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString@NoArgsConstructor
public class PartChargeDAO {
    private double charge;
    private double cost;
    private String partNumber;
    private String partName;
    private ObjectId partId;
    private int quantity;
    private double total;

    public PartChargeDAO(PartDAO part, int quantity){
        this.partId = part.getId();
        this.partName = part.getPartName();
        this.partNumber = part.getPartNumber();
        this.charge = part.getCharge();
        this.cost = part.getCost();
        this.quantity = quantity;
        this.total = quantity * charge;
    }
    public void updateQuanity(int quantity){
        this.quantity = quantity;
        this.total = quantity * charge;
    }
    
}
