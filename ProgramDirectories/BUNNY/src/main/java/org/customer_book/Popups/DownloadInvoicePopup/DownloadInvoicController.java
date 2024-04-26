package org.customer_book.Popups.DownloadInvoicePopup;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import org.customer_book.App;
import org.customer_book.Database.InvoiceCollection.InvoiceDAO;

public class DownloadInvoicController {

  @FXML
  private AnchorPane popup;

  @FXML
  private Label StatusLabel;

  @FXML
  private ProgressIndicator ProgressMeter;

  @FXML
  private Button CloseButton;

  @FXML
  void ClosePopup(ActionEvent event) {
    App.removePopup();
  }

  private DownloadInvoiceModel model;

  @FXML
  void initialize() {
    model = new DownloadInvoiceModel();

    CloseButton.visibleProperty().bind(model.getComplete());
    ScheduledService<Void> downloadInvoice = new ScheduledService<Void>() {
      @Override
      protected Task<Void> createTask() {
        return new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            Platform.runLater(()->{
                StatusLabel.setText(model.getMessage());
                if(model.getStepCount() > model.getTotalSteps()){
                    model.getComplete().set(true);
                    ProgressMeter.setProgress(1.0);
                }else{
                    model.downloadInvoice();
                    ProgressMeter.setProgress(-1);
                }
            });
            if(model.getStepCount() > model.getTotalSteps()){
                cancel();
            }
            return null;
          }
        };
      }
    };
    downloadInvoice.setPeriod(javafx.util.Duration.seconds(1));
    popup
      .sceneProperty()
      .addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
          downloadInvoice.start();
        }else{
            downloadInvoice.cancel();
        }
      });
  }

  public void setProperties(
    InvoiceDAO invoice,
    StringProperty downloadLocation
  ) {
    model.setInitialData(invoice, downloadLocation);
    //model.downloadInvoice();
  }
}
