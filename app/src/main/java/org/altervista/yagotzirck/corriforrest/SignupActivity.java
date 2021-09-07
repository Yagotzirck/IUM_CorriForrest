package org.altervista.yagotzirck.corriforrest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    private TextView errorMsg;
    private TextInputLayout username, weight, password, passwordConfirm;
    private Button signUp;
    private TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Inizializzazione attributi associati ai widgets
        errorMsg = findViewById(R.id.signup_errorMsg);

        username = findViewById(R.id.signup_username);
        weight = findViewById(R.id.signup_weight);
        password = findViewById(R.id.signup_password);
        passwordConfirm = findViewById(R.id.signup_password_confirm);
        signUp = findViewById(R.id.signup_signup);
        backToLogin = findViewById(R.id.signup_backToLogin);


        // Impostazione degli event listeners
        signUp.setOnClickListener(v -> { signUp(); } );
        backToLogin.setOnClickListener(v -> { backToLogin(); } );

    }

    private void signUp() {
        if (!validateFields()) {
            errorMsg.setVisibility(View.VISIBLE);
            return;
        }

        // se tutto è andato a buon fine aggiungi l'utente alla lista degli utenti registrati,
        // informa l'utente che la registrazione è avvenuta con successo
        // e torna all'activity Login
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String sUsername = username.getEditText().getText().toString();
        builder.setMessage("L'utente \"" + sUsername + "\" è stato registrato con successo.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addUserToList();
                        Intent login = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(login);
                    }
                });
        AlertDialog registrationOkMsg = builder.create();
        registrationOkMsg.show();


    }

    private boolean validateFields() {
        String usernameInput = username.getEditText().getText().toString();
        String passwordInput = password.getEditText().getText().toString();
        String passwordConfirmInput = passwordConfirm.getEditText().getText().toString();
        String weightInput = weight.getEditText().getText().toString();

        TextInputLayout[] inputFields = {username,
                password,
                passwordConfirm,
                weight
        };

        boolean emptyFields = false;

        username.setError(null);
        password.setError(null);
        passwordConfirm.setError(null);
        weight.setError(null);

        // Controlla che non ci siano campi vuoti
        for (TextInputLayout inputField : inputFields) {
            if (inputField.getEditText().getText().toString().isEmpty()) {
                emptyFields = true;
                inputField.setError(getString(R.string.signup_error_emptyField));
            }
        }

        Users users = Users.getInstance();

        boolean errorsPresent = false;

        // Controlla che l'utente non sia già registrato
        if (users.contains(usernameInput)) {
            username.setError(getString(R.string.signup_error_userAlreadyExists));
            errorsPresent = true;
        }

        // I due campi password devono coincidere
        if (!passwordInput.equals(passwordConfirmInput)) {
            password.setError(getString(R.string.signup_error_passwordMismatch));
            passwordConfirm.setError(getString(R.string.signup_error_passwordMismatch));
            errorsPresent = true;
        }

        return !(emptyFields || errorsPresent);
    }

    private void addUserToList(){
        String sUsername = username.getEditText().getText().toString();
        String sPassword = password.getEditText().getText().toString();
        int iWeight = Integer.parseInt( weight.getEditText().getText().toString() );

        User user = new User(sUsername, sPassword, iWeight);

        Users users = Users.getInstance();
        users.add(user);
    }

    private void backToLogin(){
        // Torna all'activity di login
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

}