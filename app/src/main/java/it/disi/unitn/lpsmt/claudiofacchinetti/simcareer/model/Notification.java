package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    public Notification() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
