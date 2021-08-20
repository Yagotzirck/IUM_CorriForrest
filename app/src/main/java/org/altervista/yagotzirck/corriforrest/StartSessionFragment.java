package org.altervista.yagotzirck.corriforrest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StartSessionFragment extends Fragment {

    private ImageButton startSession;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_start_session, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startSession = view.findViewById(R.id.button_start_session);
        startSession.setOnClickListener( v -> { startSession(); } );

    }

    private void startSession(){
        Intent startSession = new Intent(this.getActivity(), SessionStartedActivity.class);
        startActivity(startSession);
    }
}
