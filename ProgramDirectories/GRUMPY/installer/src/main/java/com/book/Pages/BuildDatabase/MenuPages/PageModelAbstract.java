package com.book.Pages.BuildDatabase.MenuPages;

import com.book.App;
import com.book.Database.DatabaseConnection;

import javafx.beans.property.IntegerProperty;

public abstract class PageModelAbstract {

    protected IntegerProperty page;

    public abstract void onPageChange();

    public void setPage(IntegerProperty page) {
        this.page = page;
        page.addListener((observable, oldValue, newValue) -> onPageChange());
    }

    protected DatabaseConnection currentConnection;

    public DatabaseConnection getConnectionProperty(){
        return (DatabaseConnection) App.getSceneProperty("CurrentConnection");
    }
    
    public void setConnectionProperty(DatabaseConnection connection){
        App.setSceneProperty("CurrentConnection", connection);
    }

  
    
}
