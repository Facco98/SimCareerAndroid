package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.gson.Gson;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity.EditChampionshipActivity;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;

import java.util.List;

public class EditListAdapter extends BaseAdapter {

    private List<Championship> championships;
    private Context context;


    public EditListAdapter(List<Championship> championships, Context context){

        this.championships = championships;
        this.context = context;

    }

    @Override
    public int getCount() {
        return this.championships.size();
    }

    @Override
    public Object getItem(int position) {
        return this.championships.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null )
            convertView = LayoutInflater.from(this.context).inflate(R.layout.adapter_edit_list, null);

        TextView name = convertView.findViewById(R.id.edit_list_lbl_name);
        Championship c = (Championship) this.getItem(position);
        name.setText(c.getName());
        View finalConvertView = convertView;
        convertView.setOnClickListener(view -> {

            Intent i = new Intent(finalConvertView.getContext(), EditChampionshipActivity.class );
            i.putExtra("champ", c);
            Log.e("LISTADAP", new Gson().toJson(c));
            finalConvertView.getContext().startActivity(i);


        });
        return convertView;

    }
}
