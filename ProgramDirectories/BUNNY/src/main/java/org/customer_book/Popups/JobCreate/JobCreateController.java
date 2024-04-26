package org.customer_book.Popups.JobCreate;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import org.customer_book.App;
import org.customer_book.Database.CustomerCollection.CustomerDAO;

public class JobCreateController {

  @FXML
  private ComboBox<String> MachineNameBox;

  @FXML
  private TextField JobNameField;

  @FXML
  private TextArea JobDescriptionField;

  @FXML
  private Tooltip JobNameError;

  @FXML
  private Tooltip MachineError;

  @FXML
  void close(ActionEvent event) {
    App.removePopup();
  }

  @FXML
  void CreateJob(ActionEvent event) {
    model.addNewJob();
  }

  private JobCreateModel model;

  @FXML
  void initialize() {
    model = new JobCreateModel();
    MachineNameBox.setItems(model.getMachineNames());
    MachineNameBox.valueProperty().bindBidirectional(model.getMachineName());
    JobNameField.textProperty().bindBidirectional(model.getJobName());
    JobDescriptionField
      .textProperty()
      .bindBidirectional(model.getJobDescription());


    JobNameError.textProperty().bind(model.getJobNameError());
   
    makeErrorListener(model.getJobNameError(), JobNameError, (Node)JobNameField);
    MachineError.textProperty().bind(model.getMachineError());
   
    makeErrorListener(model.getMachineError(), MachineError, (Node)MachineNameBox);
    
  }
    

  public void setCustomer(CustomerDAO customer) {
    model.setCustomer(customer);
  }
  public void makeErrorListener(StringProperty valueToListen, Tooltip errorToShow, Node nodeToAttatchTo){
    errorToShow.setOpacity(0);
    valueToListen.addListener((obs, oldVal, newVal) -> {
      if (newVal.isEmpty()) {
        errorToShow.hide();
      } else {
        errorToShow.setOpacity(1);
        Point2D point = nodeToAttatchTo.localToScene(nodeToAttatchTo.getLayoutBounds().getMaxX(),nodeToAttatchTo.getLayoutBounds().getMaxY());
        errorToShow.show(nodeToAttatchTo,point.getX(),point.getY() );
        errorToShow.setAutoHide(true);
        //errorToShow.setShowDuration(javafx.util.Duration.valueOf("15s"));
      }
    });
  }
}
