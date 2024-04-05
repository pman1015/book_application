package org.customer_book.Pages.BillsPage;

import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.JobsCollection.JobDAO;

@Getter
@Setter
@NoArgsConstructor
public class BillJobCardModel {

  private ObjectProperty<JobDAO> toRemove;
  private JobDAO jobDAO;

  private StringProperty JobNameProperty = new SimpleStringProperty("");
  private StringProperty EquipmentNameProperty = new SimpleStringProperty("");
  private StringProperty PartsCostTotalProperty = new SimpleStringProperty("");
  private StringProperty LaborCostTotalProperty = new SimpleStringProperty("");
  private StringProperty JobCostProperty = new SimpleStringProperty("");
  private StringProperty DeliveryFeeProperty = new SimpleStringProperty("");

  private ObservableList<Parent> PartsUsedList = FXCollections.observableArrayList();

  public void setJobDAO(JobDAO jobDAO, ObjectProperty<JobDAO> toRemove) {
    this.jobDAO = jobDAO;
    this.toRemove = toRemove;

    JobNameProperty.set(jobDAO.getJobName());
    EquipmentNameProperty.set(jobDAO.getEquipmentName());
    PartsCostTotalProperty.set("$" + jobDAO.getBill().getPartsCost());
    LaborCostTotalProperty.set("$" + jobDAO.getBill().getLaborCost());
    DeliveryFeeProperty.set("$" + jobDAO.getBill().getDeliveryCost());
    JobCostProperty.set("$" + jobDAO.getBill().getBillTotal());
    loadParts();
  }

  private void loadParts() {
    if (
      jobDAO == null ||
      jobDAO.getBill() == null ||
      jobDAO.getBill().getPartCharges() == null
    ) return;
    PartsUsedList.clear();
    jobDAO
      .getBill()
      .getPartCharges()
      .forEach(partChargeDAO -> {
        try {
          FXMLLoader loader = App.getLoader("BillsPage", "JobInBillPartCard");
          Parent card = loader.load();
          ((BillJobPartCardView) loader.getController()).setPartChargeDAO(
              partChargeDAO
            );
          PartsUsedList.add(card);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
  }
}
