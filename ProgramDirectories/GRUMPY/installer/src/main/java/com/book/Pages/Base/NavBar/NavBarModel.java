package com.book.Pages.Base.NavBar;

import java.io.IOException;

import com.book.App;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class NavBarModel {
    private final boolean detailedDebug = false;

    public void goToAtlasGuide() {
        FXMLLoader atlasGuidLoader = App.getLoader("AtlasGuide", "AtlasGuide");
        try {
            Parent page = atlasGuidLoader.load();
            App.setPage(page);
        } catch (IOException e) {
            System.out.println("Error loading AtlasGuide page: " + e.getMessage());
            if (detailedDebug) {
                e.printStackTrace();
            }
        }
    }

    public void goToBuild() {
        FXMLLoader loader = App.getLoader("BuildDatabaseConnection", "BuildDatabaseConnection");
        try {
            Parent page = loader.load();
            App.setPage(page);
        } catch (IOException e) {
            System.out.println("Error loading BuildDatabaseConnection page: " + e.getMessage());
            if (detailedDebug) {
                e.printStackTrace();
            }
        }
    }

}
