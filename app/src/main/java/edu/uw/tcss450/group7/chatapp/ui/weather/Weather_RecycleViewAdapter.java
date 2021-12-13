package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentWeatherCardRvBinding;
import edu.uw.tcss450.group7.chatapp.ui.contact.Contact;
import edu.uw.tcss450.group7.chatapp.ui.contact.ContactListFragmentDirections;
import edu.uw.tcss450.group7.chatapp.ui.contact.ContactRecyclerViewAdapter;

/**
 * Adapter for the recycle view that displays both 7day forecasts and hourly forecasts
 * @version: 12/12/2021
 * @author Aaron Purslow
 * Commented by: Aaron Purslow
 */
public class Weather_RecycleViewAdapter extends RecyclerView.Adapter<Weather_RecycleViewAdapter.myViewHolder>{
    //either the 7 day forecast array or hourly forecast array
    private Object[] mWeatherList;

    public Weather_RecycleViewAdapter(Object[] theWeatherList) {
        this.mWeatherList = theWeatherList;
    }

    /**
     * viewholder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public Weather_RecycleViewAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_card_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.setWeather((Weather_Current) mWeatherList[position]);

    }

    /**
     * @return the amount of items in the array
     */
    @Override
    public int getItemCount() {
        return mWeatherList.length;
    }

    /**
     * Viewholder to change what is displayed on the recycle view for each weather_current object in the mWeatherList array
     */
    public class myViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public FragmentWeatherCardRvBinding binding;
        private Weather_Current mWeather;

        public myViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentWeatherCardRvBinding.bind(view);

        }

        void setWeather(@NonNull final Weather_Current theWeather) {
            mWeather = theWeather;
                binding.weatherCardTemp.setText(""+theWeather.getMyTemp()+"°F");
                binding.weatherCardTime.setText(""+theWeather.getMyTime());
               // binding.weatherCardWind.setText("Wind Speed "+theWeather.getMyWindSpeed()+" mph");
            Picasso.with(mView.getContext())
                    .load("https://openweathermap.org/img/wn/"+theWeather.getMyIconID()+"@2x.png")
                    //.resize(binding.weatherCardIcon.getWidth(),binding.weatherCardIcon.getHeight())
                    .into(binding.weatherCardIcon);
            }
    }
}
