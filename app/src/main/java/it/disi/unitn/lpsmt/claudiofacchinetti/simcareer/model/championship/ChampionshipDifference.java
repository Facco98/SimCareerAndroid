package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship;

public class ChampionshipDifference<T> {

    public final String championshipName;
    public final Field field;
    public final T oldValue;
    public final T newValue;

    public ChampionshipDifference(String championshipName, Field field, T oldValue, T newValue) {
        this.field = field;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.championshipName = championshipName;
    }
}