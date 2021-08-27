package org.altervista.yagotzirck.corriforrest;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SessionDetailsDialog extends DialogFragment {

    private DataSession dataSession;

    public static SessionDetailsDialog newInstance(DataSession dataSession) {
        SessionDetailsDialog f = new SessionDetailsDialog();

        f.setDataSession(dataSession);

        return f;
    }

    private void setDataSession(DataSession dataSession){ this.dataSession = dataSession; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true); // deprecated
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_session_details, container, false);
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

}
