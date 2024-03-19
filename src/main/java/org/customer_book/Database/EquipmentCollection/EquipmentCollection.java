package org.customer_book.Database.EquipmentCollection;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Sorts.descending;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import org.bson.types.ObjectId;

public class EquipmentCollection {

  MongoCollection<EquipmentDAO> collection;

  public EquipmentCollection(MongoDatabase database) {
    collection = database.getCollection("Equipment", EquipmentDAO.class);
  }

  public void printAll() {
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public void getEquipmentList(int size, int skip) {
    collection
      .find()
      .sort(descending("modelNumber"))
      .limit(size)
      .skip(skip)
      .forEach(c -> {
        System.out.println(c);
      });
  }

  public EquipmentDAO getEquipment(ObjectId id) {
    return collection.find().filter(eq("_id", id)).first();
  }

  public ArrayList<EquipmentDAO> getEquipmentList(ArrayList<ObjectId> ids) {
    return collection.find(in("_id", ids)).into(new ArrayList<>());
  }
}
