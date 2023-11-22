package com.example.taskwise.Model;

public class Option {
    String optionTitle;
    int optionIcon;

    public Option(String optionTitle, int optionIcon) {
        this.optionTitle = optionTitle;
        this.optionIcon = optionIcon;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public int getOptionIcon() {
        return optionIcon;
    }

    public void setOptionIcon(int optionIcon) {
        this.optionIcon = optionIcon;
    }
}
