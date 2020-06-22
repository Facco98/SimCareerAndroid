package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.CalendarItem;

import java.util.List;

public class ChampionshipCalendarAdapter extends BaseAdapter {

    private static final String TAG = "CalendarAdapter";

    private List<CalendarItem> dates;
    private Context context;

    public ChampionshipCalendarAdapter(@NonNull List<CalendarItem> dates, @NonNull Context context) {
        this.dates = dates;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.dates.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.dates.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null )
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_championship_detail_calendar_list, null);

        //MaterialCardView card = convertView.findViewById(R.id.championships_details_calendar_row);
        TextView lblDate = convertView.findViewById(R.id.championship_details_calendar_lbl_date);
        CalendarItem race = (CalendarItem) this.getItem(position);
        lblDate.setText(race.getCircuit() + " - " + race.getDate());
        lblDate.setBackgroundResource(R.color.design_default_color_error);
        try {
            if( race.isPast() )
                //Log.d(TAG, "YES");
                lblDate.setBackgroundResource(R.color.championships_chip_over_background);
            else
                //Log.d(TAG, "NO");
                lblDate.setBackgroundResource(R.color.championships_chip_ongoing_background);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }
}
