package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.FullViewFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.championship.ChampionshipsDetailsFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Gallery;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;

public class GalleryExplorerAdapter extends BaseAdapter {

    private Gallery gallery;
    private Context context;
    private boolean viewOpen;

    public GalleryExplorerAdapter(Context context, Gallery gallery) {

        this.gallery = gallery;
        this.context = context;
        this.viewOpen = false;

    }

    @Override
    public int getCount() {
        return this.gallery.count();
    }

    @Override
    public Object getItem(int position) {
        return this.gallery.getThumbFullPath(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null )
            convertView = LayoutInflater.from(this.context).inflate(R.layout.adapter_gallery_grid, null);

        ImageView imgThumb = convertView.findViewById(R.id.gallery_collection_item_img);
        String fullPath = (String) this.getItem(position);
        View finalConvertView = convertView;
        imgThumb.setOnClickListener((view) -> {

            FragmentTransaction ft = ((FragmentActivity) (finalConvertView.getContext())).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.home_frg_container, new FullViewFragment(this.gallery, position));
            ft.addToBackStack(null);
            ft.commit();

        });
        Remote.loadBitmapFromAssets(fullPath, (result) ->{

            if( result.getContent() != null )
                imgThumb.setImageBitmap(result.getContent());
            else if( result.getError() != null)
                result.getError().printStackTrace();

        });

        return convertView;


    }
}
