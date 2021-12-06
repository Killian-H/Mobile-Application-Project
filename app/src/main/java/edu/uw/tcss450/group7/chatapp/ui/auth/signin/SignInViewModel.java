/*
 * TCSS 450
 * View model for the sign in fragment.
 */
package edu.uw.tcss450.group7.chatapp.ui.auth.signin;

import android.app.Application;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.group7.chatapp.io.RequestQueueSingleton;

/**
 * Class creating a view model for the sign in fragment.
 *
 * @author Charles Bryan
 * @author Group 7
 * Commented by: Killian Hickey
 */
public class SignInViewModel extends AndroidViewModel {

    /* Response from the server. */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * Overloaded constructor calling its parent. Initializes
     * the JSON response object.
     *
     * @param application The application the view model is connected to.
     */
    public SignInViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Takes note of the response from the server when the user attempts to sign in.
     *
     * @param owner The current life cycle of the provider.
     * @param observer Response from the server.
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }


    /**
     * If there is an error when parsing the JSON object this method will
     * handle the error and output a message in the Logcat.
     *
     * @param error The error returned by the Volley library.
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
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
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * This method connects the front end of sign-in with the back-end
     * of the server. Connects to the authorization endpoint of our server.
     * It sends the email and password provided by the user to check if the
     * email and password exist.
     *
     * @param email The email provided by the user.
     * @param password The password provided by the user.
     */
    public void connect(final String email, final String password) {
        String url = "https://mobile-application-project-450.herokuapp.com/auth";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                String credentials = email + ":" + password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

}
