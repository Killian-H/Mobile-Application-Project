package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
/**
 * View model for location data
 * @version: 12/12/2021
 *
 * @author Charles Bryan
 * @author Killian Hickey
 * Commented by: Aaron Purslow
 */
public class LocationViewModel extends ViewModel {
    //Live location data
    private MutableLiveData<Location> mLocation;

    public LocationViewModel() {
        mLocation = new MediatorLiveData<>();
    }

    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    /**
     *
     * @param location sets user location based on gps data
     */
    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    /**
     *
     * @return the current location data
     */
    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }

}
