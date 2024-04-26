package org.customer_book.Pages.PageComponents.NavBar;

import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.customer_book.App;

public class NavBarModel {

  private ObservableList<Parent> navButtons;
  private ArrayList<String> buttonNames;

  public NavBarModel() throws IOException {
    navButtons = FXCollections.observableArrayList();
    buttonNames = new ArrayList<String>();
    buttonNames.add("Home");
    buttonNames.add("Customers");
    buttonNames.add("Jobs");
    buttonNames.add("Inventory");
    buttonNames.add("Equipment");
    buttonNames.add("Bills");

    for (String name : buttonNames) {
      FXMLLoader buttonLoader = App.getLoader("PageComponents", "NavButton");
      Parent button = buttonLoader.load();
      ((NavButtonController) buttonLoader.getController()).setButtonText(name);

      navButtons.add(button);
    }
  }

  public ObservableList<Parent> getNavButtons() {
    return navButtons;
  }
}
