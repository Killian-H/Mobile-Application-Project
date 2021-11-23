package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;

import org.json.JSONObject;

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
    private RecyclerView myRecylerView;
    private Object[] myWeatherArray;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mWeatherModel = new ViewModelProvider(getActivity()).get(Fragment_weatherViewModel.class);
        //myRecylerView = findViewById(R.id.weather_7dayRV);
        //myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        connectInBakcground();

        //
        mWeatherModel.addResponseObserver(getViewLifecycleOwner(), response -> {

            Weather_Main weatherObject = new Weather_Main(response);
              binding.weatherHumidity.setText(""+weatherObject.getMyCurrentWeather().getMyHumidity()+"%");
              binding.weatherConditionText.setText(""+weatherObject.getMyCurrentWeather().getMyShortDescription());
              binding.weatherTemp.setText(""+weatherObject.getMyCurrentWeather().getMyTemp()+"°F");
              binding.weatherFeelsLike.setText(""+weatherObject.getMyCurrentWeather().getMyFeels()+"°F");
              binding.weatherPressure.setText(""+weatherObject.getMyCurrentWeather().getMyPressure()+" hPa");
              binding.weatherLocationSearch.setText(""+weatherObject.getMyTimezone());


            if (weatherObject.getMy7DayForecast().getMy7DayWeatherArray()!= null) {
                //RecycleViewHourly
                Weather_RecycleViewAdapter rvAdapter=new Weather_RecycleViewAdapter(weatherObject.getMy7DayForecast().getMy7DayWeatherArray());
                binding.weather7dayRV.setAdapter(rvAdapter);}
            });

    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


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
    private void connectInBakcground() {
        //Tacoma Gps hardcoded currently showing timezone instead of actual location
        mWeatherModel.connect(-122.465,47.258);
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }


}