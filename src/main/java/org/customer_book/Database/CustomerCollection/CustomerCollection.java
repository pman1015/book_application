package org.customer_book.Database.CustomerCollection;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Sorts.descending;

import java.util.ArrayList;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CustomerCollection {
    MongoCollection<CustomerDAO> collection;

    public CustomerCollection(MongoDatabase database){
        collection = database.getCollection("Customers", CustomerDAO.class);
    }
    public void printAll(){
        collection.find().forEach(d -> {
            System.out.println(d.toString());
        });
    }
    public ArrayList<CustomerDAO> getCustomers(int size, int skip){
        ArrayList<CustomerDAO> customers = new ArrayList<>();
        collection.find().sort(descending("name")).limit(size).skip(skip).forEach(c -> {
            customers.add(c);
        });
        return customers;
    }
    public void updateCustomer(CustomerDAO customer){
        System.out.println(collection.findOneAndReplace(all("_id",customer.getId()), customer));
    }
}
