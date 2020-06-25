package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception.InvalidCarException;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.util.Constants;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Championship implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("nome")
    private String name;
    @SerializedName("logo")
    private String logo;
    @SerializedName("calendario")
    private List<CalendarItem> calendar;
    @SerializedName("impostazioni-gioco")
    private List<SettingItem> gameSettings;
    @SerializedName("lista-auto")
    private String carList;
    @SerializedName("piloti-iscritti")
    private List<PilotSubscriptionItem> subscribedPilots;

    public static class Wrapper {

        @SerializedName("campionati")
        private List<Championship> championships;

        public Wrapper() {
        }

        public Wrapper(List<Championship> championships) {
            this.championships = championships;
        }

        public List<Championship> getChampionships() {
            return championships;
        }

        public void setChampionships(List<Championship> championships) {
            this.championships = championships;
        }

    }

    public Championship() {

    }

    public Championship(String id, String name, String logo, List<CalendarItem> calendar, List<SettingItem> gameSettings, String carList, List<PilotSubscriptionItem> subscribedPilots) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.calendar = calendar;
        this.gameSettings = gameSettings;
        this.carList = carList;
        this.subscribedPilots = subscribedPilots;
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

    public List<CalendarItem> getCalendar() {
        return calendar;
    }

    public void setCalendar(List<CalendarItem> calendar) {
        this.calendar = calendar;
    }

    public List<SettingItem> getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(List<SettingItem> gameSettings) {
        this.gameSettings = gameSettings;
    }

    public String getCarListAsString() {
        return carList;
    }

    public void setCarList(String carList) {
        this.carList = carList;
    }

    public List<PilotSubscriptionItem> getSubscribedPilots() {
        return subscribedPilots;
    }

    public void setSubscribedPilots(List<PilotSubscriptionItem> subscribedPilots) {
        this.subscribedPilots = subscribedPilots;
    }

    public String getDescription(String starting){

        StringBuilder sb = new StringBuilder();
        sb.append(starting);
        sb.append(" ");
        for( int i = 0; i < this.calendar.size() - 1; i++ ) {
            sb.append(this.calendar.get(i).getCircuit());
            sb.append(", ");
        }
        sb.append(this.calendar.get(this.calendar.size()-1).getCircuit());

        return sb.toString();
    }

    public Boolean isOver() throws ParseException {

        Boolean over = true;
        Date now = new Date();
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        for( CalendarItem item : this.calendar ){

            Date raceDate = parser.parse(item.getDate());
            if (raceDate != null) {
                over = raceDate.before(now);
            }

        }
        return over;

    }

    public List<String> getCarList(){

        List<String> list = new ArrayList<>();
        String[] carArray = this.getCarListAsString().split(Constants.CAR_LIST_SEPARATOR);
        Collections.addAll(list, carArray);
        return list;

    }

    @Nullable
    public Boolean userPartecipates(@Nullable User user) throws ParseException {

        if( user == null )
            return null;

        if( this.isOver() )
            return null;

        String name = user.getName();
        String surname = user.getSurname();

        String query = name + " " + surname;

        for( PilotSubscriptionItem subscriptionItem : this.subscribedPilots ){

            if( subscriptionItem.getName().equals(query) )
                return true;

        }

        return false;

    }

    public boolean addSubscription( @NonNull  PilotSubscriptionItem subscriptionItem ) throws InvalidCarException {

        List<String> carVariants = new ArrayList<String>();
        carVariants.add(subscriptionItem.getCar());
        carVariants.add(subscriptionItem.getCar().toLowerCase());
        carVariants.add(subscriptionItem.getCar().toUpperCase());

        boolean carAllowed = false;

        for( String variant : carVariants )
            carAllowed = carAllowed || this.getCarList().contains(variant);

        if( !carAllowed )
            throw new InvalidCarException();

        return this.subscribedPilots.add(subscriptionItem);

    }

    public void removeUserSubscription(@NonNull User user){

        String query = user.getName() + " " + user.getSurname();
        PilotSubscriptionItem item = null;
        Iterator<PilotSubscriptionItem> it = this.subscribedPilots.iterator();
        while( it.hasNext() && item == null ){

            PilotSubscriptionItem current = it.next();
            if( current.getName().equals(query) )
                item = current;

        }

        if( item != null )
            this.subscribedPilots.remove(item);

    }

    public List<ChampionshipDifference<String>> differences (@NonNull Championship c) {

        if( !c.getId().equals(this.getId()) )
            return null;

        List<ChampionshipDifference<String>> differences = new ArrayList<>();
        List<CalendarItem> cCalcendar = c.getCalendar();
        Collections.sort(this.calendar, (a,b) -> Integer.parseInt(a.getSeq()) - Integer.parseInt(b.getSeq()));
        Collections.sort(cCalcendar, (a,b) -> Integer.parseInt(a.getSeq()) - Integer.parseInt(b.getSeq()));
        if( this.calendar.size() != cCalcendar.size() ){

            differences.add(new ChampionshipDifference<String>(this.getName(), Field.LENGTH, ""+calendar.size(), ""+cCalcendar.size()));

        } else{

            boolean end = false;
            for (int i = 0; i < calendar.size() && !end; i++ ) {

                CalendarItem first = calendar.get(i);
                CalendarItem second = cCalcendar.get(i);
                if (!first.getDate().equals(second.getDate())) {
                    differences.add(new ChampionshipDifference<String>(this.getName(), Field.CALENDAR, first.getDate(), second.getDate()));
                    end = true;
                }
                if (!first.getCircuit().equals(second.getCircuit())) {
                    differences.add(new ChampionshipDifference<String>(this.getName(),Field.CIRCUIT, first.getCircuit(), second.getCircuit()));
                    end = true;
                }
            }

        }

        if( !this.carList.equals(c.getCarListAsString()) ){

            differences.add(new ChampionshipDifference<String>(this.getName(),Field.CAR_LIST, this.carList, c.getCarListAsString()));

        } else{

            List<String> carList = this.getCarList();
            List<String> cCarList = c.getCarList();
            Collections.sort(carList);
            Collections.sort(cCarList);
            boolean end = false;
            for (int i = 0; i < this.getCarList().size() && !end; i++ ) {

                if( !carList.get(i).equals(cCarList.get(i)) ){
                    differences.add(new ChampionshipDifference<>(this.getName(),Field.CAR_LIST, this.getCarListAsString(), c.getCarListAsString()));
                    end = true;
                }

            }

        }

        if( this.gameSettings.size() != c.getGameSettings().size() ){

            differences.add(new ChampionshipDifference<String>(this.getName(),Field.GAME_SETTINGS, ""+this.getGameSettings().size(), ""+c.getGameSettings().size()));

        } else{

            List<SettingItem> gameSettings = this.getGameSettings();
            List<SettingItem> cGameSettings = c.getGameSettings();
            Collections.sort(gameSettings, (a,b) -> a.getType().compareTo(b.getType()));
            Collections.sort(cGameSettings, (a,b) -> a.getType().compareTo(b.getType()));
            boolean end = false;
            for (int i = 0; i < gameSettings.size() && !end; i++ ) {

                for( int j = 0; j < gameSettings.size() && !end; j++ )
                    if( gameSettings.get(i).getType().equals(cGameSettings.get(j).getType()) &&
                            !gameSettings.get(i).getValue().equals(cGameSettings.get(j).getValue())){
                        differences.add(new ChampionshipDifference<String>(this.getName(), Field.GAME_SETTINGS, this.getCarListAsString(), c.getCarListAsString()));
                        end = true;
                    }

            }

        }

        return differences;


    }

}