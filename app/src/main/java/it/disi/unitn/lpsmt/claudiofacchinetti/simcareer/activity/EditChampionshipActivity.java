package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.EditChampionshipSettingsAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;

public class EditChampionshipActivity extends AppCompatActivity {

    private Championship championship;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_edit_championship);
        Intent i = this.getIntent();

        if( i != null && i.hasExtra("champ") ){

            this.championship = (Championship) i.getSerializableExtra("champ");

        }

        if( this.championship == null )
            this.finish();


        EditText txtName = this.findViewById(R.id.edit_championship_txt_name);
        txtName.setText(this.championship.getName());

        EditText txtCars = this.findViewById(R.id.edit_championship_txt_cars);
        txtCars.setText(this.championship.getCarListAsString());

        ListView lstSettings = this.findViewById(R.id.edit_championship_lst_settings);
        EditChampionshipSettingsAdapter a = new EditChampionshipSettingsAdapter(this.championship.getGameSettings(), this);

        lstSettings.setAdapter(a);

        MaterialButton btnConfirm = this.findViewById(R.id.edit_championship_btn_confirm);
        btnConfirm.setOnClickListener((view) -> {
            
            btnConfirm.requestFocus();
            Remote.editChampionship(this.championship, result -> this.finish());

        });

    }
}
