package org.customer_book.Database.InvoiceCollection;
import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
        return collection.insertOne(newInvoice).getInsertedId().asObjectId().getValue();
    }
    public ArrayList<InvoiceDAO> getAllInvoices() {
        ArrayList<InvoiceDAO> invoices = new ArrayList<>();
        collection.find().forEach(c -> {
            invoices.add(c);
        });
        return invoices;
    }
    
}
