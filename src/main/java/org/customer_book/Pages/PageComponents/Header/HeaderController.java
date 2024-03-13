package org.customer_book.Pages.PageComponents.Header;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import org.customer_book.App;

public class HeaderController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private Button ExitButton;

  @FXML
  void CloseWindow(ActionEvent event) {
    App.closeWindow();
  }

  @FXML
  void initialize() {
    assert ExitButton !=
    null : "fx:id=\"ExitButton\" was not injected: check your FXML file 'Header.fxml'.";
    ExitButton.addEventHandler(
      MouseEvent.MOUSE_ENTERED,
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          ExitButton.setEffect(new GaussianBlur(2));
        }
      }
    );
    ExitButton.addEventHandler(
      MouseEvent.MOUSE_EXITED,
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          ExitButton.setEffect(new GaussianBlur(0));
        }
      }
    );
  }
}
