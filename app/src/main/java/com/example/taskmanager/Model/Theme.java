package com.example.taskmanager.Model;

public class Theme {
    int colorImage;
    String colorName;
    String textColor;

    public Theme(int colorImage, String colorName, String textColor) {
        this.colorImage = colorImage;
        this.colorName = colorName;
        this.textColor = textColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public int getColorImage() {
        return colorImage;
    }

    public String getColorName() {
        return colorName;
    }

}
