package org.customer_book.Utilities.HomePageConfig;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.json.JsonObject;
import org.customer_book.App;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

@Getter
@Setter
@AllArgsConstructor
public class HomePageConfigPermenant implements Serializable {

  //Object to store the layout of the homepage
  @Getter
  @Setter
  private class Layout implements Serializable {

    //Name of the layout
    private String layoutName;
    //Array storing the name of the content selected for each pane
    private String[] paneSelections;

    public Layout() {
      layoutName = "";
      paneSelections = new String[4];
    }

    public void updatePaneContent(int paneNumber, String content) {
      paneSelections[paneNumber] = content;
    }
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public class paneOffsets implements Serializable {

    private double topAnchor;
    private double bottomAnchor;
    private double leftAnchor;
    private double rightAnchor;

    @SuppressWarnings("static-access")
    public Node applyOffsets(Node child) {
      AnchorPane.setTopAnchor(child, topAnchor);
      AnchorPane.setBottomAnchor(child, bottomAnchor);
      AnchorPane.setLeftAnchor(child, leftAnchor);
      AnchorPane.setRightAnchor(child, rightAnchor);
      return child;
    }
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public class layoutConfig implements Serializable {

    private ArrayList<paneOffsets> paneOffsets;
    private String layoutName;

    public layoutConfig() {
      paneOffsets = new ArrayList<paneOffsets>();
      layoutName = "";
    }

    public void addPaneOffset(paneOffsets offset, int paneNumber) {
      paneOffsets.add(paneNumber, offset);
    }
    public Node applyOffset(Node child, int paneNumber) {
      return paneOffsets.get(paneNumber).applyOffsets(child);
    }
  }

  private HashMap<String, layoutConfig> layoutConfigs;

  private ArrayList<String> layoutOptions;
  private ArrayList<String> paneContentOptionsList = new ArrayList<String>(
    Arrays.asList("RecentJobs", "Image", "JobStats")
  );

  private String selectedLayout;
  private Layout layout;
  private String databaseConnectionString;
  private String username;

  public HomePageConfigPermenant() {
    this.layout = new Layout();
    this.layout.setLayoutName("LargeLeft");
    this.updatePaneConetent(0, "RecentJobs");
    databaseConnectionString = "";
    username = "";
    initaliseLayouts();
  }

  public String getPaneContent(int paneNumber) {
    return layout.getPaneSelections()[paneNumber];
  }

  public void updatePaneConetent(int paneNumber, String content) {
    layout.updatePaneContent(paneNumber, content);
  }

  public ArrayList<String> getSelections() {
    ArrayList<String> selections = new ArrayList<String>();
    for (int i = 0; i < layout.getPaneSelections().length; i++) {
      if (layout.getPaneSelections()[i] != null) {
        selections.add(layout.getPaneSelections()[i]);
      }
    }
    return selections;
  }

  private void initaliseLayouts() {
    //Initalise the HashMap
    layoutConfigs = new HashMap<String, layoutConfig>();

    //Load the layoutConfig from the JSON file
    JSONTokener tokener = new JSONTokener(
      App.class.getResourceAsStream("Data/layoutConfig.json")
    );
    JSONArray layouts = new JSONArray(tokener);

    //Iterate through the layouts
    for (int i = 0; i < layouts.length(); i++) {
      //Create a new layoutConfig for each
      JSONObject layout = layouts.getJSONObject(i);
      layoutConfig config = new layoutConfig();
      //Set the name
      config.setLayoutName(layout.getString("layoutName"));
      //Set the offsets
      JSONArray paneOffsets = layout.getJSONArray("offsets");
      for (int x = 0; x < paneOffsets.length(); x++) {
        JSONObject offset = paneOffsets.getJSONObject(x);
        paneOffsets offsetObj = new paneOffsets(
          offset.getDouble("topAnchor"),
          offset.getDouble("bottomAnchor"),
          offset.getDouble("leftAnchor"),
          offset.getDouble("rightAnchor")
        );
        config.addPaneOffset(offsetObj, x);
      }
      //Add the layout to the HashMap
        layoutConfigs.put(config.getLayoutName(), config);
    }
    //Set the layout options to the keyset of the HashMap
    layoutOptions = new ArrayList<String>(layoutConfigs.keySet());
  }
  public layoutConfig getLayoutConfig() {
    return layoutConfigs.get(this.selectedLayout);
  }

  public String toString() {
    String asString = "";
    asString += "Username: " + username + "\n";
    asString +=
      "Database Connection String: " + databaseConnectionString + "\n";
    asString += "Selected Layout: " + layout.getLayoutName() + "\n";
    asString +=
      "Pane Selections: " + Arrays.toString(layout.getPaneSelections()) + "\n";
    return asString;
  }
}
