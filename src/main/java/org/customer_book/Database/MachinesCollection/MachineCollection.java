package org.customer_book.Database.MachinesCollection;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MachineCollection {
    MongoCollection<MachineDAO> collection;
    public MachineCollection(MongoDatabase database){
        collection = database.getCollection("Machines", MachineDAO.class);
    }
    public void printAll(){
        collection.find().forEach(d -> {
            System.out.println(d.toString());
        });
    }
}
