package com.demotxt.droidsrce.homedashboard.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demotxt.droidsrce.homedashboard.R;
import com.demotxt.droidsrce.homedashboard.Utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapTripFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private Bundle arguments;

    public MapTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map2);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private List<LatLng> LatLongReadCSV(String path) throws FileNotFoundException {
        File file = new File(path);

        Scanner fileReader = new Scanner(file);
        List<LatLng> customDataEntries = new ArrayList<>();
        fileReader.nextLine();
        while (fileReader.hasNextLine()) {
            String[] array = fileReader.nextLine().split(",");
            customDataEntries.add(new LatLng(Double.parseDouble(array[0]), Double.parseDouble(array[1])));
        }
        fileReader.close();
        return customDataEntries;
    }

    private List<PolylineOptions> setColoredPolylinesOnTheMap(List<LatLng> points) {
        List<PolylineOptions> polylines = new ArrayList<>();

        for (int i = 1; i < points.size(); i++) {
            if (i % 2 == 0) {
                polylines.add(new PolylineOptions().add(points.get(i), points.get(i - 1)).color(Color.RED));
            } else {
                polylines.add(new PolylineOptions().add(points.get(i), points.get(i - 1)).color(Color.BLACK));
            }
        }

        return polylines;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String fileName = "2020-02-25T14:02:11.csv";
        List<LatLng> latLngList = null;
        PolylineOptions polylineOptions = new PolylineOptions().clickable(true);
        try {
            latLngList = LatLongReadCSV(Constants.LOCATON_LIVE_DATA_PATH + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (LatLng marker : latLngList) {
            googleMap.addMarker(new MarkerOptions().position(marker)
                    .alpha(0)
                    .title("Current point info: ")
                    .snippet("speed: 150 rpm: 3840 load: 48%"));
        }

        for (PolylineOptions option : setColoredPolylinesOnTheMap((latLngList))) {
            googleMap.addPolyline(option);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0), 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
