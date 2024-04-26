package org.customer_book.Utilities.HomePageConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Database.DatabaseConnection;

@Getter
@Setter
public class HomePageConfig {

  private File homeConfigFile;
  private HomePageConfigPermenant homePageConfig;
  private ArrayList<PaneConfig> paneConfigs;
  private ArrayList<File> paneConfigFiles;

  public HomePageConfig() {
    // Get the Resource URL

    URL resourceURL;
    try {
      resourceURL = new URL(
          App.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("%20", " "));
    }
    catch (MalformedURLException e) {
      System.out.println("Error loading the resource URL: " + e.getMessage());
      return;
    }

    // Get the DataFolder
    File DataFolder = new File(new File(resourceURL.getPath()).getParent() + "/Data");
    // If the DataFolder does not exist create it
    if (!DataFolder.exists()) {
      DataFolder.mkdir();
    }
    // Get the HomeConfig file if it doesnt exist create it
    homeConfigFile = new File(DataFolder + "/HomeConfig.txt");
    if (!homeConfigFile.exists()) {
      try {
        homeConfigFile.createNewFile();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    // Load the HomePageConfig
    loadHomePageConfig();
    // Ensure there are four pane configs
    paneConfigs = new ArrayList<>();
    paneConfigFiles = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      // load the respective config file
      File paneConfigFile = new File(DataFolder + "/PaneConfig" + (i + 1) + ".txt");
      if (!paneConfigFile.exists()) {
        try {
          paneConfigFile.createNewFile();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
      // Save the files
      paneConfigFiles.add(i, paneConfigFile);
      // Load the PaneConfig
      loadPaneConfig(paneConfigFile, i);
    }
  }

  public void setHomePageConfig(HomePageConfigPermenant homePageConfig) {
    this.homePageConfig = homePageConfig;
    saveHomePageConfig(homePageConfig);
    // If the database connection failed reload the database with the new string
    if (App.db == null || !App.db.isConnected()) {
      try {
        App.db = new DatabaseConnection();
      }
      catch (Exception e) {
        e.printStackTrace();
      }

    }
  }

  /**
   * load the PaneConfig from the file - If the file does not exist create a new
   * PaneConfig
   */
  private void loadPaneConfig(File paneConfigFile, int paneNumber) {
    try (ObjectInputStream ooi = new ObjectInputStream(new FileInputStream(paneConfigFile))) {
      paneConfigs.add(paneNumber, (PaneConfig) ooi.readObject());
    }
    catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      PaneConfig paneConfig = new PaneConfig();
      savePaneConfig(paneConfig, paneNumber);
    }
  }

  /**
   * Save the PaneConfig to the file
   */
  public void savePaneConfig(PaneConfig paneConfig, int paneNumber) {
    File paneConfigFile = paneConfigFiles.get(paneNumber);
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(paneConfigFile))) {
      oos.writeObject(paneConfig);
      paneConfigs.set(paneNumber, paneConfig);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load the HomePageConfig from the file - If the file does not exist create a
   * new HomePageConfig
   * 
   * @return
   */
  private void loadHomePageConfig() {
    try (ObjectInputStream ooi = new ObjectInputStream(new FileInputStream(homeConfigFile))) {
      homePageConfig = (HomePageConfigPermenant) ooi.readObject();
    }
    catch (IOException | ClassNotFoundException e) {
      // e.printStackTrace();
      this.homePageConfig = new HomePageConfigPermenant();
      saveHomePageConfig(homePageConfig);
    }
  }

  private void saveHomePageConfig(HomePageConfigPermenant homePageConfig) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(homeConfigFile))) {
      oos.writeObject(homePageConfig);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
