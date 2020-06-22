package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    private Bitmap avatar;


    public User(@NonNull String email, @NonNull String name, @NonNull String surname,
                @NonNull String living, @NonNull Date birthDate, @NonNull String favouriteCar, @NonNull String favouriteRace, @NonNull String favouriteCircuit,
                @NonNull String hatedCircuit, @Nullable Bitmap avatar){

        this.email = email;
        this.name = name;
        this.surname = surname;
        this.living = living;
        this.favouriteCar = favouriteCar;
        this.favouriteCircuit = favouriteCircuit;
        this.favouriteRace = favouriteRace;
        this.hatedCircuit = hatedCircuit;
        this.avatar = avatar;
        this.birthDate = birthDate;

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
        return avatar;
    }

    public String getEmail() {
        return email;
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
