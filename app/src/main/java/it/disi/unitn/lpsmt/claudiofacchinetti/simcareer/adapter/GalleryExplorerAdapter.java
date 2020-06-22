package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment.GalleryExplorerFragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Gallery;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;

public class GalleryExplorerAdapter extends BaseAdapter {

    private Gallery gallery;
    private Context context;

    public GalleryExplorerAdapter(Context context, Gallery gallery) {

        this.gallery = gallery;
        this.context = context;

    }

    @Override
    public int getCount() {
        return this.gallery.count();
    }

    @Override
    public Object getItem(int position) {
        return this.gallery.getImgFullPath(position);
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
        Remote.loadBitmapFromAssets(fullPath, (result) ->{

            if( result.getContent() != null )
                imgThumb.setImageBitmap(result.getContent());
            else if( result.getError() != null)
                result.getError().printStackTrace();

        });

        return convertView;


    }
}
