package org.customer_book.Pages.HomePage.Content.Panes.Image;


import org.customer_book.Pages.HomePage.Content.Panes.PaneController;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ImageController extends PaneController{

 

    @FXML
    private AnchorPane Parent;

    @FXML
    private ImageView ImageView;

    private ImageModel model;
    @FXML
    void initialize() {
        model = new ImageModel();

        //Bind the image view to the parent
        Parent.heightProperty().addListener((obs, oldVal, newVal) -> {
            ImageView.setFitHeight(newVal.doubleValue() - 28);
        });
        Parent.widthProperty().addListener((obs, oldVal, newVal) -> {
            ImageView.setFitWidth(newVal.doubleValue() - 28);
        });

        
        //Bind the image to the model
        ImageView.imageProperty().bind(model.getImageProperty());
    }

    @Override
    public void setPaneNumber(int paneNumber) {
        model.setPaneInfo(paneNumber);
        
    }
}
