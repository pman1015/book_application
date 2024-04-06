package org.customer_book.Database.InvoiceCollection;

import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.BillDAO;
import org.customer_book.Database.JobsCollection.JobDAO;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceDAO {

  private ObjectId id;

  private String invoiceNumber;

  private String customerName;
  private ObjectId customerId;

  private String status;

  private ArrayList<ObjectId> jobIDs;

  private String generatedDate;

  private ArrayList<InvoiceEntry> bills;
  private String totalCost;

  public void newInvoice(
    String invoiceNumber,
    ArrayList<JobDAO> jobs,
    String customerName
  ) {
    this.invoiceNumber = invoiceNumber;
    this.customerName = customerName;
    CustomerDAO customer = DatabaseConnection.customerCollection.findByName(
      customerName
    );
    this.customerId = customer.getId();
    this.status = "Awaiting Payment";
    this.jobIDs = new ArrayList<>();
    this.bills = new ArrayList<>();
    double total = 0;
    for (JobDAO job : jobs) {
      this.jobIDs.add(job.getId());
      InvoiceEntry entry = new InvoiceEntry();
      entry.setEquipmentId(job.getEquipment());
      entry.setEquipmentName(
        DatabaseConnection.equipmentCollection
          .getEquipment(job.getEquipment())
          .getModelNumber()
      );
      entry.setBill(job.getBill());
      total += job.getBill().getBillTotal();
      this.bills.add(entry);
    }
    this.totalCost = String.valueOf(total);
    this.generatedDate = java.time.LocalDate.now().toString();
  }

public ArrayList<String> getEquipment() {
    ArrayList<String> equipment = new ArrayList<>();
    for (InvoiceEntry entry : bills) {
      equipment.add(entry.getEquipmentName());
    }
    return equipment;
}
}
