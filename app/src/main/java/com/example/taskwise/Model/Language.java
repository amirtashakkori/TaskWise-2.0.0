package com.example.taskwise.Model;

public class Language {
    public int lanLogo;
    public int lanName;
    public boolean selected;

    public Language(int lanLogo, int lanName) {
        this.lanLogo = lanLogo;
        this.lanName = lanName;
    }

    public int getLanLogo() {
        return lanLogo;
    }

    public void setLanLogo(int lanLogo) {
        this.lanLogo = lanLogo;
    }

    public int getLanName() {
        return lanName;
    }

    public void setLanName(int lanName) {
        this.lanName = lanName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
