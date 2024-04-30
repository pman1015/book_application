package com.book.Pages.BuildDatabase.MenuPages.SuscessPage;

import com.book.App;
import com.book.Pages.BuildDatabase.MenuPages.PageModelAbstract;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuscessPageModel extends PageModelAbstract {

    // ---------------- View Properties ----------------//
    private StringProperty connectionString = new SimpleStringProperty("");

    // ---------------- Model Properties ----------------//
    private final int position = 3;

    @Override
    public void onPageChange() {

        if (position == page.get()) {
            currentConnection = getConnectionProperty();
            if (App.getSceneProperty("ValidConnection") != null) {
                Platform.runLater(() -> {
                    connectionString.set(
                            currentConnection.getConnectionString());
                });
            } else {
                page.set(position - 1);
            }
        }
    }

}
