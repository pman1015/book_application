package org.customer_book.Pages.PageComponents;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class PageModel {

  // ----------------------FXML Attributes----------------------//
  //These Attrubutes are used to store the current page and the popup page
  private SimpleObjectProperty<Parent> current_page_property;
  private SimpleObjectProperty<Parent> page_popup_property;

  public PageModel() {
    //--Initialize the properties--//
    current_page_property = new SimpleObjectProperty<>();
    current_page_property.set(new Parent() {});

    page_popup_property = new SimpleObjectProperty<>();
    page_popup_property.set(new Parent() {});
  }

  /**
   * This function takes in a Parent and adds it as a popup to the current
   * scene
   * @param popup - the popup to be added this
   *  should be an AnchorPane to be properyl centered on the page
   */
  public void addPopup(Parent popup) {
    popup.setVisible(false);
    page_popup_property.set(popup);
    page_popup_property.get().setVisible(true);
  }
  /**
   * This function removes the current popup from the scene graph
   */
  public void removePopup() {
    page_popup_property.set(new Parent() {});
  }
  public void setCurrent_page_property(Parent page) {
    current_page_property.set(page);
  }
}
