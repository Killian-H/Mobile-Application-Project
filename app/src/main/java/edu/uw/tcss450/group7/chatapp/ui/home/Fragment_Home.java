package edu.uw.tcss450.group7.chatapp.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Home extends Fragment {

    private FragmentHomeBinding binding;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);


        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


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
                    Fragment_HomeDirections.actionNavigationHomeToNavigationSettings());

        }


        return super.onOptionsItemSelected(item);
    }


}