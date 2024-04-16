package org.customer_book.Database.MachinesCollection;

import org.bson.types.ObjectId;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class MachineWorkDAO {

    private ObjectId id;
    private Date jobDate;
    private ObjectId jobId;
    private String partNumber;
    private String partName;

    
}
