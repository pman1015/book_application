package org.customer_book.Database.ReportsCollection;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ReportsCollection {
    MongoCollection<ReportDAO> collection;

    public ReportsCollection(MongoDatabase database){
        collection = database.getCollection("Reports", ReportDAO.class);
    }
    public void addNewReport(ReportDAO report){
        collection.insertOne(report);
    }
    public void updateReport(ReportDAO report){
        collection.replaceOne(new Document("_id", report.getId()), report);
    }
    public ReportDAO getReport(String name){
        return collection.find(new Document("reportName", name)).first();
    }
}
