package org.customer_book.Pages.HomePage;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Toggle;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import org.customer_book.App;
import org.customer_book.Pages.HomePage.Content.HomePageSettings;
import org.customer_book.Utilities.HomePageConfig.HomePageConfigPermenant;
import org.customer_book.Utilities.HomePageConfig.HomePageConfigPermenant.layoutConfig;

@Getter
@Setter
public class HomePageModel {

  //-------------------------- View Properties ----------------------//
  private BooleanProperty showSettings = new SimpleBooleanProperty(false);
  private ObservableList<Parent> homePageContent = FXCollections.observableArrayList();
  
  //-------------------------- Model Properties ----------------------//
  private HomePageSettings homePageSettings = new HomePageSettings();

  //-------------------------- Constructor ----------------------//
  public HomePageModel() {
    //Load the HomePageConfig
    homePageSettings.loadSettings();
    Platform.runLater(() -> {
      loadLayout();
    });
  }

  //-------------------------- Model Methods ----------------------//
  public void saveSettings() {
    homePageSettings.saveSettings();
    loadLayout();
  }

  public void loadLayout() {

    //Load the HomePageContent
    homePageContent.clear();
    layoutConfig currentLayout = App.homePageConfigUtil.getHomePageConfig().getLayoutConfig();
    ArrayList<String> panes = App.homePageConfigUtil.getHomePageConfig().getSelections();
    for(int i = 0; i < panes.size(); i++) {
      String selection = panes.get(i);
      System.out.println("Loading content: " + selection);
      if (selection != null) {
        try {
          FXMLLoader contentLoader = App.getLoader("HomePage/PaneContent/"+selection, selection);
          Parent content = contentLoader.load();

          homePageContent.add(
            (Parent) currentLayout.applyOffset(content, i)
          );
        } catch (Exception e) {
          System.out.println("Error loading content: " + selection);
        }
      }
    }
    
    
  }

  //-------------------------- View Methods ----------------------//
  //Toggle the settings visibility
  public void hideSettings() {
    showSettings.set(false);
  }

  public void showSettings() {
    showSettings.set(true);
  }
 
}
