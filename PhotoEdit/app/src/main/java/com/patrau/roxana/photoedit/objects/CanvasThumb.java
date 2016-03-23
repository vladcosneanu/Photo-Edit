package com.patrau.roxana.photoedit.objects;

public class CanvasThumb {

    private String thumbnail;
    private boolean isChecked = false;

    public CanvasThumb(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
