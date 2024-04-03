package org.customer_book.Pages.JobDetailsPage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.Database.JobsCollection.LaborChargeDAO;
import org.customer_book.Database.JobsCollection.PartChargeDAO;

@Getter
@Setter
@NoArgsConstructor
public class ChargeCardModel {

  private StringProperty firstLabelProperty = new SimpleStringProperty("");
  private StringProperty secondLabelProperty = new SimpleStringProperty("");
  private StringProperty thirdLabelProperty = new SimpleStringProperty("");

  private LaborChargeDAO laborCharge;
  private PartChargeDAO partChargeDAO;

  private ObjectProperty<LaborChargeDAO> selectedLaborCharge;
  private ObjectProperty<PartChargeDAO> selectedPartCharge;

  public void setLaborCharge(
    LaborChargeDAO laborCharge,
    ObjectProperty<LaborChargeDAO> selectedLaborCharge
  ) {
    this.selectedLaborCharge = selectedLaborCharge;
    this.laborCharge = laborCharge;
    firstLabelProperty.setValue(laborCharge.getName());
    secondLabelProperty.setValue(
      laborCharge.getHours() + "hrs at :$" + laborCharge.getRate() + "/hr"
    );
    thirdLabelProperty.setValue("$" + String.format("%.2f", laborCharge.getCost()));
  }

  public void setPartCharge(
    PartChargeDAO partCharge,
    ObjectProperty<PartChargeDAO> selectedPartCharge
  ) {
    this.selectedPartCharge = selectedPartCharge;
    this.partChargeDAO = partCharge;
    firstLabelProperty.setValue(partCharge.getPartName());
    secondLabelProperty.setValue(
      partCharge.getQuantity() + " at :$" + partCharge.getCharge()
    );
    thirdLabelProperty.setValue("$" + String.format("%.2f", partCharge.getTotal()));
  }

  public void updateCard() {
    if (laborCharge != null) {
      selectedLaborCharge.setValue(laborCharge);
    } else if (partChargeDAO != null) {
      selectedPartCharge.setValue(partChargeDAO);
    }
  }
}
