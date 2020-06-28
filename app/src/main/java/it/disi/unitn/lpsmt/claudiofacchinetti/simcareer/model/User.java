package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;

import java.util.Date;


public class User {

    private String email;
    private String name;
    private String surname;
    private String living;
    private String favouriteCar;
    private String favouriteRace;
    private String favouriteCircuit;
    private String hatedCircuit;
    private Date birthDate;

    private String avatarString;

    public User(@NonNull String email, @NonNull String name, @NonNull String surname,
                @NonNull String living, @NonNull Date birthDate, @NonNull String favouriteCar, @NonNull String favouriteRace, @NonNull String favouriteCircuit,
                @NonNull String hatedCircuit, @Nullable String avatarString){

        this.email = email;
        this.name = name;
        this.surname = surname;
        this.living = living;
        this.favouriteCar = favouriteCar;
        this.favouriteCircuit = favouriteCircuit;
        this.favouriteRace = favouriteRace;
        this.hatedCircuit = hatedCircuit;
        this.birthDate = birthDate;
        this.avatarString = avatarString;
    }

    public String getLiving() {
        return living;
    }

    public String getFavouriteCar() {
        return favouriteCar;
    }

    public String getFavouriteRace() {
        return favouriteRace;
    }

    public String getFavouriteCircuit() {
        return favouriteCircuit;
    }

    public String getHatedCircuit() {
        return hatedCircuit;
    }

    public Bitmap getAvatar() {

        Bitmap res = null;
        if( this.avatarString != null ){

            byte[] decodedBytes = Base64.decode(this.avatarString, Base64.DEFAULT);
            res = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        }
        return res;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setLiving(String living) {
        this.living = living;
    }

    public void setFavouriteCar(String favouriteCar) {
        this.favouriteCar = favouriteCar;
    }

    public void setFavouriteRace(String favouriteRace) {
        this.favouriteRace = favouriteRace;
    }

    public void setFavouriteCircuit(String favouriteCircuit) {
        this.favouriteCircuit = favouriteCircuit;
    }

    public void setHatedCircuit(String hatedCircuit) {
        this.hatedCircuit = hatedCircuit;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }


    public String getAvatarString() {
        return avatarString;
    }

    public void setAvatarString(String avatarString) {
        this.avatarString = avatarString;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }
}
