package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.ranking;

import com.google.gson.annotations.SerializedName;

public class TeamRankingItem {

    @SerializedName("team")
    private String name;
    @SerializedName("auto")
    private String car;
    @SerializedName("punti")
    private Integer points;

    public TeamRankingItem() {
        this.name = null;
        this.car = null;
        this.points = null;
    }

    public TeamRankingItem(String name, String car, Integer points) {
        this.name = name;
        this.car = car;
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
