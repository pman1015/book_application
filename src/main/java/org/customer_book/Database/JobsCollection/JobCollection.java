package org.customer_book.Database.JobsCollection;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import javafx.scene.Parent;
import org.bson.conversions.Bson;
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
    return collection.insertOne(newJob).getInsertedId().asObjectId().getValue();
  }

  public JobDAO getDAO(ObjectId jobID) {
    return collection.find(eq("_id", jobID)).first();
  }

  public void updateJob(JobDAO job) {
    collection.findOneAndReplace(eq("_id", job.getId()), job);
  }

  public void deleteJob(JobDAO job) {
    collection.deleteOne(eq("_id", job.getId()));
  }

  public ArrayList<JobDAO> getCompletedJobs(Bson filter, int amount, int skip) {
    ArrayList<JobDAO> jobs = new ArrayList<>();
    collection
      .find(and(ne("endDate", "n/a"), filter))
      .sort(ascending("endDate"))
      .limit(amount)
      .skip(skip)
      .into(jobs);
    return jobs;
  }
}
