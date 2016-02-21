package te.com.locksmith.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.android.airmapview.AirMapMarker;
import com.airbnb.android.airmapview.AirMapView;
import com.airbnb.android.airmapview.listeners.OnMapInitializedListener;
import com.google.android.gms.maps.model.LatLng;

import te.com.locksmith.R;
import te.com.locksmith.constants.Globals;
import te.com.locksmith.customComponets.CustomFragment;

/**
 * Created by enes on 21/02/16.
 */
public class SelectServiceType extends CustomFragment {

    private AirMapView airMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.locksmith_type, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        airMapView = (AirMapView) view.findViewById(R.id.map_view);
        airMapView.initialize(context.getSupportFragmentManager());
        airMapView.setOnMapInitializedListener(new OnMapInitializedListener() {
            @Override
            public void onMapInitialized() {
                LatLng airbnbLatLng = new LatLng(Globals.myLocation.getLatitude(), Globals.myLocation.getLongitude());
                addMarker("Buradasınız", airbnbLatLng, 1);
                airMapView.animateCenterZoom(airbnbLatLng, 15);
                airMapView.setMyLocationEnabled(true);
                airMapView.showContextMenu();
            }
        });
    }



    private void addMarker(String title, LatLng latLng, int id) {
        AirMapMarker airMapMarker = new AirMapMarker(latLng, id)
                .setTitle(title)
                .setIconId(R.mipmap.icon_location_pin);
        airMapView.addMarker(airMapMarker);
    }
}
