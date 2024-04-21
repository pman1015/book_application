package org.customer_book.Pages.PageComponents.Header;

import lombok.Getter;
import lombok.Setter;
import javafx.beans.property.StringProperty;

import org.customer_book.App;

import javafx.beans.property.SimpleStringProperty;

@Getter
@Setter
public class HeaderModel {
    private StringProperty UserName = new SimpleStringProperty("");
    private StringProperty Error = new SimpleStringProperty("");

    

    public HeaderModel() {
        UserName.set(App.homePageConfigUtil.getHomePageConfig().getUsername());
        
    }



    public void setError(String valueAdded) {
        Error.set(valueAdded);
    }
}
