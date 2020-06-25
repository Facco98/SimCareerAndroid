package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.gson.Gson;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity.HomeActivity;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Notification;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.ChampionshipDifference;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.persistence.PersistenceManager;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private static final String TAG = "NotificationService";

    PersistenceManager persistenceManager;

    @Override
    public void onCreate() {

        this.persistenceManager = new PersistenceManager(getApplicationContext());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                User user = persistenceManager.getUser();
                Log.e(TAG, "RUNNING");
                if( user != null ){
                    Remote.loadChampionshipsUserIsSubscribed(user, this::handle);

                }

            }

            public void handle(Result<List<Championship>> result){

                Log.e(TAG, "STARTING HANDLING");
                if( result.getContent() != null ) {
                    List<Championship> cached = persistenceManager.getPreviousList();
                    Log.e(TAG, new Gson().toJson(cached));
                    Log.e(TAG, new Gson().toJson(result.getContent()));
                    List<Notification> notifications = new ArrayList<>();
                    List<ChampionshipDifference<String>> differences = new ArrayList<>();

                    for( Championship old : cached ){

                        for( Championship current : result.getContent() ){

                            List<ChampionshipDifference<String>> d = old.differences(current);
                            if( d != null )
                                differences.addAll(d);

                        }

                    }

                    Log.e(TAG, "" + differences.size() +" differences found");

                    for( ChampionshipDifference<String> difference : differences ){

                        Notification notification = new Notification();
                        switch(difference.field){

                            case LENGTH:
                                notification.setTitle("Aggiornamento campionato");
                                notification.setDescription("La durata del campionato" + difference.championshipName + " è stata modificata da " + difference.oldValue + " a " + difference.newValue +" gare.");
                                break;

                            case CALENDAR:
                                notification.setTitle("Aggiornamento calendario");
                                notification.setDescription("Il calendario del campionato " + difference.championshipName + " è stato modificato");

                            case CAR_LIST:
                                notification.setTitle("Aggiornamento delle auto disponibili");
                                notification.setDescription("La lista delle auto disponibili per il campionato " + difference.championshipName + " è stata modificata." +
                                        "La nuova lista è: " + difference.newValue);

                            case GAME_SETTINGS:
                                notification.setTitle("Aggiornamento impostazioni di gioco");
                                notification.setDescription("Le impostazioni di gioco del campionato " + difference.championshipName + " sono state modificate");
                            default:
                                break;
                        }

                        notifications.add(notification);

                    }
                    persistenceManager.addNotifications(notifications);
                    persistenceManager.updatePreviousList(result.getContent());
                    if( differences.size() > 0 ){
                        Log.e(TAG, "MUST SEND NOTIFICATION");
                        createAndSendNotification();
                    }
                } else if( result.getError() != null ){

                    result.getError().printStackTrace();

                }

            }
        };

        t.schedule(task, 5000,5000);

        return START_STICKY;

    }

    private void createAndSendNotification(){

        this.createNotificationChannel();
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("NOTIFICATION", "TRUE");
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.getApplicationContext());
        android.app.Notification userNotification = new NotificationCompat.Builder(this, "SimCarrerChannelID")
                .setContentTitle("Ci sono delle modifiche nei campionati a cui sei iscritto")
                .setSmallIcon(R.mipmap.ic_launcher).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true).setContentIntent(pi).build();
        notificationManager.notify(10, userNotification);

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("SimCarrerChannelID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
