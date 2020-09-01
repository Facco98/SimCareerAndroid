package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.persistence.PersistenceManager;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MaterialButton btnLogin;
    private MaterialButton btnRegister;
    private MaterialButton btnGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.deleteDatabase("User.db");
        //new PersistenceManager(this.getApplicationContext()).setUser(null);
        Remote.init(this.getApplicationContext());
        //this.addDefaultUser();

        setContentView(R.layout.activity_main);
        this.initUI();
        if( new PersistenceManager(this.getApplicationContext()).isUserSignedIn() ){

            Intent i = new Intent(this, HomeActivity.class);
            this.startActivity(i);
            this.finish();

        }
    }

    private void initUI(){

        this.btnLogin = this.findViewById(R.id.main_btn_login);
        this.btnRegister = this.findViewById(R.id.main_btn_register);
        this.btnGuest = this.findViewById(R.id.main_btn_guest);
        this.btnLogin.setOnClickListener(this::btnLoginClick);
        this.btnRegister.setOnClickListener(this::btnRegisterClick);
        this.btnGuest.setOnClickListener(this::btnGuestClick);

    }

    private void btnLoginClick(View v){

        Log.i(TAG, "Button login did Click!");
        Intent i = new Intent(this, LoginActivity.class);
        this.startActivity(i);

    }

    private void btnRegisterClick( View v ){

        Log.i(TAG, "Button register did Click!");
        Intent i = new Intent(this, SignUpActivity.class);
        this.startActivity(i);

    }

    private void btnGuestClick( View v ){

        Log.i(TAG, "Button guest did click");
        Intent i = new Intent(this, HomeActivity.class);
        this.startActivity(i);

    }

    private void addDefaultUser(){

        try {
            Date bDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY).parse("11/01/1998");
            if(bDate == null )
                return;
            User u = new User("devack@hotmail.it", "Claudio", "Facchinetti", "BS", bDate,
                    "Seat Leon", "89", "Leon", "Monza", null);
            Remote.register(u, "pas", (res) -> System.out.println("REGISTERED") );
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
