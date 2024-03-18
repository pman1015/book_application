package org.customer_book.Database.JobsCollection;

import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillDAO {

  private double billTotal;
  private double deliveryCost;
  private String laborCost;
  private String partsCost;
  private ArrayList<LaobrChargeDAO> laborCharges;
  private ArrayList<PartChargeDAO> partCharges;
}
