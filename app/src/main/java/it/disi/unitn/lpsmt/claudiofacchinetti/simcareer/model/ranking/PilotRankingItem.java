package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.ranking;

import com.google.gson.annotations.SerializedName;

public class PilotRankingItem {

    @SerializedName("nome")
    private String name;
    @SerializedName("auto")
    private String car;
    @SerializedName("team")
    private String team;
    @SerializedName("punti")
    private Integer points;

    public PilotRankingItem() {
        this.name = null;
        this.car = null;
        this.team = null;
        this.points = null;
    }

    public PilotRankingItem(String name, String car, String team, Integer points) {
        this.name = name;
        this.car = car;
        this.team = team;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
