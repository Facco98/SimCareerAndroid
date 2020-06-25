package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.ranking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.ViewPagerAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.ranking.ChampionshipRanking;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Result;

public class RankingFragment extends Fragment {

    private Championship championship;

    public RankingFragment(Championship championship) {
        super();
        this.championship = championship;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking, container, false);
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
        Remote.loadRankingForChampionship(this.championship, this::updateUI);
    }

    private void updateUI(Result<ChampionshipRanking> result){

        TabLayout tabLayout = this.getView().findViewById(R.id.ranking_tabs);
        ViewPager viewPager = this.getView().findViewById(R.id.ranking_pager);

        if( result.getError() != null ){

            Toast.makeText(this.getView().getContext(), R.string.ranking_error, Toast.LENGTH_SHORT).show();
            result.getError().printStackTrace();

        } else if ( result.getContent() != null ) {

            ViewPagerAdapter adapter = new ViewPagerAdapter(this.getChildFragmentManager());
            adapter.addFragment(new PilotRankingFragment(result.getContent().getPilotRanking()), "Piloti");
            adapter.addFragment(new TeamRankingFragment(result.getContent().getTeamRanking()), "Team");
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
