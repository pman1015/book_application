package org.customer_book.Pages.PageComponents.NavBar;

import java.io.IOException;
import org.customer_book.App;

public class NavButtonModel {

  public NavButtonModel() {
  }

  public void navigate(String pageName) throws IOException {
    switch (pageName) {
    case "Home":
      // navigate to home page
      App.setPage("HomePage", "HomePageBaseLayout");
      break;
    case "Customers":
      // navigate to customers page
      App.setPage("CustomerListPage", "CustomerListPage");
      break;
    case "Jobs":
      // navigate to jobs page
      App.setPage("JobsPage", "JobsPage");
      break;
    case "Inventory":
      // navigate to inventory page
      App.setPage("InventoryPage", "InventoryPage");
      break;
    case "Equipment":
      // navigate to equipment page
      App.setPage("EquipmentPage", "EquipmentPage");
      break;
    case "Bills":
      // navigate to bills page
      App.setPage("BillsPage", "BillsPage");
      break;
    }
  }
}
