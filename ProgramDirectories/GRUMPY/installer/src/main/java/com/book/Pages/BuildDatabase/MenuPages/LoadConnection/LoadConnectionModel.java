package com.book.Pages.BuildDatabase.MenuPages.LoadConnection;

import com.book.Pages.BuildDatabase.MenuPages.PageModelAbstract;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;


@Getter@Setter
public class LoadConnectionModel extends PageModelAbstract{
    //-------------------- View Properties -------------------- //
    private StringProperty connectionResult = new SimpleStringProperty("");
    private DoubleProperty progress = new SimpleDoubleProperty(-1);

    private final int position = 1;

    @Override
    public void onPageChange() {
        Thread thread = new Thread(() -> {
            if(page.get() == position){
                currentConnection = getConnectionProperty();
                String result = currentConnection.connectToDatabase();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    connectionResult.set(result);
                    progress.set(1);
                });
            }
        });
        thread.start();
        
    }
    
}
