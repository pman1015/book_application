package org.customer_book.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.customer_book.App;

//This class is used to store and load the download destination for the invoices
@Getter
@Setter
public class DownloadDestination {

  private StringProperty downloadDestination;
  private String downloadDestinationString;

  public DownloadDestination(StringProperty downloadDestination) {
    this.downloadDestination = downloadDestination;
    this.downloadDestinationString = downloadDestination.get();
    loadDownloadDestination();

    downloadDestination.addListener((obs, oldVal, newVal) -> {
      saveNewDownloadDestination(newVal);
    });

    if (downloadDestinationString.equals("")) {
      downloadDestination.set(setDefaultDownloadLocation());
    }
  }

  private void loadDownloadDestination() {
    URL resourceURL;
    try {
      resourceURL = new URL(
          App.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("%20", " "));
      // Get the path of the resourceFile
      File DataFolder = new File(new File(resourceURL.getPath()).getParent() + "/Data");
      // Create the data folder if it does not exist
      if (!DataFolder.exists()) {
        DataFolder.mkdir();
      }
      // Load the DownloadLoaction file
      // If the file does not exist create it
      File downloadLocation = new File(DataFolder + "/DownloadLocation.txt");
      if (!downloadLocation.exists()) {
        downloadLocation.createNewFile();
      }
      // Load the Download location from the file
      InputStream inputStream = new FileInputStream(downloadLocation);
      Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
      downloadDestinationString = scanner.hasNext() ? scanner.next() : "";
      downloadDestination.set(downloadDestinationString);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void saveNewDownloadDestination(String newDownloadDestination) {
    try {
      // Get the resource URL
      URL resourceURL = new URL(
          App.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("%20", " "));
      // Get the path of the resourceFile
      File DataFolder = new File(new File(resourceURL.getPath()).getParent() + "/Data");
      // Create the data folder if it does not exist
      if (!DataFolder.exists()) {
        DataFolder.mkdir();
      }
      // Load the DownloadLoaction file
      // If the file does not exist create it
      File downloadLocation = new File(DataFolder + "/DownloadLocation.txt");
      if (!downloadLocation.exists()) {
        downloadLocation.createNewFile();
      }
      // Save the new download location to the file
      downloadDestinationString = newDownloadDestination;
      downloadDestination.set(downloadDestinationString);
      java.io.FileWriter fileWriter = new java.io.FileWriter(downloadLocation);
      fileWriter.write(downloadDestinationString);
      fileWriter.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String setDefaultDownloadLocation() {
    String DownloadsFolder = System.getProperty("user.home") + "/Downloads";
    return DownloadsFolder;
  }
}
