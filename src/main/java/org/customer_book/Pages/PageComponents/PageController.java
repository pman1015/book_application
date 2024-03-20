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
    BorderPane.setTop(App.loadPage("PageComponents", "Header"));
    BorderPane.setLeft(App.loadPage("PageComponents", "NavBar"));
    model
      .getCurrent_page_property()
      .addListener((obs, oldPage, newPage) -> {
        MainContent.getChildren().clear();
        MainContent.getChildren().add(newPage);
      });
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
