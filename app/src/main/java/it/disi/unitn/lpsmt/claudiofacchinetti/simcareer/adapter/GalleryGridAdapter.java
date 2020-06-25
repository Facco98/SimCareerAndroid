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
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.GalleryExplorerFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Gallery;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;

import java.util.List;

public class GalleryGridAdapter extends BaseAdapter {

    private Context context;
    private List<Gallery> galleries;

    public GalleryGridAdapter(Context context, List<Gallery> galleries) {

        this.galleries = galleries;
        this.context = context;

    }

    @Override
    public int getCount() {
        return this.galleries.size();
    }

    @Override
    public Object getItem(int position) {
        return this.galleries.get(position);
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
        String fullPath = ((Gallery) this.getItem(position)).getImgFullPath(0);
        Remote.loadBitmapFromAssets(fullPath, (result) ->{

            if( result.getContent() != null )
                imgThumb.setImageBitmap(result.getContent());
            else if( result.getError() != null)
                result.getError().printStackTrace();

        });

        View finalConvertView = convertView;
        convertView.setOnClickListener((view) -> {

            FragmentTransaction ft = ((FragmentActivity)(finalConvertView.getContext())).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.home_frg_container, new GalleryExplorerFragment((Gallery) this.getItem(position)));
            ft.addToBackStack(null);
            ft.commit();
        });

        return convertView;


    }
}
