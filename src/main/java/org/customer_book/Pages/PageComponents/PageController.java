package org.customer_book.Pages.PageComponents;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.customer_book.App;

public class PageController {

  PageModel model;

  @FXML
  private BorderPane BorderPane;



  @FXML
  void initialize() throws IOException {
    model = new PageModel();
    BorderPane.setTop(App.loadPage("PageComponents", "Header"));
    BorderPane.setLeft(App.loadPage("PageComponents", "NavBar"));
    BorderPane.centerProperty().bind(model.getCurrent_page_property());
  }

  public PageModel getModel() {
    return model;
  }
}
