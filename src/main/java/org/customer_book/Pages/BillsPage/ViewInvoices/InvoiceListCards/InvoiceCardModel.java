package org.customer_book.Pages.BillsPage.ViewInvoices.InvoiceListCards;

import java.io.IOException;
import java.util.ArrayList;
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
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceCardModel {

  //------------- Invoice Card Properties ---------------//
  private StringProperty customerName = new SimpleStringProperty("");
  private StringProperty completedDate = new SimpleStringProperty("");
  private ObservableList<Parent> equipmentList = FXCollections.observableArrayList();

  //------------- DAO Properties ---------------//
  private InvoiceDAO invoiceDAO;
  private ObjectProperty<InvoiceDAO> selectedInvoice;

  public void setInvoice(
    InvoiceDAO invoiceDAO,
    ObjectProperty<InvoiceDAO> selectedInvoice
  ) {
    this.invoiceDAO = invoiceDAO;
    this.selectedInvoice = selectedInvoice;
    this.customerName.set(invoiceDAO.getCustomerName());
    this.completedDate.set(invoiceDAO.getGeneratedDate());
    loadEquipment(invoiceDAO.getEquipment());
  }

  private void loadEquipment(ArrayList<String> equipment) {
    equipmentList.clear();
    for (String name : equipment) {
      try {
        FXMLLoader loader = App.getLoader(
          "BillsPage",
          "InvoiceCardEquipmentCard"
        );
        Parent card = loader.load();
        ((InvoiceCardEquipment) loader.getController()).setEquipmentName(name);
        equipmentList.add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void selectInvoice() {
    selectedInvoice.set(invoiceDAO);
  }
}
