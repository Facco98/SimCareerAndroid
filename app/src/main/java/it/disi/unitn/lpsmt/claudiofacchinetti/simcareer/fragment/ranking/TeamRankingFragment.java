package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.ranking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.TeamRankingAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.ranking.TeamRankingItem;

import java.util.Collections;
import java.util.List;

public class TeamRankingFragment extends Fragment {

    private List<TeamRankingItem> ranking;

    public TeamRankingFragment(List<TeamRankingItem> ranking) {
        this.ranking = ranking;
        Collections.sort(this.ranking, (a, b) -> b.getPoints() - a.getPoints());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //super.onViewCreated(view, savedInstanceState);
        if( this.getView() == null )
            return;

        ListView lstRanking = this.getView().findViewById(R.id.ranking_team_lst);
        TeamRankingAdapter adapter = new TeamRankingAdapter(this.ranking, this.getView().getContext());
        lstRanking.setAdapter(adapter);

    }

}
