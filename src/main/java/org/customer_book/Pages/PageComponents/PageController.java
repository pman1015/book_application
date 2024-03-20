package org.customer_book.Pages.PageComponents;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import org.customer_book.App;

public class PageController {

  PageModel model;
  @FXML
  private BorderPane BorderPane;

  @FXML
  private AnchorPane MainContent;

  @FXML
  void initialize() throws IOException {
    model = new PageModel();
    //-----------------set the header and nav bar-----------------//
    BorderPane.setTop(App.loadPage("PageComponents", "Header"));
    BorderPane.setLeft(App.loadPage("PageComponents", "NavBar"));
   //Add an event listener to the current page property to
   //update the MainContent on change
    model
      .getCurrent_page_property()
      .addListener((obs, oldPage, newPage) -> {
        MainContent.getChildren().clear();
        MainContent.getChildren().add(newPage);
      });
    //Add an event listener to the popup page property to 
    //add the current popup and remove the old one
    //Also centers the popup on the page if it is an AnchorPane
    model
      .getPage_popup_property()
      .addListener((obs, oldPopup, newPopup) -> {
        MainContent.getChildren().remove(oldPopup);
        MainContent.getChildren().add(newPopup);
        if (newPopup.getClass() == AnchorPane.class) {
          AnchorPane.setTopAnchor(
            newPopup,
            (
              (
                MainContent.getHeight() -
                ((AnchorPane) newPopup).getPrefHeight()
              ) /
              (2)
            )
          );

          AnchorPane.setLeftAnchor(
            newPopup,
            (
              (
                MainContent.getWidth() - ((AnchorPane) newPopup).getPrefWidth()
              ) /
              (2)
            )
          );
        }
      });
  }

  public PageModel getModel() {
    return model;
  }
}
