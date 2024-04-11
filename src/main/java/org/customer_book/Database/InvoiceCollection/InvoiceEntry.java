package org.customer_book.Database.InvoiceCollection;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.JobsCollection.BillDAO;

import javafx.scene.chart.PieChart.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class InvoiceEntry {
    private ObjectId EquipmentId;
    private ObjectId JobId;
    private String Notes;
    private String EquipmentName;
    private BillDAO bill;
    private boolean showNotes;

    @BsonIgnore
    public String getJobName() {
       return DatabaseConnection.jobCollection.getDAO(JobId).getJobName();
    }
    @BsonIgnore
    public String getJobTotal() {
       return String.valueOf(bill.getPartsCost());
    }
    @BsonIgnore
    public String getStartDate() {
      return DatabaseConnection.jobCollection.getDAO(JobId).getStartDate();
    }
    
}
