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

  /**
   * printAll:
   * This function prints all of the equipment DAOs in the database
   */
  public void printAll() {
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  /**
   * getEquipmentList:
   * This function returns a list of equipment DAOs from the database
   * The list is sorted by model number and is limited to the size and skip
   * @param size - the number of DAOs to be returned
   * @param skip - the number of DAOs to be skipped
   * @return - the list of DAOs
   */
  public ArrayList<EquipmentDAO> getEquipmentList(int size, int skip) {
   ArrayList<EquipmentDAO> equipmentList = new ArrayList<>();
    collection
      .find()
      .sort(descending("modelNumber"))
      .limit(size)
      .skip(skip)
      .into(equipmentList);
    return equipmentList;
  }

  /**
   * getEquipment:
   * This function returns the DAO of an equipment from the database
   * @param id - the ObjectId of the equipment to be returned
   * @return - the DAO of the equipment
   */
  public EquipmentDAO getEquipment(ObjectId id) {
    return collection.find().filter(eq("_id", id)).first();
  }
  /**
   * getEquipmentID:
   * This function returns the ObjectId of an equipment from the database
   * based on the model number
   * @param modelNumber - the model number of the equipment
   * @return
   */
  public ObjectId getEquipmentID(String modelNumber) {
    return collection.find(eq("modelNumber", modelNumber)).first().getId();
  }

  /**
   * getEquipmentList:
   * This function returns a list of equipment DAOs from the database
   * This list contains the DAO's for all of the ObjectIds in the ids list
   * @param ids
   * @return
   */
  public ArrayList<EquipmentDAO> getEquipmentList(ArrayList<ObjectId> ids) {
    return collection.find(in("_id", ids)).into(new ArrayList<>());
  }
  /**
   * updateEquipment:
   * This function updates the DAO of an equipment in the database
   * by replacing the old DAO with the new one
   * @param equipment - the DAO of the equipment to be updated
   */
  public void updateEquipment(EquipmentDAO equipment) {
    collection.replaceOne(eq("_id", equipment.getId()), equipment);
  }
  /**
   * modelNumberExists:
   * This function checks if a model number exists in the database and returns
   * true if it does
   * @param modelNumber - the number to be checked
   * @return - true if the model number exists
   */
  public boolean modelNumberExists(String modelNumber) {
    return collection.countDocuments(eq("modelNumber", modelNumber)) > 0;
  }
  /**
   * addEquipment:
   * This function adds a new equipment to the database
   * @param equipment - The DAO of the equipment to be added
   */
  public void addEquipment(EquipmentDAO equipment) {
    collection.insertOne(equipment);
  }

  /**
   * getAllEquipmentList:
   * this function returns a list of all the equipment model numbers in the database as an ArrayList
   */
  public ArrayList<String> getAllEquipmentList(){
    ArrayList<String> equipmentList = new ArrayList<>();
    collection
      .find()
      .forEach(e -> {
        equipmentList.add(e.getModelNumber());
      });
    return equipmentList;
    
  }



}
