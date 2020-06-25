package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.SettingItem;

import java.util.List;

public class EditChampionshipSettingsAdapter extends BaseAdapter {

    private List<SettingItem> settings;
    private Context context;

    public EditChampionshipSettingsAdapter(List<SettingItem> settings, Context context) {
        this.settings = settings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.settings.size();
    }

    @Override
    public Object getItem(int position) {
        return this.settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView  == null ){

            convertView = LayoutInflater.from(this.context).inflate(R.layout.adapter_edit_championship_settings, null);

        }

        TextView lblKey = convertView.findViewById(R.id.edit_championship_lbl_key);
        EditText txtValue = convertView.findViewById(R.id.edit_championship_txt_value);

        SettingItem item = (SettingItem) this.getItem(position);
        lblKey.setText(item.getType());
        txtValue.setText(item.getValue());

        txtValue.setOnFocusChangeListener((view, focused) -> {

            if( !txtValue.isFocused() )
                item.setValue(txtValue.getText().toString());

        });

        return convertView;

    }
}
