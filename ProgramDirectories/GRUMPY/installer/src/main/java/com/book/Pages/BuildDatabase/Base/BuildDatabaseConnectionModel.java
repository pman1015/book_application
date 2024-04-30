package com.book.Pages.BuildDatabase.Base;

import com.book.App;
import com.book.Pages.BuildDatabase.MenuPages.PageControllerAbstract;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildDatabaseConnectionModel {

    // --------------- View Properties --------------- //
    private ObjectProperty<Parent> currentPage = new SimpleObjectProperty<>();
    private BooleanProperty showPrevious = new SimpleBooleanProperty(false);
    private BooleanProperty showNext = new SimpleBooleanProperty(true);

    // ---------------- Model Properties -------------- //
    private IntegerProperty page = new SimpleIntegerProperty(-1);

    Parent[] pages;
    String[] pageNames = { "ConnectionStringPage" , "LoadingConnection","DatabaseSetupPage","SuscessPage" };
    String menuPagesFolder = "BuildDatabaseConnection/MenuPages";

    // ------------------ Model Methods --------------- //
    public BuildDatabaseConnectionModel() {
        loadPages();
        page.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                currentPage.set(pages[newValue.intValue()]);
                showPrevious.set(newValue.intValue() > 0);
                showNext.set(newValue.intValue() < pages.length);
            });
        });
    }

    public void loadPages() {
        pages = new Parent[pageNames.length];
        Thread pageLoader = new Thread(() -> {
            System.out.println("Loading Pages on: " + Thread.currentThread().getName());
            for (int i = 0; i < pageNames.length; i++) {
                try {
                    FXMLLoader loader = App.getLoader(menuPagesFolder, pageNames[i]);
                    Parent parent = loader.load();
                    ((PageControllerAbstract) loader.getController()).setPage(page);
                    pages[i] = parent;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            page.set(0);
        });
        pageLoader.start();
    }

    // ------------------ View Methods --------------- //
    public void getPrevious() {
        System.out.println("Previous");
        if (page.get() > 0) {
            page.set(page.get() - 1);
        }
    }

    public void getNext() {
        System.out.println("Next");
        if (page.get() < pages.length - 1) {
            page.set(page.get() + 1);
        }

    }

}
