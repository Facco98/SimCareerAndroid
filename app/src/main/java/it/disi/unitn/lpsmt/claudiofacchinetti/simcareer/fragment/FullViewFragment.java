package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Gallery;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

public class FullViewFragment extends Fragment {

    private Gallery gallery;
    private int position;

    private ImageView imgFullView;


    public FullViewFragment(Gallery gallery, int position) {

        this.gallery = gallery;
        this.position = position;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_full_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if( this.getView() == null )
            return;

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((FragmentActivity) (requireContext())).getSupportFragmentManager().popBackStackImmediate();
            }
        };
        this.requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);


        this.imgFullView = this.requireView().findViewById(R.id.gallery_img_full_view);
        this.updateImage();

        this.imgFullView.setOnClickListener(v -> {
            position ++;
            updateImage();
        });

        this.imgFullView.setOnLongClickListener(v -> {

            this.imgFullView.showContextMenu();
            return true;

        });

        this.registerForContextMenu(this.imgFullView);

    }


    private void updateImage() {

        try {
            String fullPath = this.gallery.getImgFullPath(this.position);
        } catch ( IndexOutOfBoundsException ex ){
            if( this.position >= this.gallery.count() )
                this.position = this.gallery.count() -1;
            else if ( this.position < 0 )
                this.position = 0;
        } finally {
            String fullPath = this.gallery.getImgFullPath(this.position);
            Remote.loadBitmapFromAssets(fullPath, res -> {

                this.imgFullView.setImageBitmap(res.getContent());

            });
        }


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.img_share_btn ){

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            File outputDir = this.requireContext().getCacheDir(); // context being the Activity pointer
            try {
                File outputFile = File.createTempFile("temp", ".png", outputDir);
                FileOutputStream fos = new FileOutputStream(outputFile);
                ((BitmapDrawable) this.imgFullView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);
                System.out.println("URI: "+outputFile.getAbsolutePath());
                Uri uri = FileProvider.getUriForFile(this.requireContext(),this.requireContext().getPackageName(),outputFile);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                System.out.println("URI: "+outputFile.getAbsolutePath());
                startActivity(Intent.createChooser(intent, "Condividi immagine"));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;


        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.requireActivity().getMenuInflater();
        inflater.inflate(R.menu.img_menu, menu);

    }
}
