package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.NotificationFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.ProfileFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.championship.ChampionshipsListFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.GalleryFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.persistence.PersistenceManager;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.service.NotificationService;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private User user;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;

    private HashMap<String, Fragment> loadedFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.loadedFragments = new HashMap<>(4);
        PersistenceManager persistenceManager = new PersistenceManager(this.getApplicationContext());
        Log.i(TAG, "User Signed in: "+persistenceManager.isUserSignedIn());
        this.user=persistenceManager.getUser();
        this.setContentView(R.layout.activity_home);
        this.initUI();
        Intent i = new Intent(this, NotificationService.class);
        this.startService(i);
    }

    private void initUI(){

        this.bottomNavigationView = this.findViewById(R.id.home_navigation_bottom);
        this.fragmentContainer = this.findViewById(R.id.home_frg_container);

        this.bottomNavigationView.setOnNavigationItemSelectedListener(this::onItemSelected);
        this.bottomNavigationView.setSelectedItemId(R.id.home_tab_championships);

        ChampionshipsListFragment listFragment = new ChampionshipsListFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.home_frg_container, listFragment);

        ft.commit();

        this.loadedFragments.put(ChampionshipsListFragment.KEY, listFragment);


    }

    private boolean onItemSelected(@NonNull MenuItem item){



        int itemId = item.getItemId();
        if( itemId != this.bottomNavigationView.getSelectedItemId() ) {

            Fragment toLoad = null;

            Log.i(TAG, "Item ( " + item.getTitle().toString() + " ) selected");
            switch (item.getItemId()) {

                case R.id.home_tab_championships:
                    Log.i(TAG, "Entrato nella pagina dei campionati");
                    if( this.loadedFragments.get(ChampionshipsListFragment.KEY) == null )
                        this.loadedFragments.put(ChampionshipsListFragment.KEY, new ChampionshipsListFragment());
                    toLoad = this.loadedFragments.get( ChampionshipsListFragment.KEY );
                    break;

                case R.id.home_tab_gallery:
                    if( this.loadedFragments.get(GalleryFragment.KEY) == null )
                        this.loadedFragments.put(GalleryFragment.KEY, new GalleryFragment());
                    toLoad = this.loadedFragments.get( GalleryFragment.KEY );
                    break;

                case R.id.home_tab_notifications:
                    if( this.loadedFragments.get(NotificationFragment.KEY) == null )
                        this.loadedFragments.put(NotificationFragment.KEY, new NotificationFragment());
                    toLoad = this.loadedFragments.get( NotificationFragment.KEY );
                    break;

                case R.id.home_tab_profile:
                    if( this.loadedFragments.get(ProfileFragment.KEY) == null )
                        this.loadedFragments.put(ProfileFragment.KEY, new ProfileFragment());
                    toLoad = this.loadedFragments.get( ProfileFragment.KEY );
                    break;

            }

            if( toLoad != null ){
                Log.i(TAG, "Entrato nella galleria");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frg_container, toLoad);
                ft.commit();
            }

            return true;

        } else {

            return false;

        }

    }

}
