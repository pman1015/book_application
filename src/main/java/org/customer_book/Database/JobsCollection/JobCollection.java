package org.customer_book.Database.JobsCollection;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import org.bson.types.ObjectId;

public class JobCollection {

  MongoCollection<JobDAO> collection;

  public JobCollection(MongoDatabase database) {
    collection = database.getCollection("Jobs", JobDAO.class);
  }

  public void printAll() {
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public ArrayList<JobDAO> getJobs(int size, int skip) {
    ArrayList<JobDAO> jobs = new ArrayList<>();
    collection
      .find()
      .limit(size)
      .skip(skip)
      .forEach(c -> {
        jobs.add(c);
      });
    return jobs;
  }

  public ArrayList<JobDAO> getDAOs(ArrayList<ObjectId> jobIds) {
    return collection.find(in("_id", jobIds)).into(new ArrayList<>());
  }

public ObjectId addJob(JobDAO newJob) {
   return collection
     .insertOne(newJob)
     .getInsertedId()
     .asObjectId()
     .getValue();
}
}
