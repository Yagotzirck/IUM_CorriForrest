package org.altervista.yagotzirck.corriforrest;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class SessionDetailsMapDialog extends DialogFragment
        implements OnMapReadyCallback{

    private GoogleMap mMap;

    private Button closeButton;

    public SessionDetailsMapDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_details_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.dialog_map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        closeButton = view.findViewById(R.id.dialogmap_closeButton);
        closeButton.setOnClickListener(v -> dismiss() );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomAmount =16f;


        LatLng startPosLat = new LatLng(39.906909, 8.514383);

        LatLng pos2Lat = new LatLng(39.907106, 8.513639);
        LatLng pos3Lat = new LatLng(39.907218, 8.513700);
        LatLng pos4Lat = new LatLng(39.907244, 8.513754);
        LatLng pos5Lat = new LatLng(39.907537, 8.513887);

        LatLng arrivalPosLat = new LatLng(39.908286, 8.511248);

        MarkerOptions startPos = new MarkerOptions().position(startPosLat).title("Partenza");
        MarkerOptions arrivalPos = new MarkerOptions().position(arrivalPosLat).title("Arrivo");


        mMap.addMarker(startPos);
        mMap.addMarker(arrivalPos).showInfoWindow();

        PolylineOptions routePoints = new PolylineOptions()
                .add(startPosLat, pos2Lat)
                .add(pos2Lat, pos3Lat)
                .add(pos3Lat, pos4Lat)
                .add(pos4Lat, pos5Lat)
                .add(pos5Lat, arrivalPosLat)

                .color(Color.BLUE);

        mMap.addPolyline(routePoints);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrivalPosLat, zoomAmount));
    }
}