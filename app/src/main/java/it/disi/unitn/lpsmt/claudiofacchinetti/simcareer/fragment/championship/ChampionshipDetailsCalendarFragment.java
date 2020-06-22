package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.championship;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.ChampionshipCalendarAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.CalendarItem;

import java.util.List;

public class ChampionshipDetailsCalendarFragment extends Fragment {

    private ListView lstCalendarList;
    private List<CalendarItem> dates;

    public ChampionshipDetailsCalendarFragment(@NonNull List<CalendarItem> dates) {

        super();
        this.dates = dates;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("DETAILS", "CREATING");
        return inflater.inflate(R.layout.fragment_championship_detail_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if( this.getView() == null )
            return;

        this.lstCalendarList = this.getView().findViewById(R.id.championship_details_calendar_lst_dates);
        ChampionshipCalendarAdapter adapter = new ChampionshipCalendarAdapter(this.dates, this.getView().getContext());
        this.lstCalendarList.setAdapter(adapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
