package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Notification;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;

import java.util.Arrays;
import java.util.List;

public class PersistenceManager {

    private final static String TAG = "PersistenceManager";
    private static final String USER_KEY = "USER";
    private static final String PREFERENCES_NAME = "PERSISTANCE";
    private static final String NOTIFICATIONS_KEY = "NOTIFICATIONS";

    private final Context context;

    public PersistenceManager(@NonNull Context context){

        this.context = context;

    }

    public Boolean isUserSignedIn(){

        SharedPreferences preferences = this.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String data = null;
        return ( preferences != null && (data = preferences.getString(USER_KEY, null)) != null);

    }

    @Nullable
    public User getUser() {

        User result = null;
        if( this.isUserSignedIn() ){

            SharedPreferences preferences = this.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            result = new Gson().fromJson(preferences.getString(USER_KEY, ""), User.class);

        }
        return result;

    }

    public void setUser(@Nullable User user) {

        SharedPreferences preferences = this.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        if( preferences != null ) {
            SharedPreferences.Editor editor = preferences.edit();
            if (user == null) {
                editor.remove(USER_KEY);
            } else {
                String userJSON = new Gson().toJson(user);
                editor.putString(USER_KEY, userJSON);
            }
            editor.commit();
        }
    }

    @NonNull
    public List<Notification> getNotifications(){

        Notification[] notifications;
        SharedPreferences preferences = this.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        notifications = new Gson().fromJson(preferences.getString(NOTIFICATIONS_KEY, "[]"), Notification[].class);
        return Arrays.asList(notifications);

    }

    public void addNotification(Notification notification){

        List<Notification> alreadySaved = this.getNotifications();
        alreadySaved.add(notification);
        SharedPreferences preferences = this.context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NOTIFICATIONS_KEY, gson.toJson(alreadySaved));
        editor.apply();


    }


}
