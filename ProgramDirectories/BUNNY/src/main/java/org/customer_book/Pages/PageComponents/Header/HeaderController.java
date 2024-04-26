package org.customer_book.Pages.PageComponents.Header;

import java.util.ResourceBundle;
import java.util.logging.ErrorManager;

import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import org.customer_book.App;

public class HeaderController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private Button ExitButton;
  @FXML
  private Label UserLabel;
  @FXML
  private Label ErrorMessage;

  @FXML
  void CloseWindow(ActionEvent event) {
    App.closeWindow();

  }

  private HeaderModel model;

  @FXML
  void initialize() {
    model = new HeaderModel();
    UserLabel.textProperty().bind(model.getUserName());
    ErrorMessage.textProperty().bind(model.getError());

    assert ExitButton != null : "fx:id=\"ExitButton\" was not injected: check your FXML file 'Header.fxml'.";
    ExitButton.addEventHandler(
        MouseEvent.MOUSE_ENTERED,
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent e) {
            ExitButton.setEffect(new GaussianBlur(2));
          }
        });
    ExitButton.addEventHandler(
        MouseEvent.MOUSE_EXITED,
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent e) {
            ExitButton.setEffect(new GaussianBlur(0));
          }
        });
    Platform.runLater(() -> {
      if (!App.db.isConnected())
        App.db.reConnect();
      if (ErrorMessage.getScene().getProperties().containsKey("Error")) {
        model.setError((String) ErrorMessage.getScene().getProperties().get("Error"));
      }
      ErrorMessage.getScene().getProperties().addListener(new MapChangeListener<Object, Object>() {

        @Override
        public void onChanged(Change<? extends Object, ? extends Object> change) {
          if (change.wasAdded()) {
            if (change.getKey().equals("Error")) {
              model.setError((String) change.getValueAdded());
            }
           
          } if (change.wasRemoved()) {
              if (change.getKey().equals("Error")) {
                model.setError("");
              }
            }
        }
      });
    });
  }
}
