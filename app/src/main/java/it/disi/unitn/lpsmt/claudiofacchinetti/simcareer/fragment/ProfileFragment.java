package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity.LoginActivity;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity.SignUpActivity;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;

import java.util.Locale;

public class ProfileFragment extends Fragment {

    public static final String KEY = "ProfileFragment";
    public static final String TAG = "ProfileFragment";

    private User user;

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


    public ProfileFragment(@Nullable User user){

        this.user = user;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if( this.user == null )
            return inflater.inflate(R.layout.fragment_profile_not_logged, container, false);
        else
            return inflater.inflate(R.layout.fragment_profile_logged, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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

            this.imgAvatar = this.requireView().findViewById(R.id.profile_img_avatar);
            this.imgAvatar.setImageBitmap(this.user.getAvatar());

            this.setupTextFields();

        }
    }

    private void setupTextFields(){

        this.txtName = this.requireActivity().findViewById(R.id.profile_txt_name);
        this.txtName.setText(this.user.getName());
        this.txtName.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtSurname = this.requireActivity().findViewById(R.id.profile_txt_surname);
        this.txtSurname.setText(this.user.getSurname());
        this.txtSurname.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtEmail = this.requireActivity().findViewById(R.id.profile_txt_email);
        this.txtEmail.setText(this.user.getEmail());
        this.txtEmail.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtBirthDate = this.requireActivity().findViewById(R.id.profile_txt_bdate);
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY).format(this.user.getBirthDate());
        this.txtBirthDate.setText(date);
        this.txtBirthDate.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtLiving = this.requireActivity().findViewById(R.id.profile_txt_living);
        this.txtLiving.setText(this.user.getLiving());
        this.txtLiving.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtFavCar = this.requireActivity().findViewById(R.id.profile_txt_prefcar);
        this.txtFavCar.setText(this.user.getFavouriteCar());
        this.txtFavCar.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtFavCircuit = this.requireActivity().findViewById(R.id.profile_txt_prefcircuit);
        this.txtFavCircuit.setText(this.user.getFavouriteCircuit());
        this.txtFavCircuit.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtFavRace = this.requireActivity().findViewById(R.id.profile_txt_prefrace);
        this.txtFavRace.setText(this.user.getFavouriteRace());
        this.txtFavRace.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtHateCir = this.requireActivity().findViewById(R.id.profile_txt_hatecir);
        this.txtHateCir.setText(this.user.getHatedCircuit());
        this.txtHateCir.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtPassword = this.requireActivity().findViewById(R.id.profile_txt_password);
        this.txtPassword.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtConfirmPassword = this.requireActivity().findViewById(R.id.profile_txt_confirm_password);
        this.txtConfirmPassword.setOnFocusChangeListener(this::hideKeyboardOnDeselect);

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

    }

    private void loggedBtnAvatarClick(View v){

        Log.i(TAG, "Btn avatar did click");


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
}
