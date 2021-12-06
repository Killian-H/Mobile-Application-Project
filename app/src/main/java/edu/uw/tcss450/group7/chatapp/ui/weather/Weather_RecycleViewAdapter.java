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

public class Weather_RecycleViewAdapter extends RecyclerView.Adapter<Weather_RecycleViewAdapter.myViewHolder>{
    private Object[] mWeatherList;




    public Weather_RecycleViewAdapter(Object[] theWeatherList) {
        this.mWeatherList = theWeatherList;
    }


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


    @Override
    public int getItemCount() {
        return mWeatherList.length;
    }

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
                binding.weatherCardWind.setText(""+theWeather.getMyWindSpeed()+" mph");
            Picasso.with(mView.getContext())
                    .load("https://openweathermap.org/img/wn/"+theWeather.getMyIconID()+"@2x.png")
                    //.resize(binding.weatherCardIcon.getWidth(),binding.weatherCardIcon.getHeight())
                    .into(binding.weatherCardIcon);
            }
    }
}
