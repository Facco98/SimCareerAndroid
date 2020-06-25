package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SettingItem implements Serializable {

    @SerializedName("tipo")
    private String type;

    @SerializedName("valore")
    private String value;

    public SettingItem() {
    }

    public SettingItem(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
