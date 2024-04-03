package org.customer_book.Database.JobsCollection;

import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.Database.InventoryCollection.PartDAO;

@Getter
@Setter
@NoArgsConstructor
public class BillDAO {

  private double billTotal;
  private double deliveryCost;
  private String laborCost;
  private String partsCost;
  private ArrayList<LaborChargeDAO> laborCharges;
  private ArrayList<PartChargeDAO> partCharges;

  public void updateDeliveryCost(double deliveryCost) {
    this.deliveryCost = deliveryCost;
    updateBill();
  }

  public void removeLaborCharge(LaborChargeDAO laborCharge) {
    laborCharges.remove(laborCharge);
    updateBill();
  }

  public void addNewLaborCharge(String chargeName, double rate, double hours) {
    if (laborCharges == null) {
      laborCharges = new ArrayList<>();
    }
    LaborChargeDAO newLaborCharge = new LaborChargeDAO(chargeName, rate, hours);
    laborCharges.add(newLaborCharge);
    updateBill();
  }

  public void addNewPartCharge(PartDAO part, int quantity) {
    if (partCharges == null) {
      partCharges = new ArrayList<>();
    }
    PartChargeDAO newPartCharge = new PartChargeDAO(part, quantity);
    partCharges.add(newPartCharge);
    updateBill();
  }

  public void updateBill() {
    double newTotal = 0;
    partsCost = "0.00";
    if (partCharges != null) {
      for (PartChargeDAO partCharge : partCharges) {
        partsCost =
          String.format(
            "%.2f",
            Double.parseDouble(partsCost) + partCharge.getTotal()
          );
      }
    }
    laborCost = "0.00";
    if (laborCharges != null) {
      for (LaborChargeDAO laborCharge : laborCharges) {
        laborCost =
          String.format(
            "%.2f",
            Double.parseDouble(laborCost) + laborCharge.getCost()
          );
      }
    }
    newTotal += Double.parseDouble(partsCost);
    newTotal += Double.parseDouble(laborCost);
    newTotal += deliveryCost;
    setBillTotal(newTotal);
  }

  public void updatePartCharge(PartChargeDAO partCharge, int quantity) {
    ArrayList<PartChargeDAO> newPartCharges = new ArrayList<>();
    this.partCharges.forEach(part -> {
        if (part.equals(partCharge)) {
          part.updateQuanity(quantity);
          newPartCharges.add(part);
        } else {
          newPartCharges.add(part);
        }
      });
    this.partCharges = newPartCharges;
    updateBill();
  }

  public void removePartCharge(PartChargeDAO partChargeDAO) {
    partCharges.remove(partChargeDAO);
    updateBill();
  }
}
