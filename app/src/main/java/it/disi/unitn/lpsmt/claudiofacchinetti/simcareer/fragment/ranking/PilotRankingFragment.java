package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.ranking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.PilotRankingAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.ranking.PilotRankingItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PilotRankingFragment extends Fragment {

    private List<PilotRankingItem> ranking;

    public PilotRankingFragment(List<PilotRankingItem> ranking) {
        this.ranking = ranking;
        Collections.sort(this.ranking, (a,b) -> b.getPoints() - a.getPoints());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ranking_pilot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //super.onViewCreated(view, savedInstanceState);
        if( this.getView() == null )
            return;

        ListView lstRanking = this.getView().findViewById(R.id.ranking_pilot_lst);
        PilotRankingAdapter adapter = new PilotRankingAdapter(this.ranking, this.getView().getContext());
        lstRanking.setAdapter(adapter);

    }

}
