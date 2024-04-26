package com.book.Pages.Base;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class BaseController {

    @FXML
    private BorderPane baseBorderPane;

    private BaseModel model;
    @FXML
    void initialize() {
        //Create the model
        model = new BaseModel();

        //Bind model properties to the view
        baseBorderPane.topProperty().bind(model.getTopView());
        baseBorderPane.centerProperty().bind(model.getCenterView());
        baseBorderPane.bottomProperty().bind(model.getBottomView());
        baseBorderPane.leftProperty().bind(model.getLeftView());

    }

    public BaseModel getModel(){
        return model;
    }
}
