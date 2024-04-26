package org.customer_book.Database.InvoiceCollection;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonIgnore;
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
  private String generatedDate;
  private Date generatedDateTime;
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
    this.bills = new ArrayList<>();
    double total = 0;
    for (JobDAO job : jobs) {
      InvoiceEntry entry = new InvoiceEntry();
      entry.setEquipmentId(job.getEquipment());
      entry.setEquipmentName(
        DatabaseConnection.equipmentCollection
          .getEquipment(job.getEquipment())
          .getModelNumber()
      );
      entry.setNotes(job.getDetails());
      entry.setBill(job.getBill());
      entry.setJobId(job.getId());
      entry.setShowNotes(false);
      total += job.getBill().getBillTotal();
      this.bills.add(entry);
    }
    this.totalCost = String.valueOf(total);
    this.generatedDate =
      java.time.LocalDate
        .now()
        .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        .toString();
    this.generatedDateTime = new java.util.Date();
  }

  public ArrayList<String> getEquipment() {
    ArrayList<String> equipment = new ArrayList<>();
    for (InvoiceEntry entry : bills) {
      equipment.add(entry.getEquipmentName());
    }
    return equipment;
  }

  @BsonIgnore
  public String getCustomerAddress() {
    return DatabaseConnection.customerCollection
      .findCustomerById(customerId)
      .getAddress();
  }

  @BsonIgnore
  public String getChargeTotal() {
    return totalCost;
  }

  @BsonIgnore
  public String getDeliveryTotal() {
    double deliveryTotal = 0;
    if (bills != null) {
      for (InvoiceEntry entry : bills) {
        deliveryTotal += entry.getBill().getDeliveryCost();
      }
    }

    return String.valueOf(deliveryTotal);
  }

  @BsonIgnore
  public String getLaborTotal() {
    double laborTotal = 0;
    if (bills != null) {
      for (InvoiceEntry entry : bills) {
        if (entry.getBill().getLaborCost() != null) {
          laborTotal += Double.valueOf(entry.getBill().getLaborCost());
        }
      }
    }

    return String.valueOf(laborTotal);
  }

  @BsonIgnore
  public String getCustomerPhoneNumber() {
    return DatabaseConnection.customerCollection
      .findCustomerById(customerId)
      .getPhoneNumber();
  }

  @BsonIgnore
  public void updateEntry(InvoiceEntry entry) {
    for (InvoiceEntry e : bills) {
      if (e.getJobId().equals(entry.getJobId())) {
        e.setNotes(entry.getNotes());
        break;
      }
    }
  }

  @BsonIgnore
  public String getPartsTotal(){
    double partsTotal = 0;
    if(bills != null){
      for(InvoiceEntry entry : bills){
        partsTotal += Double.valueOf(entry.getBill().getPartsCost());
      }
    }
    return "$"+partsTotal;
  }
}
