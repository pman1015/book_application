package org.customer_book;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import org.customer_book.Database.DatabaseConnection;
import org.customer_book.Pages.PageComponents.PageController;
import org.customer_book.Pages.PageComponents.PageModel;
import org.customer_book.Utilities.HomePageConfig.HomePageConfig;
import org.scenicview.ScenicView;

/**
 * JavaFX App
 */
public class App extends Application {

  // The scene for the application
  private static Scene scene;
  // Reference to the main page model
  private static PageModel mainPage;

  // Database connection
  public static DatabaseConnection db;

  // Homepage Layout
  public static HomePageConfig homePageConfigUtil;

  @Override
  public void start(Stage stage) throws IOException {
    // Initialise the homepage config
    homePageConfigUtil = new HomePageConfig();

    // Load the main page from fxml
    FXMLLoader fxmlLoader = getLoader("PageComponents", "BasePage");
    // Set the loaded page as the scene
    scene = new Scene(fxmlLoader.load());
    // Store a static refrence to the page model for navigation
    mainPage = (PageModel) ((PageController) fxmlLoader.getController()).getModel();
    // Set the Homepage as the current page
    setPage("HomePage", "HomePageBaseLayout");
    // Load the fonts for the project
    loadFonts();
    // Add index.css to the scene
    System.out.println(App.class.getResource("").toString() + "css/index.css");
    scene.getStylesheets().add(App.class.getResource("").toString() + "css/Index.css");

    // Size and style the stage so it is the appropriate size and has no top bar
    stage.sizeToScene();
    stage.setResizable(false);
    stage.initStyle(StageStyle.UNDECORATED);
    stage.setScene(scene);
    // ScenicView.show(scene);
    stage.show();

    // Try to initalise the database connection
    try {
      db = new DatabaseConnection();

    }
    catch (Exception e) {
      System.out.println("Error connecting to database");
    }
  }

  /**
   * Closes the current scene
   * 
   * @param none
   * @return none
   */
  public static void closeWindow() {
    ((Stage) scene.getWindow()).close();
    Platform.exit();
  }

  /**
   * Takes in a pageName ans sets it as the content of the page
   * 
   * @param pageName
   * @param fxml
   * @throws IOException
   */
  static void setRoot(String pageName, String fxml) throws IOException {
    scene.setRoot(loadPage(pageName, fxml));
  }

  /**
   * This function takes in a popup parent and adds it to the scene graph The
   * popup should have an AnchorPane as its root
   * 
   * @param popup
   */
  public static void addPopup(Parent popup) {
    mainPage.addPopup(popup);
  }

  /*
   * This function removes the current popup from the scene graph
   */
  public static void removePopup() {
    mainPage.removePopup();
  }

  /**
   * This function takes in a page name and fxml file and sets it as the current
   * page
   * 
   * @param pageName - name of the page folder in the views directory
   * @param fxml     - name of the fxml file
   * @throws IOException - if the fxml file is not found
   */
  public static void setPage(String pageName, String fxml) throws IOException {
    mainPage.setCurrent_page_property(loadPage(pageName, fxml));
  }

  /**
   * This function takes in a parent node and sets it as the current page
   *
   * @param page
   */
  public static void setPage(Parent page) {
    mainPage.setCurrent_page_property(page);
  }

  /**
   * @param pageName
   * @param fxml
   * @return
   * @throws IOException
   */

  public static Parent loadPage(String pageName, String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        App.class.getResource("views/" + pageName + "/" + fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void setSceneProperty(String key, Object value) {
    scene.getProperties().put(key, value);
  }

  public static void removeSceneProperty(String key) {
    scene.getProperties().remove(key);
  }

  public static Object getSceneProperty(String key) {

    return scene.getProperties().get(key);
  }

  public static void setBackPointer(String pageName, String fxml) {
    setSceneProperty("returnDestination", new String[] { pageName, fxml });
  }
  public static boolean hasBackPointer() {
    return getSceneProperty("returnDestination") != null;
  }

  public static void useBackPointer() {
    Object backPointer = App.getSceneProperty("returnDestination");
    if (backPointer != null && backPointer instanceof String[]
        && ((String[]) backPointer).length == 2) {
      try {
        removeSceneProperty("returnDestination");
        setPage(((String[]) backPointer)[0], ((String[]) backPointer)[1]);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public static FXMLLoader getLoader(String pageName, String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        App.class.getResource("views/" + pageName + "/" + fxml + ".fxml"));
    return fxmlLoader;
  }

  public static void main(String[] args) {
    launch();
  }

  public static void loadFonts() {
    Font f = Font.loadFont(
        App.class.getResource("fonts/JetBrains_Mono/static/JetBrainsMono-Regular.ttf")
            .toExternalForm(),
        10);
    System.out.println(f.getName());
  }

  public static Window getStage() {
    return scene.getWindow();
  }
}
