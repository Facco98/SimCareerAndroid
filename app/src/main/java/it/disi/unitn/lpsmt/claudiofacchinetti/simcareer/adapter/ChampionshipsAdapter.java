package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.championship.ChampionshipsDetailsFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;


public class ChampionshipsAdapter extends BaseAdapter {

    private List<Championship> values;
    private Context context;

    public ChampionshipsAdapter(@NonNull List<Championship> values, @NonNull Context context){

        this.values = values;
        this.context = context;

    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    @Override
    public Object getItem(int position) {
        return this.values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null )
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_championships_list, null);

        Championship elem = (Championship) this.getItem(position);
        this.setUI(convertView, elem);

        MaterialCardView cardChampionship = convertView.findViewById(R.id.championships_row_card);
        View finalConvertView = convertView;
        cardChampionship.setOnClickListener( (view ) ->{

            FragmentTransaction ft = ((FragmentActivity) (finalConvertView.getContext())).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.home_frg_container, new ChampionshipsDetailsFragment(elem));
            ft.addToBackStack(null);
            ft.commit();

        });

        return convertView;

    }

    private void setUI(@NonNull View convertView, @NonNull Championship elem){

        ImageView imgChampionshipThumb = convertView.findViewById(R.id.championships_row_image);
        TextView txtTitle = convertView.findViewById(R.id.championships_row_title);
        TextView txtDescription = convertView.findViewById(R.id.championships_row_description);
        Chip chipOver = convertView.findViewById(R.id.championships_row_chip);

        txtTitle.setText(elem.getName());
        String description = elem.getDescription(this.context.getResources().getString(R.string.championships_description_starting));
        txtDescription.setText(description);

        try {

            if( elem.isOver() ) {
                chipOver.setChipBackgroundColorResource(R.color.championships_chip_over_background);
                chipOver.setText(R.string.championships_chip_over_text);
            }
            else {
                chipOver.setChipBackgroundColorResource(R.color.championships_chip_ongoing_background);
                chipOver.setText(R.string.championships_chip_ongoing_text);
            }

        } catch (ParseException e) {
            chipOver.setText(R.string.championships_error_parsing);
            e.printStackTrace();
        }

        if( elem.getLogo().startsWith("http://") ){

            Remote.getBitmapFromURL(elem.getLogo(), (result) ->{

                if( result.getContent() != null )
                    imgChampionshipThumb.setImageBitmap(result.getContent());
                else if( result.getError() != null)
                    result.getError().printStackTrace();

            });

        } else {
            try {
                Bitmap img = BitmapFactory.decodeStream(this.context.getAssets().open(elem.getLogo()));
                imgChampionshipThumb.setImageBitmap(img);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
