package com.book.Pages.BuildDatabase.MenuPages.LoadConnection;

import com.book.Pages.BuildDatabase.MenuPages.PageControllerAbstract;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class LoadConnectionController extends PageControllerAbstract{

   

    @FXML
    private ProgressIndicator LoadingIndicator;

    @FXML
    private Label ResultMessage;

    @FXML
    void initialize() {
        model = new LoadConnectionModel();
        
        ResultMessage.textProperty().bind(((LoadConnectionModel) model).getConnectionResult());
        LoadingIndicator.progressProperty().bind(((LoadConnectionModel) model).getProgress());
       
    }
}
