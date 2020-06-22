package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.championship;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.ChampionshipsAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;

public class ChampionshipsListFragment extends Fragment {

    public static final String KEY = "ChampionshipsList";

    private static final String TAG = "ChampionshipsListFragment";

    private ListView lstChampionshipsList;
    private SwipeRefreshLayout swipeLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_championships_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if( this.getView() != null )
            this.lstChampionshipsList = this.getView().findViewById(R.id.championships_list);
        else
            return;

        this.swipeLayout = this.getView().findViewById(R.id.championships_list_swipe);
        swipeLayout.setOnRefreshListener(this::updateList);
        this.updateList();


    }

    private void updateList(){

        Remote.getChampionshipsList((result) -> {

            if( result.getError() != null ){

                Toast.makeText(this.getView().getContext(), "Errore nel recupero della lista dei campionati", Toast.LENGTH_SHORT).show();
                Log.e(TAG, result.getError().toString());

            } else if( result.getContent() != null) {

                ChampionshipsAdapter adapter = new ChampionshipsAdapter(result.getContent(), this.getView().getContext());
                this.lstChampionshipsList.setAdapter(adapter);

            }

        });
        this.swipeLayout.setRefreshing(false);

    }
}
