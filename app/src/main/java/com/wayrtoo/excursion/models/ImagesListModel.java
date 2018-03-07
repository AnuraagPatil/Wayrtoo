package com.wayrtoo.excursion.models;

import java.io.Serializable;

/**
 * Created by Ishwar on 23/12/2017.
 */
public class ImagesListModel implements Serializable{


    private String Display_Position  ;
    private String ImagePath;

    public String getDisplay_Position() {
        return Display_Position;
    }

    public void setDisplay_Position(String display_Position) {
        Display_Position = display_Position;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}

