package org.customer_book.Database.MachinesCollection;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.EquipmentCollection.EquipmentDAO;

public class MachineCollection {

  MongoCollection<MachineDAO> collection;

  public MachineCollection(MongoDatabase database) {
    collection = database.getCollection("Machines", MachineDAO.class);
  }

  public void printAll() {
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public ObjectId addMachine(MachineDAO newMachine) {
    return collection
      .insertOne(newMachine)
      .getInsertedId()
      .asObjectId()
      .getValue();
  }

  public ObjectId getEquipmentIdbyMachineId(ObjectId machineId) {
    MachineDAO machine = collection.find(eq("_id", machineId)).first();
    return machine.getEquipmentId();
  }

  public MachineDAO getMachine(String customerName, String equipmentId) {
    return collection
      .find()
      .filter(eq("customerName", customerName))
      .filter(eq("equipmentId", equipmentId))
      .first();
  }
  public MachineDAO getCustomerMachine(String customerName, String equipmentName){
    EquipmentDAO eq = DatabaseConnection.equipmentCollection.findByName(equipmentName);
    if(eq == null) return null;
    return collection.find(and(eq("customerName", customerName), eq("equipmentId", eq.getId()))).first();
  }

  public ArrayList<MachineDAO> getMachinesbyIDs(
    ArrayList<ObjectId> machineIDs
  ) {
    ArrayList<MachineDAO> machines = new ArrayList<>();
    collection
      .find(in("_id", machineIDs))
      .forEach(m -> {
        machines.add(m);
      });
    return machines;
  }

  public MachineDAO getMachineDAObyId(ObjectId machineId) {
    return collection.find(eq("_id", machineId)).first();
  }
  public void updateMachineNotes(MachineDAO machineDAO) {
    collection.updateOne(
      eq("_id", machineDAO.getId()),
      new Document("$set", new Document("notes", machineDAO.getNotes()))
    );
  }
  public ArrayList<ObjectId> getEquipmentByMachines(ArrayList<ObjectId> machineIds){
    ArrayList<ObjectId> equipment = new ArrayList<>();

    collection.find(in("_id", machineIds)).forEach(m -> {
      equipment.add(m.getEquipmentId());
    });
    return equipment;
  }

  public void updateMachine(MachineDAO machine) {
    collection.updateOne(
      eq("_id", machine.getId()),
      new Document("$set", machine)
    );
  }
}
