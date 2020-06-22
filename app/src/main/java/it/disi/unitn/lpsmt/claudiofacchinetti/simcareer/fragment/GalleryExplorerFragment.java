package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.MaterialToolbar;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.GalleryExplorerAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Gallery;

public class GalleryExplorerFragment extends Fragment {

    private Gallery gallery;

    public GalleryExplorerFragment(Gallery gallery) {

        this.gallery = gallery;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if( this.getView() == null )
            return;
        GridView grdGallery = this.getView().findViewById(R.id.gallery_list);
        grdGallery.setAdapter(new GalleryExplorerAdapter(this.getView().getContext(),this.gallery));
        MaterialToolbar toolbar = this.getView().findViewById(R.id.topAppBar2);
        toolbar.setTitle(this.gallery.getName());

    }
}
