package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarItem implements Serializable {

    @SerializedName("seq")
    private String seq;
    @SerializedName("data")
    private String date;
    @SerializedName("circuito")
    private String circuit;

    public CalendarItem() {
    }

    public CalendarItem(String seq, String date, String circuit) {
        this.seq = seq;
        this.date = date;
        this.circuit = circuit;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public Date getParsedDate() throws ParseException {

        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
        return parser.parse(this.getDate());

    }

    public Boolean isPast() throws ParseException {

        return this.getParsedDate().before(new Date());

    }
}
