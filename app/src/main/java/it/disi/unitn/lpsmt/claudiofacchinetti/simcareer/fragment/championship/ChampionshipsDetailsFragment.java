package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.championship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.ViewPagerAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.persistence.PersistenceManager;

import java.util.Comparator;

public class ChampionshipsDetailsFragment extends Fragment  {

    private Championship championship;

    public ChampionshipsDetailsFragment( @NonNull Championship championship ){

        super();
        this.championship = championship;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_championships_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((FragmentActivity) (getView().getContext())).getSupportFragmentManager().popBackStackImmediate();
            }
        };
        this.requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        this.updateUI();
    }

    private void updateUI(){

        this.championship.getCalendar().sort(Comparator.comparingInt(dateA -> Integer.parseInt(dateA.getSeq())));

        TabLayout tabLayout = this.getView().findViewById(R.id.championship_details_tabs);
        ViewPager viewPager = this.getView().findViewById(R.id.championship_details_pager);

        this.setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getChildFragmentManager());

        User user = new PersistenceManager(this.getActivity().getApplicationContext()).getUser();
        adapter.addFragment(new ChampionshipDetailsInfoFragment(this.championship), this.getResources().getString(R.string.championship_detail_info_tab));
        adapter.addFragment(new ChampionshipDetailsCalendarFragment(this.championship.getCalendar()),  this.getResources().getString(R.string.championship_detail_calendar_tab));
        adapter.addFragment(new ChampionshipsDetailsPilotListFragment(this.championship),  this.getResources().getString(R.string.championship_detail_pilot_list_tab));
        viewPager.setAdapter(adapter);

    }


}

