package org.customer_book.Pages.HomePage.Content;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Utilities.HomePageConfig.HomePageConfigPermenant;

@Getter
@Setter
@NoArgsConstructor
public class HomePageSettings {

  //------------------------------------------------------------------//
  //-------------------------- View Properties ----------------------//
  //------------------------------------------------------------------//
  //Layout Settings
  private StringProperty layoutName = new SimpleStringProperty("");
  private ObservableList<String> layoutOptions = FXCollections.observableArrayList();
  private ObjectProperty<Image> layoutImage = new SimpleObjectProperty<Image>();

  //Toggle selections
  private int selectedPane = 0;

  //Content for selectedPane
  private StringProperty selectedPaneContent = new SimpleStringProperty("");
  private ObservableList<String> paneContentOptions = FXCollections.observableArrayList();

  //Show the selectImageButton
  private BooleanProperty showSelectImageButton = new SimpleBooleanProperty(
    false
  );

  //Database Connection String
  private StringProperty databaseConnectionString = new SimpleStringProperty(
    ""
  );

  //Username
  private StringProperty username = new SimpleStringProperty("");

  //------------------------------------------------------------------//
  //-------------------------- Model Properties ----------------------//
  //------------------------------------------------------------------//
  private HomePageConfigPermenant homePageConfig;

  //TODO: add the images for the layouts

  /**
   * loadSettings:
   *  - Load the settings from the HomePageConfig from the static HomePageConfigUtil
   *  - It then initalises the settings properties with the loaded settings
   */
  public void loadSettings() {
    homePageConfig = App.homePageConfigUtil.getHomePageConfig();
    //----------------------------- Initalise the view properties ----------------------//
    //Set the layout options
    layoutOptions.addAll(homePageConfig.getLayoutOptions());
    layoutName.set(homePageConfig.getSelectedLayout());
    //TODO: Set the layout image

    //Set the pane content options
    paneContentOptions.addAll(homePageConfig.getPaneContentOptionsList());
    selectedPaneContent.set(homePageConfig.getPaneContent(selectedPane));
    selectedPaneContent.addListener((observable, oldValue, newValue) -> {
      if (selectedPane != -1) {
        updatePaneContent(selectedPane, newValue);
        if (newValue !=null && newValue.equals("Image")) {
          showSelectImageButton.set(true);
        } else {
          showSelectImageButton.set(false);
        }
      }
    });

    //Set the database connection string
    databaseConnectionString.set(homePageConfig.getDatabaseConnectionString());

    //Set the username
    username.set(homePageConfig.getUsername());
  }

  //--------------------------------------------------------------//
  //-------------------------- View Methods ----------------------//
  //--------------------------------------------------------------//
  //Save the settings
  public void saveSettings() {
    homePageConfig.setSelectedLayout(layoutName.get());
    homePageConfig.setDatabaseConnectionString(databaseConnectionString.get());
    homePageConfig.setUsername(username.get());

    //Update the pane content for the selected pane
    if (selectedPane != -1) {
      updatePaneContent(selectedPane, selectedPaneContent.get());
    }
    App.homePageConfigUtil.setHomePageConfig(homePageConfig);
  }

  //Show the selectImage dialog
  public void showSelectImage() {
    //TODO: Show the select image dialog
  }

  public void selectPanel(Toggle selection) {
    if (selection.getClass().equals(RadioButton.class)) {
      RadioButton input = (RadioButton) selection;
      selectedPane = Integer.parseInt(input.getText()) - 1;
      selectedPaneContent.set(homePageConfig.getPaneContent(selectedPane));
    } else {
      System.out.println("Error: Selection is not a radio button");
    }
  }

  //--------------------------------------------------------------//
  //-------------------------- Model Methods ---------------------//
  //--------------------------------------------------------------//
  //Update the pane content for the selected pane
  private void updatePaneContent(int paneNumber, String content) {
    homePageConfig.updatePaneConetent(paneNumber, content);
  }
  public String getSelectedLayout(){
    return homePageConfig.getSelectedLayout();
  }
}
