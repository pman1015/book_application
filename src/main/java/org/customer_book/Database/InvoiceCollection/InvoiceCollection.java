package org.customer_book.Database.InvoiceCollection;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class InvoiceCollection {

  MongoCollection<InvoiceDAO> collection;

  public InvoiceCollection(MongoDatabase database) {
    collection = database.getCollection("Invoices", InvoiceDAO.class);
  }

  public ArrayList<InvoiceDAO> getInvoices(int size, int skip) {
    ArrayList<InvoiceDAO> invoices = new ArrayList<>();
    collection
      .find()
      .limit(size)
      .skip(skip)
      .forEach(c -> {
        invoices.add(c);
      });
    return invoices;
  }

  public ObjectId addInvoice(InvoiceDAO newInvoice) {
    return collection
      .insertOne(newInvoice)
      .getInsertedId()
      .asObjectId()
      .getValue();
  }

  public ArrayList<InvoiceDAO> getAllInvoices() {
    ArrayList<InvoiceDAO> invoices = new ArrayList<>();
    collection
      .find()
      .forEach(c -> {
        invoices.add(c);
      });
    return invoices;
  }

  public void updateInvoice(InvoiceDAO invoiceDAO) {
    collection.findOneAndReplace(eq("_id", invoiceDAO.getId()), invoiceDAO);
  }

  public void deleteInvoice(InvoiceDAO invoiceDAO) {
    collection.findOneAndDelete(eq("_id", invoiceDAO.getId()));
  }

  public void updateEntryOnInvoice(InvoiceDAO invoiceDAO, InvoiceEntry entry) {
    invoiceDAO.updateEntry(entry);
    updateInvoice(invoiceDAO);
  }

  public ArrayList<InvoiceDAO> getCompletedInvoices(
    Bson filter,
    int amount,
    int skip
  ) {
    ArrayList<InvoiceDAO> invoices = new ArrayList<>();
    collection
      .find(filter)
      .sort(ascending("_id"))
      .limit(amount)
      .skip(skip)
      .into(invoices);
    return invoices;
  }
}
