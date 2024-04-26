package org.customer_book.Pages.HomePage.Content.Panes.Image;

import java.io.InputStream;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.customer_book.App;
import org.customer_book.Pages.HomePage.Content.Panes.PaneModel;

@Getter
@Setter
public class ImageModel extends PaneModel {

  //------------------- View Properties -------------------//
  private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

  //------------------- Model Properties -------------------//

  //------------------- Constructor -------------------//
  public ImageModel() {
    paneInfoProperty.addListener((obs, oldPane, newPane) -> {
      try {
        System.out.println("ImageModel: PaneInfo Changed");
        if (newPane != null && newPane.getImageFile() != null) {
          if (newPane.getImageFile().exists()) {
            System.out.println("Image Path: " + newPane.getImageFile().toPath().toUri().toString());
            imageProperty.set(
              new Image(
                newPane.getImageFile().toPath().toUri().toString(),
                true
              )
            );
          } else {
            imageProperty.set(
              new Image(
                App.class.getResourceAsStream("Data/ImageNotFound.jpg")
              )
            );
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
