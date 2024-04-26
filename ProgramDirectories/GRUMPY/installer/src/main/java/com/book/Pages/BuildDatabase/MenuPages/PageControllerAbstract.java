package com.book.Pages.BuildDatabase.MenuPages;

import javafx.beans.property.IntegerProperty;

public abstract class PageControllerAbstract {

    protected PageModelAbstract model;

    public void setPage(IntegerProperty page) {
        model.setPage(page);
    }

}
