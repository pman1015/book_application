package org.customer_book.Popups.ExpenseCategoryCreate;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Database.ReportsCollection.ReportDAO;

@Getter
@Setter
@ToString
public class ExpenseCategoryCreateModel {

  private StringProperty categoryNameProperty = new SimpleStringProperty("");
  private StringProperty categoryNameErrorProperty = new SimpleStringProperty(
    ""
  );
  private ReportDAO report;

  public ExpenseCategoryCreateModel() {
    report =
      DatabaseConnection.reportsCollection.getReport("ExpenseCategories");
    //If there is no report for ExpenseCategories, create one
    if (report == null) {
      report = new ReportDAO();
      report.setReportName("ExpenseCategories");
      report.setValues(new ArrayList<String>());
      DatabaseConnection.reportsCollection.addNewReport(report);
    }
  }

  public boolean validateCategoryName() {
    boolean valid = true;
    if (categoryNameProperty.get().isEmpty()) {
      valid = false;
      categoryNameErrorProperty.set("Category Name cannot be empty");
    } else {
      if (report.getValues().contains(categoryNameProperty.get())) {
        valid = false;
        categoryNameErrorProperty.set("Category Name already exists");
      } else {
        categoryNameErrorProperty.set("");
      }
    }
    return valid;
  }

  public void saveChanges() {
    if (validateCategoryName()) {
      report.getValues().add(categoryNameProperty.get());
      DatabaseConnection.reportsCollection.updateReport(report);
      App.removePopup();
    }
  }
}
