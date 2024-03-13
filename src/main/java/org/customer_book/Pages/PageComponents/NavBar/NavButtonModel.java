package org.customer_book.Pages.PageComponents.NavBar;

import java.io.IOException;

import org.customer_book.App;

public class NavButtonModel {
    public NavButtonModel() {
    }

    public void navigate(String pageName) throws IOException{
        switch(pageName){
            case "Home":
                //navigate to home page
               
                break;
            case "Customers":
                //navigate to customers page
               App.setPage("CustomerListPage", "CustomerListPage");
                break;
            case "Jobs":
                //navigate to jobs page
                break;
            case "Inventory":
                //navigate to inventory page
                break;
            case "Equipment":
                //navigate to equipment page
                break;
            case "Bills":
                //navigate to bills page
                break;
        }
    }
    
}
