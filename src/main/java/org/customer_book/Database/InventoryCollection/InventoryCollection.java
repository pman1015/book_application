package org.customer_book.Database.InventoryCollection;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;
import org.bson.types.ObjectId;

public class InventoryCollection {

  MongoCollection<PartDAO> collection;

  public InventoryCollection(MongoDatabase database) {
    collection = database.getCollection("Inventory", PartDAO.class);
  }

  public void printAll() {
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public ArrayList<PartDAO> getParts(int size, int skip) {
    ArrayList<PartDAO> parts = new ArrayList<>();
    collection.find().limit(size).skip(skip).into(parts);
    return parts;
  }

  public ArrayList<PartDAO> getSelectParts(ArrayList<ObjectId> partIDs) {
    ArrayList<PartDAO> parts = new ArrayList<>();
    collection.find(in("_id", partIDs)).into(parts);
    return parts;
  }

  public int getPartsCount() {
    return (int) collection.countDocuments();
  }

  public boolean partNumberExists(String partNumber) {
    return (
      collection.find().filter(eq("partNumber", partNumber)).first() != null
    );
  }

  public boolean partNameExists(String partName) {
    return collection.find().filter(eq("partName", partName)).first() != null;
  }

  public void addPart(PartDAO part) {
    collection.insertOne(part);
  }

  public PartDAO getPartByID(ObjectId id) {
    return collection.find(eq("_id", id)).first();
  }

  public void updatePart(PartDAO part) {
    collection.replaceOne(eq("_id", part.getId()), part);
  }

  public void removeCompatibleEquipment(ObjectId partID, ObjectId equipmentID) {
    collection.findOneAndUpdate(
      eq("_id", partID),
      Updates.pull("compatibleEquipment", equipmentID)
    );
  }
  public void addCompatibleEquipment(ObjectId partID, ObjectId equipmentID) {
    collection.findOneAndUpdate(
      eq("_id", partID),
      Updates.push("compatibleEquipment", equipmentID)
    );
  }

  public ArrayList<PartDAO> getNamesList(ArrayList<ObjectId> existingIDs) {
    ArrayList<PartDAO> parts = new ArrayList<>();
    collection.find(Filters.nin("_id", existingIDs)).into(parts);
    return parts;
  }
}
