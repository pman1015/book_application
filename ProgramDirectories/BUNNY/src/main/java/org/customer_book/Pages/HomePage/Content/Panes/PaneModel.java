package org.customer_book.Pages.HomePage.Content.Panes;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import org.customer_book.App;
import org.customer_book.Utilities.HomePageConfig.PaneConfig;

public abstract class PaneModel {

  private int paneNumber;
  private PaneConfig paneInfo;
  public ObjectProperty<PaneConfig> paneInfoProperty = new SimpleObjectProperty();

  //Set the PaneConfig for the pane number
  public void setPaneInfo(int paneNumber) {
    this.paneNumber = paneNumber;
    try {
      this.paneInfo = App.homePageConfigUtil.getPaneConfigs().get(paneNumber);
      System.out.println("Pane class: " + this.getClass().toString());
      ValidatePaneConfig(this.getClass().toString());
      paneInfoProperty.set(paneInfo);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Save the PaneConfig to the file
   * - Save the PaneConfig to the file
   */
  public void savePane() {
    App.homePageConfigUtil.savePaneConfig(paneInfo, paneNumber);
  }

  /**
   * ValidatePaneConfig
   */
  private void ValidatePaneConfig(String paneClass) {
    if (
      paneInfo.getPaneClassName() != null &&
      paneInfo.getPaneClassName().equals(paneClass)
    ) {
      return;
    }
    PaneConfig newPane = new PaneConfig();
    newPane.setPaneClassName(paneClass);
    this.paneInfo = newPane;
    savePane();
  }
}
