package org.customer_book.Pages.PageComponents;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import lombok.Getter;
@Getter
public class PageModel {

  private SimpleObjectProperty<Parent> current_page_property;
  private SimpleObjectProperty<Parent> page_popup_property;

  public PageModel() {
    current_page_property = new SimpleObjectProperty<>();
    current_page_property.set(new Parent() {});

    page_popup_property = new SimpleObjectProperty<>();
    page_popup_property.set(new Parent() {});
  }

  public SimpleObjectProperty<Parent> getCurrent_page_property() {
    return current_page_property;
  }

  public void setCurrent_page_property(Parent page) {
    current_page_property.set(page);
  }
  public void addPopup(Parent popup) {
    page_popup_property.set(popup);
  }
  public void removePopup() {
    page_popup_property.set(new Parent() {});
  }
}
