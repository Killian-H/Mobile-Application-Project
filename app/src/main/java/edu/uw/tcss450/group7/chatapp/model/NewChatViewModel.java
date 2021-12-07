package edu.uw.tcss450.group7.chatapp.model;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ViewModel;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;


import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat;
import edu.uw.tcss450.group7.chatapp.ui.contact.Contact;

public class NewChatViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mAddContactList;

    public NewChatViewModel(@NonNull Application application) {
        super(application);
        mAddContactList = new MutableLiveData<>();
        mAddContactList.setValue(new ArrayList<>());
    }

    private void handleError(final VolleyError error) {
        mAddContactList = new MutableLiveData<>();
        Log.e("CONNECTION ERROR", error.toString());
    }

    public void connectCreateChatAndAddUsers(final String jwt, final String chatName) {
        Log.d("In connectCreateChatAndAddUsers", "chatName: " + chatName);
        String url = getApplication().getResources().getString(R.string.base_url) +
                "chats";

        HashMap<String, String> params = new HashMap<>();
        params.put("name", chatName);

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(params),
                this::handleCreateResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    private void handleCreateResult(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;


        try {
            JSONObject root = result;
            if (root.has("success")) {
                //int chatId = root.getInt("chatId"));
                Log.d("Created Chat", "Success");
                Log.d("Created Chat", result.toString());

            } else {
                Log.e("ERROR!", "No chatid");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }
}
