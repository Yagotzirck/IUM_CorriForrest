package org.altervista.yagotzirck.corriforrest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class ProfileChangeUsernameFragment extends DialogFragment {

    private TextInputLayout newUsername;


    private ProfileChangeField listener;


    private boolean isNewUsernameFilled;

    private AlertDialog alertDialog;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.fragment_change_username, null);

        listener = (ProfileChangeField)getParentFragment();
        if(listener == null)    // Dialog used inside an activity
            listener = (ProfileChangeField)getActivity();

        builder.setView(view)
                .setTitle("Modifica nome utente")
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
        newUsername = view.findViewById(R.id.profile_newUsername);
    }



    private boolean isTextInputLayoutFieldEmpty(TextInputLayout til){
        return til.getEditText().getText().toString().isEmpty();
    }

    private String getTextInputLayoutField(TextInputLayout til){
        return til.getEditText().getText().toString();
    }

    private void activateOkButtonIfAllFieldsAreFilled(){
        int visibility = isNewUsernameFilled ? View.VISIBLE : View.INVISIBLE;
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
        isNewUsernameFilled = false;


        setOnTextChanged(newUsername, bVal -> isNewUsernameFilled = bVal);

    }


    private boolean dialogOK(){
        if(validateFields()) {
            listener.updateField(getTextInputLayoutField(newUsername));
            return true;
        }
        return false;
    }

    private boolean validateFields(){
        String newUsernameInput = getTextInputLayoutField(newUsername);

        User user = Users.getInstance().get(LoggedUser.getInstance().get());

        // Il nuovo nome utente non può essere uguale a quello precedente
        if(newUsernameInput.equals(user.getUsername())){
            newUsername.setError(getString(R.string.changeUsername_error_usernameSameAsOld));
            return false;
        }

        return true;
    }
}
