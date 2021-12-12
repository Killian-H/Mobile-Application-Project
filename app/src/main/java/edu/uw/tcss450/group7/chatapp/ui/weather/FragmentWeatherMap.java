package edu.uw.tcss450.group7.chatapp.ui.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentWeatherMapBinding;

public class FragmentWeatherMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private LocationViewModel mModel;

    private GoogleMap mMap;

    private Fragment_weatherViewModel weatherView;
    /**
     * Standard onCreate method for when the fragment is first accessed.
     *
     * @param savedInstanceState The current state of the app.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherView = new ViewModelProvider(getActivity()).get(Fragment_weatherViewModel.class);

    }

    /**
     * Inflates the map layout.
     *
     * @param inflater Standard layout inflater.
     * @param container Container containing the map.
     * @param savedInstanceState Current state of the fragment.
     *
     * @return Returns the inflated map layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_map, container, false);
    }

    /**
     * When the view has been created this will check to see if the map
     * is ready for use.
     *
     * @param view Current view.
     * @param savedInstanceState Current state of fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //add this fragment as the OnMapReadyCallback -> See onMapReady()
        mapFragment.getMapAsync(this);

    }

    /**
     * When the map is clicked the camera will be moved over the clicked
     * position.
     *
     * @param latLng Latitude and Longitude clicked by the user.
     */
    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        latLng, mMap.getCameraPosition().zoom));
        mMap.clear();
         mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("New Marker"));

         //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        weatherView.connect(latLng.longitude, latLng.latitude);


    }

    /**
     * When the map is ready sets the location of the camera above
     * the user.
     *
     * @param googleMap The map being viewed.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if(location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);

                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
            }
        });

        mMap.addMarker(new MarkerOptions()
                .position(weatherView.getMarkerSaved())
                .title("New Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(weatherView.getMarkerSaved(), 10.0f));
        mMap.setOnMapClickListener(this);
    }

}
