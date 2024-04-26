package com.book.Pages.Base;

import lombok.Getter;
import lombok.Setter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;

@Getter
@Setter
public class BaseModel {
    //--------------- View Properties --------------- //
    private ObjectProperty<Parent> topView = new SimpleObjectProperty<>();
    private ObjectProperty<Parent> centerView = new SimpleObjectProperty<>();
    private ObjectProperty<Parent> bottomView = new SimpleObjectProperty<>();
    private ObjectProperty<Parent> leftView = new SimpleObjectProperty<>();
    private ObjectProperty<Parent> rightView = new SimpleObjectProperty<>();

    //---------------- Model Properties -------------- //


    //------------------ Model Methods --------------- //
    public void setTopView(Parent view){
        topView.set(view);
    }
    public void setCenterView(Parent view){
        centerView.set(view);
    }
    public void setBottomView(Parent view){
        bottomView.set(view);
    }
    public void setLeftView(Parent view){
        leftView.set(view);
    }
    public void setRightView(Parent view){
        rightView.set(view);
    }
    
    
}
