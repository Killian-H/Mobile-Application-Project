package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.group7.chatapp.ui.auth.register.RegisterViewModel;
import edu.uw.tcss450.group7.chatapp.ui.contact.ContactListFragmentDirections;

/**
 * A simple {@link Fragment} subclass.

 */
public class Fragment_weather extends Fragment {

    //binding used to update weather info
    private FragmentWeatherBinding binding;
    //view model for weather
    private Fragment_weatherViewModel mWeatherModel;
    private Weather_Main myWeatherMain;

    /* The desired interval for location updates. Inexact. Updates may
    be more or less frequent. */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /* The fastest rate for active location updates. Exact. Updates will never be more
    frequent than this value. */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /* A constant int for the permissions request code. Must be a 16 bit number. */
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;

    private LocationRequest mLocationRequest;

    /* Use a FusedLocationProviderClient to request the location. */
    private FusedLocationProviderClient mFusedLocationClient;

    /* Will use this call back to decide what to do when a location change is detected. */
    private LocationCallback mLocationCallback;

    /* The ViewModel that will store the current location. */
    private LocationViewModel mLocationModel;
    /*Places client */
    private PlacesClient mPlacesClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mWeatherModel==null)mWeatherModel = new ViewModelProvider(getActivity()).get(Fragment_weatherViewModel.class);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyDrnr_jdk8D_Nyp741UZys_gqMFOb9w54g");
        }
        // Create a new PlacesClient instance
        mPlacesClient = Places.createClient(getContext());


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        } else {
            //The user has already allowed the use of Locations. Get the current location.
            requestLocation();

        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Log.d("LOCATION UPDATE!", location.toString());
                    if (mLocationModel == null) {
                        mLocationModel = new ViewModelProvider(getActivity())
                                .get(LocationViewModel.class);
                    }
                    mLocationModel.setLocation(location);
                    //mWeatherModel.connect(mLocationModel.getCurrentLocation().getLongitude(),mLocationModel.getCurrentLocation().getLatitude());

                }
            }

            ;
        };


        createLocationRequest();
        connectInBackground();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater);

        return binding.getRoot();

    }

    /**
     * Create and configure a Location Request used when retrieving location updates
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //7 day and 24 hour forecast recycle view listener
        binding.weatherRVSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && myWeatherMain != null) {
                    Weather_RecycleViewAdapter rvAdapter7DAY = new Weather_RecycleViewAdapter(myWeatherMain.getMy7DayForecast().getMy7DayWeatherArray());
                    binding.weather7dayRV.setAdapter(rvAdapter7DAY);
                    binding.weatherDisplayRvHeader.setText("7 day forecast");
                    binding.weatherRVSwitch.setText("Switch to 24 hour");

                } else if (!isChecked && myWeatherMain != null) {

                    Weather_RecycleViewAdapter rvAdapterHOURLY = new Weather_RecycleViewAdapter(myWeatherMain.getMyHourlyForecast().getMyHourlyWeatherArray());
                    binding.weather7dayRV.setAdapter(rvAdapterHOURLY);
                    binding.weatherDisplayRvHeader.setText("24 hour forecast");
                    binding.weatherRVSwitch.setText("Switch to 7 day");
                }
            }
        });


        //Json request/response observer for location data
        mWeatherModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            //main weather object that contains all the weather data converted from the response
            myWeatherMain = new Weather_Main(response);
            //update ui elements to reflect data
            binding.weatherHumidity.setText("Humidity " + myWeatherMain.getMyCurrentWeather().getMyHumidity() + "%");
            binding.weatherConditionText.setText("" + myWeatherMain.getMyCurrentWeather().getMyShortDescription());
            binding.weatherTemp.setText("" + myWeatherMain.getMyCurrentWeather().getMyTemp() + "°F");
            binding.weatherFeelsLike.setText("Feels like " + myWeatherMain.getMyCurrentWeather().getMyFeels() + "°F");
            binding.weatherPressure.setText("Pressure " + myWeatherMain.getMyCurrentWeather().getMyPressure() + " hPa");
            Geocoder geo = new Geocoder(getContext(),Locale.US);

            try {
                String geoString = geo.getFromLocation( myWeatherMain.getMyLatitude(),myWeatherMain.getMyLongitude(),1).get(0).getAddressLine(0);
                binding.weatherDisplayMainHeader.setText(""+geoString.substring(geoString.indexOf(",")+1));
            } catch (IOException e) {
                Log.e("header geocoder","main header failed with geocoder");
            }

            //Use picasso dependency to load weather icons via the api endpoint
            Picasso.with(getContext())
                    .load("https://openweathermap.org/img/wn/" + myWeatherMain.getMyCurrentWeather().getMyIconID() + "@2x.png")
                    .into(binding.weatherConditionIcon);

            //update recycle card view to show hourly first
            Weather_RecycleViewAdapter rvAdapterHOURLY = new Weather_RecycleViewAdapter(myWeatherMain.getMyHourlyForecast().getMyHourlyWeatherArray());
            binding.weather7dayRV.setAdapter(rvAdapterHOURLY);
        });
        //????
        binding.buttonMap.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    Fragment_weatherDirections.actionNavigationWeatherToMap()
            );
        });

        // Initialize the google places AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        // Specify the types of place data to return.
        autocompleteFragment.setTypeFilter(TypeFilter.GEOCODE);
        //Specify which fields you want to include
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.LAT_LNG));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("onPlaceSelected", "Place: " + place.getName() + ", " + place.getId());
                //Update ui based on selected location
                binding.weatherDisplayMainHeader.setText(place.getAddress().substring(place.getAddress().indexOf(",")));
                mWeatherModel.connect(place.getLatLng().longitude,place.getLatLng().latitude);


            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("onErrorPlaces", "An error occurred: " + status);
            }
        });

    }


    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                    requestLocation();

                } else {
                    // TODO: Figure out what to do here.
                    System.out.print("TBD");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Requests the current location of the device. If the user allowed location services
     * the map will be updated with their current location. If not, no updates or location
     * services will start.
     */
    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("LOCATION", location.toString());
                                if (mLocationModel == null) {
                                    mLocationModel = new ViewModelProvider(getActivity())
                                            .get(LocationViewModel.class);
                                }
                                mLocationModel.setLocation(location);
                            }
                        }
                    });
        }
    }

    /**
     * Updates the current location.
     */
    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    /**
     * Stops updating the current location.
     */
    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    /**
     * Inflates the settings menu.
     *
     * @param menu The settings menu being inflated.
     * @param inflater Default menu inflater.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Navigates to the settings activity when the settings option is selected
     * from the dropdown menu.
     *
     * @param item Settings item within the menu.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Navigation.findNavController(getView()).navigate(
                    Fragment_weatherDirections.actionNavigationWeatherToSettingsActivity());

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Asynchronous call. verifying the registration with the auth endpoint of the server.
     */
    private void connectInBackground() {
        //default location Set to Anartica for Debug purposes
        Double lat = -69.0;
        Double lon = -69.0;

        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");

            }
            else{
                mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d("background Location", location.toString());
                           // lat = mFusedLocationClient.getLastLocation().getResult().getLatitude();
                            //lon = mFusedLocationClient.getLastLocation().getResult().getLongitude();
                            mWeatherModel.connect(location.getLongitude(),location.getLatitude());
                        }
                    }
                });

            }

        }
        catch (Exception e){
            Log.e("Location Error", " mFusedLocation Problem, using default location");
            //Tacoma Gps hardcoded currently showing timezone instead of actual location
            mWeatherModel.connect(lon,lat);
        }


        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }


}