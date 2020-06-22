package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.PilotSubscriptionItem;

import java.util.List;

public class ChampionshipPilotListAdapter extends BaseAdapter {

    private List<PilotSubscriptionItem> pilotList;
    private Context context;

    public ChampionshipPilotListAdapter(List<PilotSubscriptionItem> pilotList, Context context) {
        this.pilotList = pilotList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.pilotList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.pilotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null )
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_championship_detail_pilot_list, null);


        TextView txtName = convertView.findViewById(R.id.championship_detail_pilot_list_txt_name);
        TextView txtTeam = convertView.findViewById(R.id.championship_detail_pilot_list_txt_team);
        TextView txtCar = convertView.findViewById(R.id.championship_detail_pilot_list_txt_car);

        PilotSubscriptionItem item = (PilotSubscriptionItem) this.getItem(position);
        StringBuilder sb = new StringBuilder();
        sb.append(this.context.getResources().getString(R.string.championship_details_pilot_list_team_pre)).append(" ");
        sb.append(item.getTeam());
        txtTeam.setText(sb.toString());

        sb = new StringBuilder();
        sb.append(this.context.getResources().getString(R.string.championship_details_pilot_list_car_pre)).append(" ");
        sb.append(item.getCar());
        txtCar.setText(sb.toString());

        txtName.setText(item.getName());



        return convertView;


    }
}
