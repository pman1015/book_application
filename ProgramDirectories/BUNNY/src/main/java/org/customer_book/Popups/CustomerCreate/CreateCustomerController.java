package org.customer_book.Popups.CustomerCreate;

import java.util.concurrent.CompletableFuture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateCustomerController {

  @FXML
  private TextField CustomerAddressField;

  @FXML
  private TextField CustomerNicknameField;

  @FXML
  private TextField CustomerPhoneNumberField;

  @FXML
  private Label CustomerPhoneNumberError;

  @FXML
  private TextField CustomerNameField;

  @FXML
  private Label CustomerAddressError;

  @FXML
  private Label CustomerNameError;

  @FXML
  private Label CustomerNicknameError;

  @FXML
  void closeAddNewCustomer(ActionEvent event) {
    model.closeAddNewCustomer();
  }

  @FXML
  void addNewCustomer(ActionEvent event) {
    model.addNewCustomer();
  }

  private CreateCustomerModel model;
  @FXML
  void initialize() {
    model = new CreateCustomerModel();
    CompletableFuture<Void> initializeErrors = CompletableFuture.runAsync(this::initializeErrors);
    CompletableFuture<Void> initializeFields = CompletableFuture.runAsync(this::initializeFields);

    CompletableFuture.allOf(initializeErrors,initializeFields).join();
  }

  private void initializeErrors(){
    CustomerNameError.textProperty().bind(model.getCustomerNameError());
    CustomerNicknameError.textProperty().bind(model.getCustomerNicknameError());
    CustomerAddressError.textProperty().bind(model.getCustomerAddressError());
    CustomerPhoneNumberError.textProperty().bind(model.getCustomerPhoneNumberError());
    
  }
  private void initializeFields(){
    CustomerNameField.textProperty().bindBidirectional(model.getCustomerName());
    CustomerNicknameField.textProperty().bindBidirectional(model.getCustomerNickname());
    CustomerAddressField.textProperty().bindBidirectional(model.getCustomerAddress());
    CustomerPhoneNumberField.textProperty().bindBidirectional(model.getCustomerPhoneNumber());
  }

  
}
