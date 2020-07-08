package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity.LoginActivity;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity.SignUpActivity;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception.EmptyFieldException;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.persistence.PersistenceManager;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Result;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    public static final String KEY = "ProfileFragment";
    public static final String TAG = "ProfileFragment";

    private User user;
    private boolean userChanged = true;

    private EditText txtName;
    private EditText txtSurname;
    private EditText txtEmail;
    private EditText txtBirthDate;
    private EditText txtLiving;

    private EditText txtFavRace;
    private EditText txtFavCircuit;
    private EditText txtFavCar;

    private EditText txtHateCir;

    private EditText txtPassword;
    private EditText txtConfirmPassword;


    private ImageView imgAvatar;
    private Bitmap choosenImage;


    public ProfileFragment(@Nullable User user){

        this.user = user;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){

            if( this.userChanged ){

                FragmentManager fm = this.requireActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.detach(this);
                ft.attach(this);
                ft.commit();
                this.userChanged = false;
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.user == null)
            return inflater.inflate(R.layout.fragment_profile_not_logged, container, false);
        else {
            setHasOptionsMenu(true);
            return inflater.inflate(R.layout.fragment_profile_logged, container, false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.profile_logged_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if( item.getItemId() == R.id.action_logout ){

            new PersistenceManager(this.requireActivity().getApplicationContext()).setUser(null);
            this.user = null;
            FragmentManager fm = this.requireActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.detach(this);
            ft.attach(this);
            ft.commit();
        }
        return true;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        PersistenceManager.registerForUserChange(this, this::userChanged);

        if( this.user == null ){
            // User is not logged in
            Button btnSignUp = this.requireView().findViewById(R.id.profile_btn_register);
            Button btnLogin = this.requireView().findViewById(R.id.profile_btn_login);

            btnSignUp.setOnClickListener( this::notLoggedBtnSignUpClick );
            btnLogin.setOnClickListener( this::notLoggedBtnLoginClick );

        } else {

            Button btnUpdate = this.requireView().findViewById(R.id.profile_btn_update);
            Button btnAvatar = this.requireView().findViewById(R.id.profile_btn_avatar);

            btnUpdate.setOnClickListener(this::loggedBtnUpdateClick);
            btnAvatar.setOnClickListener(this::loggedBtnAvatarClick);

            this.assignFields();
            this.setupFields();
            Toolbar toolbar = this.requireActivity().findViewById(R.id.topAppBar);
            ((AppCompatActivity) this.requireActivity()).setSupportActionBar(toolbar);

        }
    }

    private void assignFields() {

        this.txtName = this.requireActivity().findViewById(R.id.profile_txt_name);
        this.txtSurname = this.requireActivity().findViewById(R.id.profile_txt_surname);
        this.txtEmail = this.requireActivity().findViewById(R.id.profile_txt_email);
        this.txtBirthDate = this.requireActivity().findViewById(R.id.profile_txt_bdate);
        this.txtLiving = this.requireActivity().findViewById(R.id.profile_txt_living);
        this.txtFavCar = this.requireActivity().findViewById(R.id.profile_txt_prefcar);
        this.txtFavCircuit = this.requireActivity().findViewById(R.id.profile_txt_prefcircuit);
        this.txtFavRace = this.requireActivity().findViewById(R.id.profile_txt_prefrace);
        this.txtHateCir = this.requireActivity().findViewById(R.id.profile_txt_hatecir);
        this.txtPassword = this.requireActivity().findViewById(R.id.profile_txt_password);
        this.txtConfirmPassword = this.requireActivity().findViewById(R.id.profile_txt_confirm_password);

        this.imgAvatar = this.requireView().findViewById(R.id.profile_img_avatar);
        this.txtName.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtSurname.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtEmail.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtBirthDate.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtLiving.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtFavCar.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtFavCircuit.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtFavRace.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtHateCir.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtPassword.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtConfirmPassword.setOnFocusChangeListener(this::hideKeyboardOnDeselect);



    }

    private void setupFields(){

        Log.i(TAG, "Reloading");

        this.txtName.setText(this.user.getName());
        this.txtSurname.setText(this.user.getSurname());
        this.txtEmail.setText(this.user.getEmail());
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY).format(this.user.getBirthDate());
        this.txtBirthDate.setText(date);
        this.txtLiving.setText(this.user.getLiving());
        this.txtFavCar.setText(this.user.getFavouriteCar());
        this.txtFavCircuit.setText(this.user.getFavouriteCircuit());
        this.txtFavRace.setText(this.user.getFavouriteRace());
        this.txtHateCir.setText(this.user.getHatedCircuit());
        this.imgAvatar.setImageBitmap(this.user.getAvatar());

    }


    private void notLoggedBtnLoginClick(View v){

        Intent i = new Intent( this.requireActivity(), LoginActivity.class );
        this.requireActivity().startActivity(i);
        //this.requireActivity().finish();


    }

    private void notLoggedBtnSignUpClick(View v){

        Intent i = new Intent( this.requireActivity(), SignUpActivity.class );
        this.requireActivity().startActivity(i);
        //this.requireActivity().finish();

    }

    private void loggedBtnUpdateClick(View v){

        Log.i(TAG, "Btn update did click");

        try {
            Date birthDate = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.ITALY).parse(this.txtBirthDate.getText().toString());
            String name = this.txtName.getText().toString().trim();
            String surname = this.txtSurname.getText().toString().trim();
            String email = this.txtEmail.getText().toString().trim();
            String living = this.txtLiving.getText().toString().trim();
            String favouriteCar = this.txtFavCar.getText().toString().trim();
            String favouriteRace = this.txtFavRace.getText().toString().trim();
            String favouriteCircuit = this.txtFavCircuit.getText().toString().trim();
            String hatedCircuit = this.txtHateCir.getText().toString().trim();
            String password = this.txtPassword.getText().toString().trim();
            String confirmPassword = this.txtConfirmPassword.getText().toString().trim();

            this.assertNotEmpty(name, "Nome");
            this.assertNotEmpty(surname, "Cognome");
            this.assertNotEmpty(email, "Indirizzo email");
            this.assertNotEmpty(living, "Residenza");
            this.assertNotEmpty(favouriteCar, "Auto preferita");
            this.assertNotEmpty(favouriteRace, "Numero di gara preferito");

            String psw = null;
            if( !password.equals("") && password.equals(confirmPassword) )
                psw = password;
            else if ( !password.equals("") && !password.equals(confirmPassword)){

                Toast.makeText(this.requireContext(), R.string.sign_up_password_mismatch, Toast.LENGTH_SHORT).show();
                return;

            }

            Log.e(TAG, "PASSWORD IS [" + psw + "]");

            String avatarString = null;
            if( this.choosenImage != null ){
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                this.choosenImage.compress(Bitmap.CompressFormat.PNG, 100, os);
                avatarString = Base64.encodeToString(os.toByteArray(), Base64.DEFAULT);
            }

            if (birthDate != null) {
                this.user = new User(email, name, surname, living, birthDate, favouriteCar, favouriteRace,
                        favouriteCircuit, hatedCircuit, avatarString);
                Remote.update(this.user, psw, this::updateHandler);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (EmptyFieldException e){
            Toast.makeText(this.requireContext(), e.fieldName + " è un campo obbligatorio", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateHandler(Result<Boolean> result){

        if( result.getError() != null ){

            Toast.makeText(this.requireContext(), R.string.profile_error_update, Toast.LENGTH_SHORT).show();
            result.getError().printStackTrace();

        } else if ( result.getContent() != null ){

            PersistenceManager pm = new PersistenceManager(this.requireActivity().getApplicationContext());
            pm.setUser(this.user);
            Toast.makeText(this.requireContext(), R.string.profile_update_success, Toast.LENGTH_SHORT).show();
            FragmentManager fm = this.requireActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.detach(this);
            ft.attach(this);
            ft.commit();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            if (selectedImage == null)
                return;

            try {
                InputStream is = this.requireActivity().getContentResolver().openInputStream(selectedImage);
                this.choosenImage = BitmapFactory.decodeStream(is);

                this.imgAvatar.setImageBitmap(this.choosenImage);
                this.imgAvatar.setBackgroundColor(Color.TRANSPARENT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void loggedBtnAvatarClick(View v){

        Log.i(TAG, "Btn avatar did click");

        Intent intent = new Intent();
        intent.setType("image/png");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE);


    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)this.requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void hideKeyboardOnDeselect(View v, boolean hasFocus){

        if( !hasFocus )
            this.hideKeyboard(v);

    }

    private void assertNotEmpty(String str, String field) throws EmptyFieldException{

        if( str.isEmpty() ){

            throw new EmptyFieldException(field);
        }

    }

    private void userChanged(User user){

        this.user = user;
        this.userChanged = true;

    }
}
