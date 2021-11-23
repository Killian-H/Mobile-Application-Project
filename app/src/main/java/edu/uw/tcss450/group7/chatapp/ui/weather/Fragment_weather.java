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
import android.widget.CompoundButton;
import android.widget.Switch;
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
    private Weather_Main myWeatherMain;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mWeatherModel = new ViewModelProvider(getActivity()).get(Fragment_weatherViewModel.class);



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
        binding.weatherRVSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Weather_RecycleViewAdapter rvAdapter7DAY=new Weather_RecycleViewAdapter(myWeatherMain.getMy7DayForecast().getMy7DayWeatherArray());
                    binding.weather7dayRV.setAdapter(rvAdapter7DAY);
                    binding.weatherDisplayRvHeader.setText("7 day forecast");
                    binding.weatherRVSwitch.setText("Switch to 24 hour");
                }
                else {

                    Weather_RecycleViewAdapter rvAdapterHOURLY=new Weather_RecycleViewAdapter(myWeatherMain.getMyHourlyForecast().getMyHourlyWeatherArray());
                    binding.weather7dayRV.setAdapter(rvAdapterHOURLY);
                    binding.weatherDisplayRvHeader.setText("24 hour forecast");
                    binding.weatherRVSwitch.setText("Switch to 7 day");
                }
            }
        });



        //
        mWeatherModel.addResponseObserver(getViewLifecycleOwner(), response -> {

            myWeatherMain = new Weather_Main(response);
              binding.weatherHumidity.setText(""+myWeatherMain.getMyCurrentWeather().getMyHumidity()+"%");
              binding.weatherConditionText.setText(""+myWeatherMain.getMyCurrentWeather().getMyShortDescription());
              binding.weatherTemp.setText(""+myWeatherMain.getMyCurrentWeather().getMyTemp()+"°F");
              binding.weatherFeelsLike.setText(""+myWeatherMain.getMyCurrentWeather().getMyFeels()+"°F");
              binding.weatherPressure.setText(""+myWeatherMain.getMyCurrentWeather().getMyPressure()+" hPa");
              binding.weatherLocationSearch.setText(""+myWeatherMain.getMyTimezone());



                //RecycleViewDaily
                Weather_RecycleViewAdapter rvAdapter7DAY=new Weather_RecycleViewAdapter(myWeatherMain.getMy7DayForecast().getMy7DayWeatherArray());
                Weather_RecycleViewAdapter rvAdapterHOURLY=new Weather_RecycleViewAdapter(myWeatherMain.getMyHourlyForecast().getMyHourlyWeatherArray());
                // .getMyHourlyForecast().getMyHourlyWeatherArray()
                binding.weather7dayRV.setAdapter(rvAdapterHOURLY);
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