package org.altervista.yagotzirck.corriforrest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.MutableBoolean;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class ProfileChangePasswordFragment extends DialogFragment {

    private TextInputLayout oldPassword;
    private TextInputLayout newPassword;
    private TextInputLayout confirmNewPassword;

    private ProfileChangeField listener;

    private boolean isOldPasswordFilled;
    private boolean isNewPasswordFilled;
    private boolean isConfirmNewPasswordFilled;

    private AlertDialog alertDialog;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.fragment_change_password, null);

        listener = (ProfileChangeField)getParentFragment();
        if(listener == null)    // Dialog used inside an activity
            listener = (ProfileChangeField)getActivity();

        builder.setView(view)
                .setTitle("Modifica password")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // usually there's dialogOK() here, but we override this later
                        // in order to prevent the dialog from closing automatically
                        // when OK is clicked
                    }
                });

        alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if(dialogOK())
                dismiss();
        });

        findViews(view);
        setOnTextChangedForAll();



        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility((View.INVISIBLE));
        return alertDialog;
    }

    private void findViews(View view){
        oldPassword = view.findViewById(R.id.profile_oldPassword);
        newPassword = view.findViewById(R.id.profile_newPassword);
        confirmNewPassword = view.findViewById(R.id.profile_confirmNewPassword);
    }

    // Todo: aggiungere setCallbacks() con le varie funzioni onFocusChanged() !hasFocus eccetera per vedere se l'utente ha compilato tutti i campi
    // 3 booleani (uno per campo): se tutti e 3 sono true, attiva il pulsante di conferma


    private boolean isTextInputLayoutFieldEmpty(TextInputLayout til){
        return til.getEditText().getText().toString().isEmpty();
    }

    private String getTextInputLayoutField(TextInputLayout til){
        return til.getEditText().getText().toString();
    }

    private void activateOkButtonIfAllFieldsAreFilled(){
        int visibility = (isOldPasswordFilled && isNewPasswordFilled && isConfirmNewPasswordFilled) ? View.VISIBLE : View.INVISIBLE;
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(visibility);
    }



    @FunctionalInterface
    private interface SetBooleanValue{
        void setBool(boolean value);
    }

    private void setOnTextChanged(TextInputLayout til, SetBooleanValue setBooleanValue){


        til.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBooleanValue.setBool(!isTextInputLayoutFieldEmpty(til));
                activateOkButtonIfAllFieldsAreFilled();
            }

        });
    }


    private void setOnTextChangedForAll(){
        isOldPasswordFilled = false;
        isNewPasswordFilled = false;
        isConfirmNewPasswordFilled = false;

        setOnTextChanged(oldPassword, bVal -> isOldPasswordFilled = bVal);
        setOnTextChanged(newPassword, bVal -> isNewPasswordFilled = bVal);
        setOnTextChanged(confirmNewPassword, bVal -> isConfirmNewPasswordFilled = bVal);

    }


    private boolean dialogOK(){
        if(validateFields()) {
            listener.updateField(getTextInputLayoutField(newPassword));
            return true;
        }
        return false;
    }

    private boolean validateFields(){
        String oldPasswordInput = getTextInputLayoutField(oldPassword);
        String newPasswordInput = getTextInputLayoutField(newPassword);
        String newPasswordConfirmInput = getTextInputLayoutField(confirmNewPassword);

        User user = Users.getInstance().get(LoggedUser.getInstance().get());

        // Controllo sul vecchio campo password
        if(!user.getPassword().equals(oldPasswordInput)){
            oldPassword.setError(getString(R.string.changePW_error_oldPasswordMismatch));
            return false;
        }


        // I due campi password devono coincidere
        if (!newPasswordInput.equals(newPasswordConfirmInput)) {
            newPassword.setError(getString(R.string.changePW_error_passwordMismatch));
            confirmNewPassword.setError(getString(R.string.changePW_error_passwordMismatch));
            return false;
        }

        // La nuova password non pu?? essere uguale a quella precedente
        if(newPasswordInput.equals(oldPasswordInput)){
            newPassword.setError(getString(R.string.changePW_error_passwordSameAsOld));
            confirmNewPassword.setError(getString(R.string.changePW_error_passwordSameAsOld));
            return false;
        }

        return true;
    }
}
