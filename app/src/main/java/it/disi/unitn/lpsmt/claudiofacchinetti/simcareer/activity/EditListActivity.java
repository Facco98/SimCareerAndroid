package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity;

import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.EditListAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;

import java.util.List;

public class EditListActivity extends AppCompatActivity {

    private List<Championship> championships;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_edit_list);
        ListView lst = this.findViewById(R.id.edit_list_lst_championships);
        Remote.getChampionshipsList(result -> {

            if( result.getError() != null )
                result.getError().printStackTrace();
            else if ( result.getContent() != null ) {
                this.championships = result.getContent();
                lst.setAdapter(new EditListAdapter(result.getContent(), this));
            }

        });

    }

    @Override
    protected void onResume() {

        ListView lst = this.findViewById(R.id.edit_list_lst_championships);
        Remote.getChampionshipsList(result -> {

            if( result.getError() != null )
                result.getError().printStackTrace();
            else if ( result.getContent() != null ) {
                this.championships = result.getContent();
                lst.setAdapter(new EditListAdapter(result.getContent(), this));
            }

        });

        super.onResume();


    }
}
