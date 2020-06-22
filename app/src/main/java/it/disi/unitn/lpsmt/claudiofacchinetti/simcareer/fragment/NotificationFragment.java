package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.NotificationListAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Notification;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.persistence.PersistenceManager;

import java.util.List;

public class NotificationFragment extends Fragment {

    public static final String KEY = "NotificationFragment";
    public static final String TAG = "NotificationFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        this.requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStackImmediate();

            }
        });

        if( this.getView() == null )
            return;

        PersistenceManager persistenceManager = new PersistenceManager(this.requireActivity().getApplicationContext());
        List<Notification> notifications = persistenceManager.getNotifications();
        ListView lstNotifications = this.getView().findViewById(R.id.notifications_list);
        NotificationListAdapter adapter = new NotificationListAdapter(notifications, this.getView().getContext());
        lstNotifications.setAdapter(adapter);

    }


}
