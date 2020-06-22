package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.championship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.ChampionshipPilotListAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;

public class ChampionshipsDetailsPilotListFragment extends Fragment {

    private Championship championship;

    public ChampionshipsDetailsPilotListFragment(Championship championship) {
        super();
        this.championship = championship;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_championship_detail_pilot_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        if( this.getView() == null )
            return;

        ChampionshipPilotListAdapter adapter = new ChampionshipPilotListAdapter(this.championship.getSubscribedPilots(), this.getView().getContext());
        ListView lstSubscriptions = this.getView().findViewById(R.id.championships_details_pilot_list_lst);
        lstSubscriptions.setAdapter(adapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
