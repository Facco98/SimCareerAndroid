package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception.EmptyFieldException;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Result;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private static final int PICK_IMAGE = 127;

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

    private CheckBox cbxTerms;
    private CheckBox cbxPrivacy;

    private Button btnChooseAvatar;
    private Button btnSignUp;

    private ImageView imgAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.initUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if( requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null ){


            Uri selectedImage = data.getData();

            this.imgAvatar.setImageURI(selectedImage);
            this.imgAvatar.setBackgroundColor(Color.TRANSPARENT);

            /*
            String[] filePathColumn = { MediaStore.Images.Media.DATE_ADDED };
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            */



        }

    }

    private void initUI() {

        this.txtName = this.findViewById(R.id.sign_up_txt_name);
        this.txtName.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtSurname = this.findViewById(R.id.sign_up_txt_surname);
        this.txtSurname.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtEmail = this.findViewById(R.id.sign_up_txt_email);
        this.txtEmail.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtBirthDate = this.findViewById(R.id.sign_up_txt_bdate);
        this.txtBirthDate.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtLiving = this.findViewById(R.id.sign_up_txt_living);
        this.txtLiving.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtFavCar = this.findViewById(R.id.sign_up_txt_prefcar);
        this.txtFavCar.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtFavCircuit = this.findViewById(R.id.sign_up_txt_prefcircuit);
        this.txtFavCircuit.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtFavRace = this.findViewById(R.id.sign_up_txt_prefrace);
        this.txtFavRace.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtHateCir = this.findViewById(R.id.sign_up_txt_hatecir);
        this.txtHateCir.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtPassword = this.findViewById(R.id.sign_up_txt_password);
        this.txtPassword.setOnFocusChangeListener(this::hideKeyboardOnDeselect);
        this.txtConfirmPassword = this.findViewById(R.id.sign_up_txt_confirm_password);
        this.txtConfirmPassword.setOnFocusChangeListener(this::hideKeyboardOnDeselect);

        this.btnChooseAvatar = this.findViewById(R.id.sign_up_btn_avatar);
        this.btnSignUp = this.findViewById(R.id.sign_up_btn_sign_up);

        this.imgAvatar = this.findViewById(R.id.sign_up_img_avatar);

        this.cbxTerms = this.findViewById(R.id.sign_up_cbx_terms);
        this.cbxPrivacy = this.findViewById(R.id.sign_up_cbx_privacy);

        this.btnChooseAvatar.setOnClickListener(this::btnChooseAvatarClick);
        this.btnSignUp.setOnClickListener(this::btnSignUpClick);
    }

    private void btnChooseAvatarClick( View v ){

        Log.i(TAG, "Button ChooseAvatar did click!");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    private void btnSignUpClick( View v ){

        Log.i(TAG, "Button SignUp did click!");

        if( !this.cbxTerms.isChecked() || !this.cbxPrivacy.isChecked() ){

            Toast.makeText(this, R.string.sign_up_cbx_not_selected, Toast.LENGTH_SHORT);
            return;
        }

        try {
            Date birthDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY).parse(this.txtBirthDate.getText().toString());
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
            this.assertNotEmpty(password, "Password");
            this.assertNotEmpty(confirmPassword, "Conferma password");

            Bitmap avatar = ((BitmapDrawable)imgAvatar.getDrawable()).getBitmap();

            if( !password.equals(confirmPassword) )
                Toast.makeText(this, R.string.sign_up_password_mismatch, Toast.LENGTH_SHORT ).show();

            else {

                if (birthDate != null) {
                    User u = new User(email, name, surname, living, birthDate, favouriteCar, favouriteRace,
                            favouriteCircuit, hatedCircuit, avatar);
                    Remote.register(u, password, this::registrationHandler);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (EmptyFieldException e){
            Toast.makeText(this, e.fieldName + " è un campo obbligatorio", Toast.LENGTH_SHORT);
        }

    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void hideKeyboardOnDeselect(View v, boolean hasFocus){

        if( !hasFocus )
            this.hideKeyboard(v);

    }

    private void registrationHandler(Result<Boolean> result){

        if( result.isSuccessfull() ) {
            Toast toast = Toast.makeText(this, "Registrazione completata", Toast.LENGTH_SHORT);
            toast.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SignUpActivity.this.finish();
                }
            }, Toast.LENGTH_SHORT);
        }
        else if ( result.getError() != null ){

            Toast.makeText(this, "Registrazione: ERRORE", Toast.LENGTH_SHORT).show();
            result.getError().printStackTrace();
        }

    }

    private void assertNotEmpty(String str, String field) throws EmptyFieldException{

        if( str.isEmpty() ){

            throw new EmptyFieldException(field);
        }

    }
}
