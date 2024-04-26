package com.book.Pages.BuildDatabase.MenuPages.ConnectionPane;

import com.book.Database.DatabaseConnection;
import com.book.Pages.BuildDatabase.MenuPages.PageModelAbstract;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionStringPaneModel extends PageModelAbstract {

    // -------------------- View Properties -------------------- //
    // Pane Visibility
    private BooleanProperty hasConnectionString = new SimpleBooleanProperty(true);

    // Fields for Connection String
    private StringProperty connectionString = new SimpleStringProperty("");

    // Fields for Connection Details
    private StringProperty host = new SimpleStringProperty("");
    private StringProperty port = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");
    private StringProperty username = new SimpleStringProperty("");

    // Error Messages
    private StringProperty connectionStringError = new SimpleStringProperty("");

    private StringProperty hostError = new SimpleStringProperty("");
    private StringProperty portError = new SimpleStringProperty("");
    private StringProperty passwordError = new SimpleStringProperty("");
    private StringProperty usernameError = new SimpleStringProperty("");

    // -------------------- Model Properties -------------------- //
    private final int position = 0;

    // -------------------- Model Methods -------------------- //

    private void clearValues() {
        connectionString.set("");
        host.set("");
        port.set("");
        password.set("");
        username.set("");
    }

    private void clearErrors() {
        connectionStringError.set("");
        hostError.set("");
        portError.set("");
        passwordError.set("");
        usernameError.set("");
    }

    @Override
    public void onPageChange() {
        if (page.get() == position + 1) {
            if (!validateConnection()) {
                page.set(position);
            } else {
                setConnectionProperty(
                        hasConnectionString.get() ? new DatabaseConnection(connectionString.get())
                                : new DatabaseConnection(host.get(), port.get(), username.get(), password.get()));
            }
        }
    }

    private boolean validateConnection() {
        if (hasConnectionString.get()) {
            if (connectionString.get().isEmpty()) {
                connectionStringError.set("Connection String is required");
                return false;
            } else {
                connectionStringError.set("");
                return true;
            }
        } else {
            boolean valid = true;
            if (host.get().isEmpty()) {
                hostError.set("Host is required");
                valid = false;
            } else {
                hostError.set("");
            }
            if (username.get().isEmpty()) {
                usernameError.set("Username is required");
                valid = false;
            } else {
                usernameError.set("");
            }
            if (password.get().isEmpty()) {
                passwordError.set("Password is required");
                valid = false;
            } else {
                passwordError.set("");
            }
            return valid;
        }

    }

    // -------------------- View Methods -------------------- //
    public void toggleConnectionType() {
        hasConnectionString.set(!hasConnectionString.get());
        clearValues();
        clearErrors();
    }

}
