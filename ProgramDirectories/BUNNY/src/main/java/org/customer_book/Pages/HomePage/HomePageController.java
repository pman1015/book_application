package org.customer_book.Pages.HomePage;

import java.util.concurrent.CompletableFuture;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.beans.value.ChangeListener;

public class HomePageController {

  @FXML
  private ImageView LayoutImage;

  @FXML
  private TextField DatabaseLocationField;

  @FXML
  private ComboBox<String> HomePageLayoutOptions;

  @FXML
  private TextField UsernameField;

  @FXML
  private Button SelectImage;

  @FXML
  private AnchorPane MainHomePage;

  @FXML
  private AnchorPane SettingsPopup;

  @FXML
  private ToggleGroup PaneSelect;

  @FXML
  private RadioButton Pane1;

  @FXML
  private RadioButton Pane2;

  @FXML
  private RadioButton Pane3;

  @FXML
  private RadioButton Pane4;

  @FXML
  private ComboBox<String> PaneOptions;

  @FXML
  void showSettings(ActionEvent event) {
    model.showSettings();
  }

  @FXML
  void hideSettings(ActionEvent event) {
    model.hideSettings();
  }

  @FXML
  void showSelectImage(ActionEvent event) {
    model.getHomePageSettings().showSelectImage();
  }

  @FXML
  void saveSettings(ActionEvent event) {
    model.saveSettings();
    model.hideSettings();
  }

  private HomePageModel model;

  @FXML
  void initialize() {
    //Initalize the model and load the settings
    model = new HomePageModel();

    //Muti-thread the initalization of the settings
    CompletableFuture<Void> initializeSetting = CompletableFuture.runAsync(
      this::initializeSettings
    );
    CompletableFuture<Void> initializeToggleGroup = CompletableFuture.runAsync(
      this::initializeToggleGroup
    );
    CompletableFuture<Void> initializeLayout = CompletableFuture.runAsync(
      this::initializeLayout
    );

    CompletableFuture
      .allOf(initializeSetting, initializeToggleGroup, initializeLayout)
      .join();
  }

  /**
   * Initalize the layout
   *   - Set the layout of the homepage to the selected layout
   */
     
  private void initializeLayout() {
   MainHomePage.getChildren().setAll(model.getHomePageContent());
   model.getHomePageContent().addListener((ListChangeListener<Parent>)change -> {
        MainHomePage.getChildren().setAll(model.getHomePageContent());
   });
   
  }

  /**
   * initializeSettings:
   *    - Bind the values of the settings to the repective property in the model
   */
  private void initializeSettings() {
    //Bind the layout option to the view
    HomePageLayoutOptions.setItems(
      model.getHomePageSettings().getLayoutOptions()
    );
    HomePageLayoutOptions
      .valueProperty()
      .bindBidirectional(model.getHomePageSettings().getLayoutName());
    LayoutImage
      .imageProperty()
      .bind(model.getHomePageSettings().getLayoutImage());

    //Bind the pageContent options to the view
    PaneOptions.setItems(model.getHomePageSettings().getPaneContentOptions());
    PaneOptions
      .valueProperty()
      .bindBidirectional(model.getHomePageSettings().getSelectedPaneContent());
    SelectImage
      .visibleProperty()
      .bind(model.getHomePageSettings().getShowSelectImageButton());

    //Bind the database connection string and username to the view
    DatabaseLocationField
      .textProperty()
      .bindBidirectional(
        model.getHomePageSettings().getDatabaseConnectionString()
      );
    //Bind the username to the view
    UsernameField
      .textProperty()
      .bindBidirectional(model.getHomePageSettings().getUsername());
    //Bind the settings visibility to the view
    SettingsPopup.visibleProperty().bind(model.getShowSettings());
  }

  /**
   * initalizeToggleGroup:
   *    - Initalize the toggle group by adding a listener to the selectedToggleProperty
   *      when the toggleProperty changes run the selectPanel method in the model
   */
  private void initializeToggleGroup() {
    PaneSelect
      .selectedToggleProperty()
      .addListener((obs, oldValue, newValue) -> {
        model.getHomePageSettings().selectPanel(newValue);
      });
  }
}
