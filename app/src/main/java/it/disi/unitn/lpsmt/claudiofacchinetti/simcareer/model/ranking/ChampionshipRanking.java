package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.ranking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChampionshipRanking {

    @SerializedName("id")
    private String id;
    @SerializedName("nome")
    private String name;
    @SerializedName("logo")
    private String logo;
    @SerializedName("classifica-piloti")
    private List<PilotRankingItem> pilotRanking;
    @SerializedName("classifica-team")
    private List<TeamRankingItem> teamRanking;

    public static class Wrapper {

        @SerializedName("campionati")
        private List<ChampionshipRanking> championships;

        public Wrapper() {
        }

        public Wrapper(List<ChampionshipRanking> championships) {
            this.championships = championships;
        }

        public List<ChampionshipRanking> getChampionshipRankings() {
            return championships;
        }

        public void setChampionships(List<ChampionshipRanking> championships) {
            this.championships = championships;
        }

    }

    public ChampionshipRanking(String id, String name, String logo, List<PilotRankingItem> pilotRanking, List<TeamRankingItem> teamRanking){
        this.id = id;
        this.logo = logo;
        this.pilotRanking = pilotRanking;
        this.teamRanking = teamRanking;
    }

    public ChampionshipRanking() {

        this.id = null;
        this.logo = null;
        this.pilotRanking = null;
        this.teamRanking = null;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<PilotRankingItem> getPilotRanking() {
        return pilotRanking;
    }

    public void setPilotRanking(List<PilotRankingItem> pilotRanking) {
        this.pilotRanking = pilotRanking;
    }

    public List<TeamRankingItem> getTeamRanking() {
        return teamRanking;
    }

    public void setTeamRanking(List<TeamRankingItem> teamRanking) {
        this.teamRanking = teamRanking;
    }
}
