package org.customer_book.Database.CustomerCollection;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.ascending;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.customer_book.Database.JobsCollection.JobDAO;

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

  public ArrayList<CustomerDAO> getFilteredCustomers(Bson filter, int size, int skip){
    ArrayList<CustomerDAO> customers = new ArrayList<>();
    collection.find(filter).sort(ascending("_id")).limit(size).skip(skip).into(customers);
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

  public void removeJob(JobDAO job) {
    collection.updateMany(
      in("jobIDs", job.getId()),
      Updates.pull("jobIDs", job.getId())
    );
  }

  public ArrayList<String> getAllNames() {
    ArrayList<String> names = new ArrayList<>();
    collection
      .find()
      .forEach(c -> {
        names.add(c.getName());
      });
    return names;
  }

  public CustomerDAO findByName(String customerName) {
    return collection.find(eq("name", customerName)).first();
  }

  public void insertCustomer(CustomerDAO customerDAO) {
    collection.insertOne(customerDAO);
  }
  /**
   * get a list of the names and nickNames of all customers
   * @return An array of two arraylists, the first containing the names of the customers and the second containing the nicknames of the customers
   */
  public Object[] getNamesandNickNames(){
    ArrayList<String> customerNames = new ArrayList<>();
    ArrayList<String> customerNickNames = new ArrayList<>();
    collection.find().forEach(c -> {
      String name = c.getName();
      String nickString = c.getAlias();
      if(name != null && !name.equals("")){
        customerNames.add(name);
      }
      if(nickString != null && !nickString.equals("")){
        customerNickNames.add(nickString);
      }
    });
    return new Object[]{customerNames, customerNickNames};
  }
}
