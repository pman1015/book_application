package org.customer_book.Utilities;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.Table;
import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;
import org.customer_book.Database.InvoiceCollection.InvoiceEntry;
import org.customer_book.Database.JobsCollection.LaborChargeDAO;
import org.customer_book.Database.JobsCollection.PartChargeDAO;

@Getter
public class InvoiceCreate {

  //Invoice Document to be created
  private Document invoice = new Document();
  //Job Section to be cloned for each job to add
  private Section JobSection;
  //Array of Job Sections to be added to the invoice
  private ArrayList<Section> JobSections = new ArrayList<Section>();

  private InvoiceDAO dao;
  private String downloadPath;
  private CustomerDAO customer;

  private Object[] state;

  public InvoiceCreate(InvoiceDAO dao, String downloadPath) {
    this.dao = dao;
    this.downloadPath = downloadPath;
    // Load the invoice template
    invoice.loadFromFile(
      App.class.getResource("").toString().replace("file:/", "") +
      "Data/InvoiceTemplate.docx"
    );

    //Error if invoice was not loaded
    if (invoice == null) {
      state = new Object[] { 1.0, "Invoice Template Not Found" };
      return;
    }
    JobSection = invoice.getSections().get(2);
    // Update Customer Information
    customer =
      DatabaseConnection.customerCollection.findCustomerById(
        dao.getCustomerId()
      );

    //Error if customer was not found
    if (customer == null) {
      state = new Object[] { 1.0, "Customer Not Found" };
      return;
    }
    state = new Object[] { .1, "Template Loaded" };
  }

  /**
   * Generate an Invoice
   *  Take in an invoiceDAO and the destination to save it and generate an invoice
   * @param dao - InvoiceDAO of the invoice to generate
   * @param downloadPath - Path to save the invoice
   * @return - String message of the result
   * @throws InterruptedException
   */
  public void updateCustomerInfo() {
    //Update the invoice information
    updateCustomerInfo(customer);
    state = new Object[] { .2, "Customer Info Updated" };
  }

  public String getMessage() {
    if (state != null) {
      return state[1].toString();
    } else {
      return "";
    }
  }

  public void updateInvoiceInfo() {
    //Update the invoice information
    updateInvoiceInfo(dao);
    state = new Object[] { .4, "Invoice Info Updated" };
  }

  public void updateTotals() {
    //Update the totals
    updateTotals(dao);
    state = new Object[] { .5, "Totals Updated" };
  }

  public void updateJobs() {
    //Add Job sections for each bill
    for (InvoiceEntry job : dao.getBills()) {
      addJob(job, job.isShowNotes());
    }
    state = new Object[] { .7, "Jobs Added" };
  }

  public void writeJobsToInvoice() {
    //Write the Job Sections to the Invoice
    writeJobs();
    state = new Object[] { .9, "Jobs Written" };
  }

  public void saveInvoice() {
    //Save the invoice to the download path
    invoice.saveToFile(
      downloadPath + "//" + dao.getInvoiceNumber() + ".pdf",
      FileFormat.PDF
    );
    state =
      new Object[] {
        1.0,
        "Invoice Saved to: " +
        downloadPath +
        "//" +
        dao.getInvoiceNumber() +
        ".pdf",
      };
  }

  /**
   * Write the Job Sections to the Invoice
   * Remove the current job section and add the new job sections
   */
  private void writeJobs() {
    invoice.getSections().removeAt(2);
    for (Section job : JobSections) {
      invoice.getSections().insert(2, job);
    }
  }

  /**
   * Update the customer information on the invoice
   *  Set the customer name, address and phone number
   * @param dao - CustomerDAO
   */
  private void updateCustomerInfo(CustomerDAO dao) {
    invoice.replace("$Customer Name", dao.getName(), true, true);
    invoice.replace("$CustomerAddress", dao.getAddress(), true, true);
    invoice.replace("$CustomerPhone", dao.getPhoneNumber(), true, true);
  }

  /**
   * Update the invoice information on the invoice
   * Set the invoice number, date and status
   * @param dao - InvoiceDAO
   */
  private void updateInvoiceInfo(InvoiceDAO dao) {
    invoice.replace("$InvoiceNo", dao.getInvoiceNumber(), true, true);
    invoice.replace("$Date", dao.getGeneratedDate(), true, true);
    invoice.replace("$Status", dao.getStatus(), true, true);
  }

  /**
   * Update the totals on the invoice
   * Set the labor total, delivery fee and total charge
   * @param dao - InvoiceDAO
   */
  private void updateTotals(InvoiceDAO dao) {
    //Load the labor charges in a 2d array with each array representing a row
    String[][] data = new String[][] {};
    for (InvoiceEntry bill : dao.getBills()) {
      if (
        bill.getBill().getLaborCharges() != null &&
        bill.getBill().getLaborCharges().size() > 0
      ) {
        for (LaborChargeDAO labor : bill.getBill().getLaborCharges()) {
          String[] row = new String[] {
            String.valueOf(labor.getRate()) + " /hr",
            String.valueOf(labor.getHours()),
            String.valueOf(labor.getCost()),
          };
          if (data.length == 0) {
            data = new String[][] { row };
          } else {
            data = addArray(data, row);
          }
        }
      }
    }
    //Update the size if the table to fit the data
    addRows(invoice.getSections().get(3).getTables().get(0), data.length - 1);
    //Set the data in the table
    fillTableWithData(invoice.getSections().get(3).getTables().get(0), data);
    //Set the labor total, delivery fee and total charge
    invoice
      .getSections()
      .get(3)
      .getTables()
      .get(0)
      .getRows()
      .get(data.length + 1)
      .getCells()
      .get(2)
      .getParagraphs()
      .get(0)
      .setText("$" + dao.getLaborTotal());
    invoice
      .getSections()
      .get(3)
      .getDocument()
      .replace("$DeliveryFee", "$" + dao.getDeliveryTotal(), true, true);
    invoice
      .getSections()
      .get(3)
      .getDocument()
      .replace("$Amount Due", "$" + dao.getChargeTotal(), true, true);
  }

  /**
   * Add Each Job to the Invoice
   * @param job - InvoiceEntry of the Job to add
   * @param showNotes - boolean to indicate if the notes should be added
   */
  private void addJob(InvoiceEntry job, boolean showNotes) {
    //Clone the Job Section to add to the array of sections to add
    Section newJob = JobSection.deepClone();
    //2d array to hold the part charges for the job
    //Each array represents a row in the table
    String[][] data = new String[][] {};
    // Fill in the Part Section
    if (job.getBill().getPartCharges() != null) {
      for (PartChargeDAO part : job.getBill().getPartCharges()) {
        String[] row = new String[] {
          part.getPartName(),
          part.getPartNumber(),
          String.valueOf(part.getQuantity()),
          String.valueOf(part.getCharge()),
          String.valueOf(part.getTotal()),
        };
        if (data.length == 0) {
          data = new String[][] { row };
        } else {
          data = addArray(data, row);
        }
      }
    }
    //Update the size of the table to fit the data
    addRows(newJob.getTables().get(1), data.length - 1);
    //Set the data in the table
    fillTableWithData(newJob.getTables().get(1), data);
    //Fill in the Part Total
    newJob
      .getTables()
      .get(1)
      .getRows()
      .get(data.length + 1)
      .getCells()
      .get(4)
      .getParagraphs()
      .get(0)
      .setText("$" + job.getBill().getPartsCost());
    //---------- Fill in Job Details ---------//
    //Fill in the Job Name
    newJob
      .getTables()
      .get(0)
      .getRows()
      .get(0)
      .getCells()
      .get(1)
      .getParagraphs()
      .get(0)
      .replace("$JobName", job.getJobName(), true, true);
    //Fill in the Equipment Name
    newJob
      .getTables()
      .get(0)
      .getRows()
      .get(1)
      .getCells()
      .get(1)
      .getParagraphs()
      .get(0)
      .replace("$EquipmentName", job.getEquipmentName(), true, true);
    //Fill in the Notes if they are to be shown
    newJob
      .getTables()
      .get(0)
      .getRows()
      .get(2)
      .getCells()
      .get(1)
      .getParagraphs()
      .get(0)
      .replace("$JobNotes", showNotes ? job.getNotes() : "", true, true);

    //Add the Job Section to the array of sections to add
    JobSections.add(newJob);
  }

  /**
   * Add a row to a 2d array
   * @param data - The origional Array 2d String array
   * @param row - The row to add to the array
   * @return - The Updated array
   */
  private String[][] addArray(String[][] data, String[] row) {
    String[][] newData = new String[data.length + 1][];
    for (int i = 0; i < data.length; i++) {
      newData[i] = data[i];
    }
    newData[data.length] = row;
    return newData;
  }

  /**
   * Add Rows to a table
   * @param table - Table to be updated
   * @param rowNumber - Number of rows to add
   */
  public void addRows(Table table, int rowNumber) {
    for (int i = 0; i < rowNumber; i++) {
      table.getRows().insert(1 + i, table.getRows().get(1).deepClone());
    }
  }

  /**
   * Fill a table with data
   * @param table - Table to be updated
   * @param data - 2d array of data to be added
   */
  public void fillTableWithData(Table table, String[][] data) {
    for (int row = 0; row < data.length; row++) {
      for (int col = 0; col < data[row].length; col++) {
        table
          .getRows()
          .get(row + 1)
          .getCells()
          .get(col)
          .getParagraphs()
          .get(0)
          .setText(data[row][col]);
      }
    }
  }
}
