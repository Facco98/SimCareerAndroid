package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship;

import com.google.gson.annotations.SerializedName;

public class PilotSubscriptionItem {

    @SerializedName("nome")
    private String name;
    @SerializedName("team")
    private String team;
    @SerializedName("auto")
    private String car;

    public PilotSubscriptionItem() {
    }

    public PilotSubscriptionItem(String name, String team, String car) {
        this.name = name;
        this.team = team;
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
}
