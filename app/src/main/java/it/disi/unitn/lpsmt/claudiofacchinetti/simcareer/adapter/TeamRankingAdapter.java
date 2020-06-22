package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.ranking.PilotRankingItem;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.ranking.TeamRankingItem;

import java.util.List;

public class TeamRankingAdapter extends BaseAdapter {

    private List<TeamRankingItem> ranking;
    private Context context;

    public TeamRankingAdapter(List<TeamRankingItem> ranking, Context context) {
        this.ranking = ranking;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.ranking.size();
    }

    @Override
    public Object getItem(int position) {
        return this.ranking.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null )
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_ranking_pilot, null);

        MaterialCardView card = convertView.findViewById(R.id.ranking_pilot_row);
        //card.setOnClickListener((v) -> Log.e("VIEW", "Position: " + position));
        if( position + 1 == 1 )
            card.setBackgroundResource(R.color.gold);
        else if ( position + 1 == 2 )
            card.setBackgroundResource(R.color.silver);
        else if ( position + 1 == 3 )
            card.setBackgroundResource(R.color.bronze);
        else
            card.setBackgroundResource(Color.TRANSPARENT);

        TextView txtPosition = convertView.findViewById(R.id.ranking_pilot_txt_position);
        TextView txtName = convertView.findViewById(R.id.ranking_pilot_txt_name);
        TextView txtCar = convertView.findViewById(R.id.ranking_pilot_txt_car);

        TeamRankingItem item = (TeamRankingItem) this.getItem(position);
        txtPosition.setText(""+(position+1));

        StringBuilder builder = new StringBuilder();
        builder.append(item.getName()).append( " - " );
        builder.append(item.getPoints()).append(" ").append(this.context.getResources().getString(R.string.pilot_ranking_points));
        txtName.setText(builder.toString());

        builder = new StringBuilder();
        builder.append(this.context.getResources().getString(R.string.championship_details_pilot_list_car_pre)).append(" ");
        builder.append(item.getCar());
        txtCar.setText(builder.toString());




        return convertView;
    }
}
