package org.customer_book.Utilities.HomePageConfig;

import java.io.Serializable;

import org.bson.conversions.Bson;

import java.io.File;
import javafx.css.converter.URLConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class PaneConfig implements Serializable{

    private String paneClassName;
    private File imageFile;
    private Bson filter;

    


}
