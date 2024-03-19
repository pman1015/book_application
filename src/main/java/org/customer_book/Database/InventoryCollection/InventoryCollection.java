package org.customer_book.Database.InventoryCollection;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class InventoryCollection {
    MongoCollection<PartDAO> collection;

    public InventoryCollection(MongoDatabase database){
        collection = database.getCollection("Inventory", PartDAO.class);
    }

    public void printAll(){
        collection.find().forEach(d -> {
            System.out.println(d.toString());
        });
    }
    
}
