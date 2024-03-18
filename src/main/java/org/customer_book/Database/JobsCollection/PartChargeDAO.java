package org.customer_book.Database.JobsCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString@NoArgsConstructor
public class PartChargeDAO {
    private double charge;
    private double cost;
    private String partNumber;
    private int quantity;
    private double total;
    
}
