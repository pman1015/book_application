package org.customer_book.Database.ReportsCollection;

import java.util.ArrayList;
import java.util.HashMap;

import org.bson.types.ObjectId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReportDAO {
    private ObjectId id;
    private ArrayList<String> values;
    private String reportName;
    private HashMap <String, String> reportData;

    public void addNewValue(String key, String value) {
        if(reportData == null) {
            reportData = new HashMap<>();
        }
        reportData.put(key, value);
    }
}
