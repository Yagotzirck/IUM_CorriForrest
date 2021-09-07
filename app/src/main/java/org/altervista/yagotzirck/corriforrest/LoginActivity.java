package org.altervista.yagotzirck.corriforrest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

//TextInputLayout.getEditText()

public class LoginActivity extends AppCompatActivity {

    private TextView    errorMsg;
    private TextInputLayout username, password;
    private Button      login;
    private TextView    createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inizializzazione attributi associati ai widgets
        errorMsg =      findViewById(R.id.login_errorMsg);
        username =      findViewById(R.id.login_username);
        password =      findViewById(R.id.login_password);
        login =         findViewById(R.id.login_login);
        createAccount = findViewById(R.id.login_createAccount);

        // Impostazione degli event listeners
        login.setOnClickListener( v -> { login(); } );
        createAccount.setOnClickListener( v -> { signUp(); } );
    }

    private void login(){
        if(!validateFields()){
            errorMsg.setVisibility(View.VISIBLE);
            return;
        }

        // se tutto è andato a buon fine, passa all'activity Home
        LoggedUser loggedUser = LoggedUser.getInstance();
        loggedUser.set(username.getEditText().getText().toString());
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);

    }

    private boolean validateFields() {
        String usernameInput = username.getEditText().getText().toString();
        String passwordInput = password.getEditText().getText().toString();

        boolean emptyFields = false;

        username.setError(null);
        password.setError(null);

        if (usernameInput.isEmpty()) {
            username.setError(getString(R.string.login_error_emptyField));
            emptyFields = true;
        }
        if (passwordInput.isEmpty()) {
            password.setError(getString(R.string.login_error_emptyField));
            emptyFields = true;
        }

        boolean errorsPresent = false;

        Users users = Users.getInstance();

        if (!usernameInput.isEmpty() && !users.contains(usernameInput)) {
            username.setError(getString(R.string.login_error_invalidUsername));
            return false;
        }

        if (!passwordInput.isEmpty() &&
                !usernameInput.isEmpty() &&
                !users.checkPassword(usernameInput, passwordInput) ) {
            password.setError(getString(R.string.login_error_invalidPassword));
            errorsPresent = true;
        }

        return !(emptyFields || errorsPresent);
    }

    private void signUp(){
        // Passa all'activity Signup
        Intent signUp = new Intent(this, SignupActivity.class);
        startActivity(signUp);
    }
}