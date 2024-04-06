package org.customer_book.Database.InvoiceCollection;

import org.bson.types.ObjectId;
import org.customer_book.Database.JobsCollection.BillDAO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class InvoiceEntry {
    private ObjectId EquipmentId;
    private String EquipmentName;
    private BillDAO bill;
    
}
