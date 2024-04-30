package com.book.Pages.AtlasGuide.Base;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;

public class AtlasGuideController {

    @FXML
    private Button AdvanceButton;

    @FXML
    private WebView webView;

    @FXML
    private Button BackButton;

    @FXML
    private Text StepDescription;

    @FXML
    private TextField url;

    @FXML
    void BackStep(ActionEvent event) {
        model.backStep();
    }

    @FXML
    void AdvanceStep(ActionEvent event) {
        model.advanceStep();

    }

    @FXML
    void back(ActionEvent event) {
        webView.getEngine().getHistory().go(-1);
    }

    @FXML
    void goTo(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            webView.getEngine().load(url.getText());
        }

    }

    @FXML
    void reload(ActionEvent event) {
        webView.getEngine().reload();
    }

    private AtlasGuideModel model;

    @FXML
    void initialize() {
        model = new AtlasGuideModel();

        StepDescription.textProperty().bind(model.getCurrentStepDescription());
        BackButton.disableProperty().bind(model.getDisableBackStep());
        AdvanceButton.disableProperty().bind(model.getDisableAdvanceStep());
        // url.textProperty().bindBidirectional(model.getUrl());

        webView.getEngine().setJavaScriptEnabled(true);
        webView.getEngine().setOnError(new EventHandler<WebErrorEvent>() {
            @Override
            public void handle(WebErrorEvent event) {
                System.out.println("Error: " + event.getMessage());
            }

        });
        webView.getEngine().locationProperty().addListener((obs, oldLocation, newLocation) -> {
            url.setText(newLocation);
        });

        // webView.getEngine().setUserAgent("use required / Chrome");
        webView.getEngine().load(model.getAtlasURL());

    }
}
