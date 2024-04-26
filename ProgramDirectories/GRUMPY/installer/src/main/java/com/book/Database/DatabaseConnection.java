package com.book.Database;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DatabaseConnection {

    private MongoDatabase database;
    private MongoClient client;

    // Connection information
    private String connectionString;
    private String username;
    private String password;
    private String databaseName;
    private String port;
    private String host;

    // Constructors
    public DatabaseConnection(String host, String port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public DatabaseConnection(String connectionString) {
        this.connectionString = connectionString;
    }

    // Connection Methods
    public String connectToDatabase(String host, String port, String username, String password) {
        // Connect to the database
        try {
            ConnectionString connectionString = new ConnectionString(
                    "mongodb://" + username + ":" + password + "@" + host + ":" + port);
            client = MongoClients.create(connectionString);

        } catch (Exception e) {
            return e.getMessage();
        }
        return "Connected";
    }

    public String connectToDatabase(String connectionString) {
        // Connect to the database
        try {
            ConnectionString connString = new ConnectionString(connectionString);
            client = MongoClients.create(connString);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Connected";
    }

    public String connectToDatabase() {
        // Connect to the database
        try {
            ConnectionString connString;
            if (connectionString != null) {
                connString = new ConnectionString(connectionString);
            } else {
                if (port == null || port.isEmpty()) {
                    connString = new ConnectionString(
                            "mongodb+srv://" + username + ":" + password + "@" + host);
                } else {
                    connString = new ConnectionString(
                            "mongodb://" + username + ":" + password + "@" + host + ":" + port);
                }

            }

            client = MongoClients.create(connString);
            client.listDatabaseNames().iterator().forEachRemaining(name -> {
                System.out.println("Database:" + name);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        return "Connected";
    }

}
