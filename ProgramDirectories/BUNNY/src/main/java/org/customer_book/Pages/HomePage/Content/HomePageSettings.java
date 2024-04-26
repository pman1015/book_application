package org.customer_book.Pages.HomePage.Content;

import java.io.File;
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
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Pages.HomePage.Content.Panes.Image.ImageModel;
import org.customer_book.Utilities.HomePageConfig.HomePageConfigPermenant;
import org.customer_book.Utilities.HomePageConfig.PaneConfig;

@Getter
@Setter
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
  //------------------------------------------------------------------//
  //-------------------------- Constructor ---------------------------//
  //------------------------------------------------------------------//
  public HomePageSettings() {
    layoutName.addListener((observable, oldValue, newValue) -> {
      if(newValue != null){
        System.out.println("Layout changed to: " + newValue);
        loadImage(newValue);
      }
    });
  }
  /**
   * loads a preview image of the layout
   * or clears the image if no image is found
   */
  public void loadImage(String layout){
    try{
      layoutImage.set(new Image(App.class.getResourceAsStream("layoutImages/"+layout+".jpg")));

    }catch(Exception e){
      System.out.println("Error loading image: "+layout);
      layoutImage.set(null);
    }
  }
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
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Image");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    File selectedFile = fileChooser.showOpenDialog(App.getStage());
    if(selectedFile != null){
      System.out.println("Selected File: "+selectedFile.getAbsolutePath());
      updateSelectedPaneConfig(selectedFile);
    }
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
  private void updateSelectedPaneConfig(File image){
   PaneConfig paneToUpdate = App.homePageConfigUtil.getPaneConfigs().get(selectedPane);
    paneToUpdate.setImageFile(image);
    paneToUpdate.setPaneClassName(new ImageModel().getClass().toString());
    App.homePageConfigUtil.savePaneConfig(paneToUpdate, selectedPane);
  }
}
