package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.io.RequestQueueSingleton;

/**
 * Main weather fragment view model to handle data between fragments and endpoints
 * @version: 12/12/2021
 * @author Aaron Purslow
 * Commented by: Aaron Purslow
 */

public class Fragment_weatherViewModel extends AndroidViewModel {
    // The response from the web service.
    private MutableLiveData<JSONObject> mResponse;
    /*when json response received for any reason save Latlng to be used to update where the map is opened at.
    See also FragmentWeatherMap.
    * */
    private LatLng markerSaved;


    public Fragment_weatherViewModel(@NonNull Application application) {

        super(application);
        mResponse = new MutableLiveData<>();

    }
    /**
     * Listener for responses from the server when the user starts the registration process.
     *
     * @param owner Handles life cycles changes without the need for code inside this
     *              view model.
     * @param observer Listens for responses from the server.
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {
        mResponse.setValue(result);
    }

    /**
     * Handles server errors received from the web service.
     *
     * @param error The error thrown by parsing the JSON object.
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError weather");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError weather");
            }
        }
    }

    /**
     * Connects to the weather endpoint and attempts to return weather data.
     * Example input  {"lat": "-69","lon":"-69"}
     * Expected output to be JSON object containing all weather data at specified lat/lon
     * @param latitude First name provided by the user.
     * @param longitude Last name provided by the user.

     */
    public void connect(final double longitude,
                        final double latitude) {
        String urlConnect = "https://mobile-application-project-450.herokuapp.com/weather";
        JSONObject body = new JSONObject();
        try {
            body.put("lat", latitude);
            body.put("lon", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request  request = new JsonObjectRequest(
                Request.Method.POST,
                urlConnect,
                body,
                response1 -> {handleResult(response1); markerSaved = new LatLng(latitude,longitude);},
                this::handleError);




        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     *
     * @return saved marker coordinates
     */
    public LatLng getMarkerSaved() {
        return markerSaved;
    }
}
