package com.book;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.book.Pages.Base.BaseController;
import com.book.Pages.Base.BaseModel;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static BaseModel basePageModel;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the BasePage
        FXMLLoader loader = getLoader("Base", "BasePage");
        Parent root = loadFXML(loader);
        if (loader.getController() instanceof com.book.Pages.Base.BaseController) {
            basePageModel = ((BaseController) loader.getController()).getModel();
        } else {
            System.out.println("Error loading BasePage model");
            return;
        }

        // Load the navBar, header, and base page
        Parent navBar = loadFXML(getLoader("Base", "NavBar"));
        basePageModel.setLeftView(navBar);

        Parent header = loadFXML(getLoader("Base", "Header"));
        basePageModel.setTopView(header);

        Parent homePage = loadFXML(getLoader("BuildDatabaseConnection", "BuildDatabaseConnection"));
        basePageModel.setCenterView(homePage);

        

        // Set the stage to the base page
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(App.class.getResource("").toString() + "css/index.css");
        // Load the fonts for the application
        loadFonts();
        // Size and style the stage so it is the appropriate size and has no top bar
        stage.sizeToScene();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        
        stage.setScene(scene);
        // ScenicView.show(scene);
        stage.show();
    }

    /**
     * getLoader
     * - Get the FXMLLoader for the given pagePath and fileName
     * 
     * @param pagePath - Path to the FXML folder containg the file from the App
     *                 resoucres
     * @param fileName
     * @return
     */
    public static FXMLLoader getLoader(String pagePath, String fileName) {
        return new FXMLLoader(App.class.getResource("views/" + pagePath + "/" + fileName + ".fxml"));
    }

    /**
     * Load the FXMLLoader and return the Parent
     * 
     * @param loader - FXML Loader to be loaded
     * @return
     */
    public static Parent loadFXML(FXMLLoader loader) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Error loading fxml file: " + e.getMessage());
            e.printStackTrace();
        }
        return root;
    }

    /**
     * setPage
     * - Set the page to the given FXML file-name in the given path
     * 
     * @param pagePath - Path to the FXML folder containg the file from the App
     *                 resoucres
     * @param fileName - Name of the FXML file
     */
    public static void setPage(String pagePath, String fileName) {
        Parent newRoot = loadFXML(getLoader(pagePath, fileName));
        if (newRoot != null) {
            basePageModel.setCenterView(newRoot);
        } else {
            System.out.println("Error loading page: " + fileName + " in path: " + pagePath);
        }
    }

    /**
     * setPage
     * - Set the page to the given Parent
     * 
     * @param p - Parent to be set as the root
     */
    public static void setPage(Parent p) {
        basePageModel.setCenterView(p);
    }

    /**
     * load the fonts for the application
     */
    private static void loadFonts() {
        Font font = Font.loadFont(
                App.class.getResource("fonts/JetBrains_Mono/static/JetBrainsMono-Regular.ttf").toExternalForm(), 10);

    }
    /**
     * closeWindow
     */
    public static void closeWindow(){
        ((Stage)scene.getWindow()).close();
        Platform.exit();
    }

    public static void setSceneProperty(String key, Object value){
        scene.getProperties().put(key, value);
    }
    public static Object getSceneProperty(String key){
        return scene.getProperties().get(key);
    }
    public static void removeSceneProperty(String key){
        scene.getProperties().remove(key);
    }

    public static void main(String[] args) {
        launch();
    }

}