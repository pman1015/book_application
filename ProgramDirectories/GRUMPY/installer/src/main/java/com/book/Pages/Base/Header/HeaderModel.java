package com.book.Pages.Base.Header;

import com.book.App;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class HeaderModel {
    

    //--------------- View Properties --------------- //


    //---------------- Model Properties -------------- //


    //------------------ Model Methods --------------- //


    //------------------ View Methods --------------- //
    public void closeWindow(){
        App.closeWindow();
    }
}
