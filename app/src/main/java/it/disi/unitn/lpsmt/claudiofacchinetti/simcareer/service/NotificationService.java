package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
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
                //TODO: Implement the function to generate the notifications
            }
        };

        t.schedule(task, 1000,1000);

        return START_STICKY;

    }
}
