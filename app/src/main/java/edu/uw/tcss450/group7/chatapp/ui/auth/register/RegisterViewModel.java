/*
 * TCSS 450
 * View model for the register.
 */
package edu.uw.tcss450.group7.chatapp.ui.auth.register;

import android.app.Application;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Class creating a view model for the registration.
 *
 * @author Charles Bryan
 * @author Troy Zon
 * @author Zheng Zhong
 * Commented by: Killian Hickey
 */
public class RegisterViewModel extends AndroidViewModel {

    // The response from the web service.
    private MutableLiveData<JSONObject> mResponse;

    /**
     * Overloaded constructor calling it's parent. Initializes mResponse
     * and assigns it a new JSON value.
     *
     * @param application The application the view model is connected to.
     */
    public RegisterViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
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
     * Connects to the auth endpoint of the web server in an attempt
     * to register the user.
     * @param first First name provided by the user.
     * @param last Last name provided by the user.
     * @param email Email provided by the user.
     * @param password Password provided by the user.
     */
    public void connect(final String first,
                        final String last,
                        final String email,
                        final String password) {
        String url = "https://mobile-application-project-450.herokuapp.com/auth";
        JSONObject body = new JSONObject();
        try {
            body.put("first", first);
            body.put("last", last);
            body.put("email", email);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

}
