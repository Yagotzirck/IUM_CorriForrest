package org.altervista.yagotzirck.corriforrest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment implements ProfileChangeField {

    private enum SelectedField{
        USERNAME,
        PASSWORD,
        WEIGHT
    };

    User user;


    TextView username;
    TextView password;
    TextView weight;

    ImageButton usernameEdit;
    ImageButton passwordEdit;
    ImageButton weightEdit;

    SelectedField selectedField;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String loggedUser = LoggedUser.getInstance().get();
        user = Users.getInstance().get(loggedUser);

        findViews(view);
        setListeners();

        displayUserData();
    }




    private void findViews(View view){

        username = view.findViewById(R.id.profile_username);
        password = view.findViewById(R.id.profile_password);
        weight = view.findViewById(R.id.profile_weight);

        usernameEdit = view.findViewById(R.id.profile_username_edit_button);
        passwordEdit = view.findViewById(R.id.profile_password_edit_button);
        weightEdit = view.findViewById(R.id.profile_weight_edit_button);
    }

    void setListeners(){
        usernameEdit.setOnClickListener( v -> {
            selectedField = SelectedField.USERNAME;
            new ProfileChangeUsernameFragment().show(getChildFragmentManager(), null);
        });

        passwordEdit.setOnClickListener( v -> {
            selectedField = SelectedField.PASSWORD;
            new ProfileChangePasswordFragment().show(getChildFragmentManager(), null);
        });

        weightEdit.setOnClickListener( v -> {
            selectedField = SelectedField.WEIGHT;
            new ProfileChangeWeightFragment().show(getChildFragmentManager(), null);
        });
    }

    private void displayUserData(){
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        weight.setText(Integer.toString(user.getWeight()));
    }

    @Override
    public void updateField(String field) {
        String oldName;
        int newWeight;

        switch(selectedField){
            case USERNAME:
                oldName = user.getUsername();

                username.setText(field);

                user.setUsername(field);
                LoggedUser.getInstance().set(field);
                DataSessions.getInstance().changeUsernameFieldInSessions(oldName, field);
                Goals.getInstance().changeUsernameFieldInGoals(oldName, field);

                makeToast("Il nome utente è stato aggiornato.").show();
                break;

            case PASSWORD:
                password.setText(field);

                user.setPassword(field);

                makeToast("La password è stata aggiornata.").show();
                break;

            case WEIGHT:
                weight.setText(field);

                newWeight = Integer.parseInt(field);
                user.setWeight(newWeight);

                makeToast("Il peso è stato aggiornato.").show();
        }



    }

    private Toast makeToast(String msg) {
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView toastMessage = (TextView) view.findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        view.setBackgroundColor(Color.GREEN);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }
}
