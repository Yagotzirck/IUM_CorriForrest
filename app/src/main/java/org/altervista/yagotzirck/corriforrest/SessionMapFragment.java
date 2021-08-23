package org.altervista.yagotzirck.corriforrest;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class SessionMapFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_session_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.session_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomAmount =18f;


        LatLng startPosLat = new LatLng(39.906909, 8.514383);
        LatLng currPosLat = new LatLng(39.907007, 8.514008);

        MarkerOptions startPos = new MarkerOptions().position(startPosLat).title("Partenza");
        MarkerOptions currPos = new MarkerOptions().position(currPosLat).title("Posizione attuale");

        mMap.addMarker(startPos);
        mMap.addMarker(currPos).showInfoWindow();

        PolylineOptions routePoints = new PolylineOptions().add(startPosLat, currPosLat).color(Color.BLUE);
        mMap.addPolyline(routePoints);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPosLat, zoomAmount));
    }


}
