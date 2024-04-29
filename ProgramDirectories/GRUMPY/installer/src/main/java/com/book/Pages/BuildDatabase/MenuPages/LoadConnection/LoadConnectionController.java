package com.book.Pages.BuildDatabase.MenuPages.LoadConnection;

import com.book.Pages.BuildDatabase.MenuPages.PageControllerAbstract;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.shape.SVGPath;

public class LoadConnectionController extends PageControllerAbstract{

   

    @FXML
    private ProgressIndicator LoadingIndicator;

    @FXML
    private Label ResultMessage;

    @FXML
    private SVGPath ErrorIndicator;

    @FXML
    void initialize() {
        model = new LoadConnectionModel();
        
        ErrorIndicator.visibleProperty().bind(((LoadConnectionModel) model).getErrorIndicator());
        LoadingIndicator.visibleProperty().bind(((LoadConnectionModel) model).getErrorIndicator().not());
        ResultMessage.textProperty().bind(((LoadConnectionModel) model).getConnectionResult());
        LoadingIndicator.progressProperty().bind(((LoadConnectionModel) model).getProgress());
       
    }
}
