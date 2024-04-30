package com.book.Pages.AtlasGuide.Base;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtlasGuideModel {

    // ------- View Properties -------//
    private String atlasURL = "https://www.mongodb.com/cloud/atlas/register";
    private StringProperty currentStepDescription = new SimpleStringProperty();
    private BooleanProperty disableBackStep = new SimpleBooleanProperty(true);
    private BooleanProperty disableAdvanceStep = new SimpleBooleanProperty(false);
    private StringProperty url = new SimpleStringProperty(atlasURL);

    // -------- Model Properties -------//
    private int currentStep = 0;
    private String[] steps = { "Step 1: Sign up for an atlas account *Or sign up with google* ",
            "Step 2: Navigate to Organizations and create a new organization",
            "Step 3: Navigate to Projects and create a new project",
            "Step 4: Navigate to Overview and click 'Create a deployment' *Note this may not work in this browser if that is the case copy and paste the url into a new window and continue*",
            "Step 5: Select M0 for deployment leave the rest the same and press create",
            "Step 6: Copy the password and username to a secure location and press Create Database User",
            "Step 7: Close out of the setup and select the Network Access tab under Secutrity",
            "Step 8: Select Add an IP address and select Allow Access from Anywhere then confirm",
            "Step 9: Navigate to the Database tab under deployment and click Connect",
            "Step 10: Select Compass from the data through tools",
            "Step 11: Copy the portion of the connection string after @ this is your host section",
            "Step 12: with all of the information recorded go to the Build Database tool in this app and setip the database"

    };

    // -------- Constructor ------//
    public AtlasGuideModel() {
        updateStepDescription();
    }

    // -------- Model Methods -------//
    private void updateStepDescription() {
        currentStepDescription.set(steps[currentStep]);
    }

    // -------- View Methods -------//
    public void backStep() {
        if (currentStep > 0) {
            currentStep--;
            updateStepDescription();
        }
        if (currentStep == 0) {
            disableBackStep.set(true);
        }

    }

    public void advanceStep() {
        if (currentStep < steps.length - 1) {
            currentStep++;
            updateStepDescription();
        }
        if (currentStep == steps.length - 1) {
            disableAdvanceStep.set(true);
        }
    }

}
