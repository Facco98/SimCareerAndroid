package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.R;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.persistence.PersistenceManager;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Remote;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Result;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private MaterialButton btnLogin;
    private EditText txtEmail;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
        this.initUI();
    }

    private void initUI(){

        this.btnLogin = this.findViewById(R.id.login_btn_login);
        this.txtEmail = this.findViewById(R.id.login_txt_email);
        this.txtPassword = this.findViewById(R.id.login_txt_password);

        this.btnLogin.setOnClickListener(this::btnLoginDidClick);

    }

    private void btnLoginDidClick(View v){

        String email = this.txtEmail.getText().toString();
        String password = this.txtPassword.getText().toString();
        Log.i(TAG, "Button login did click; values are: email="+ email +", password="+ password );
        Remote.login(email, password, this::handleLogin);

    }

    private void handleLogin(Result<User> result){

        if(result.getError() != null)
            Toast.makeText(this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
        else if( result.getContent() != null ) {
            Log.i(TAG, "User logged in: " + result.getContent().getName() + " " + result.getContent().getSurname());
            new PersistenceManager(this.getApplicationContext()).setUser(result.getContent());
            Intent i = new Intent(this, HomeActivity.class);
            this.startActivity(i);
            this.finish();
        }
    }

}
