package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.adapter.GalleryGridAdapter;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Gallery;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Result;

import java.util.List;

public class GalleryFragment extends Fragment {

    public static final String TAG = "GalleryFragment";
    public static final String KEY = "GalleryFragment";

    private GridView lstGalleries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if( this.getView() == null )
            return;

        this.lstGalleries = this.getView().findViewById(R.id.gallery_list);

        this.requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStackImmediate();

            }
        });

        Remote.loadGalleries(this::galleriesLoadingCompletion);

    }

    private void galleriesLoadingCompletion(Result<List<Gallery>> result){

        if( result.getError() != null ){

            Log.e(TAG, result.getError().toString());
            Toast.makeText(this.getContext(), R.string.gallery_loading_error, Toast.LENGTH_SHORT).show();
            result.getError().printStackTrace();

        } else if (result.getContent() != null) {

            this.lstGalleries.setAdapter(new GalleryGridAdapter(this.getView().getContext(), result.getContent()));

        }

    }


}
