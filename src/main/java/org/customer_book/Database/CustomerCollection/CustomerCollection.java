package org.customer_book.Database.CustomerCollection;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;
import org.bson.types.ObjectId;

public class CustomerCollection {

  MongoCollection<CustomerDAO> collection;

  public CustomerCollection(MongoDatabase database) {
    collection = database.getCollection("Customers", CustomerDAO.class);
  }

  public void printAll() {
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public ArrayList<CustomerDAO> getCustomers(int size, int skip) {
    ArrayList<CustomerDAO> customers = new ArrayList<>();
    collection
      .find()
      .sort(descending("name"))
      .limit(size)
      .skip(skip)
      .forEach(c -> {
        customers.add(c);
      });
    return customers;
  }

  public void updateCustomer(CustomerDAO customer) {
    System.out.println(
      collection.findOneAndReplace(all("_id", customer.getId()), customer)
    );
  }

  public CustomerDAO findCustomerById(ObjectId id) {
    return collection.find(all("_id", id)).first();
  }

  public void addJobToCustomer(ObjectId id, ObjectId jobId) {
    collection.findOneAndUpdate(eq("_id", id), Updates.push("jobIDs", jobId));
  }
}
