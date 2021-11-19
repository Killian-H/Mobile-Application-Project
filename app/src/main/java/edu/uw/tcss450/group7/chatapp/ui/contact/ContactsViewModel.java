package edu.uw.tcss450.group7.chatapp.ui.contact;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import edu.uw.tcss450.group7.chatapp.R;

public class ContactsViewModel extends AndroidViewModel {

    private MutableLiveData<List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> mContactList;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mContactList.setValue(new ArrayList<>());
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> observer) {
        mContactList.observe(owner, observer);
    }
    private void handleError(final VolleyError error) {
        //you should add much better error handling in a production release.
        //i.e. YOUR PROJECT
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
    private void handleResult(final JSONObject result) {
        ArrayList<Contact> temp= new ArrayList<>();
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;
                if (root.has(getString.apply(R.string.keys_json_contacts_data))) {
                    JSONArray data = root.getJSONArray(
                            getString.apply(R.string.keys_json_contacts_data));

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonContact = data.getJSONObject(i);
                        edu.uw.tcss450.group7.chatapp.ui.contact.Contact contact = new edu.uw.tcss450.group7.chatapp.ui.contact.Contact.Builder(
                                jsonContact.getString(
                                        getString.apply(
                                                R.string.keys_json_contacts_firstname)),
                                jsonContact.getString(
                                        getString.apply(
                                                R.string.keys_json_contacts_lastname)))
                                .addEmail(jsonContact.getString(
                                        getString.apply(
                                                R.string.keys_json_contacts_email)))
                                .addUserName(jsonContact.getString(
                                        getString.apply(
                                                R.string.keys_json_contacts_username)))
                                .addMemberId(jsonContact.getInt(
                                        getString.apply(
                                                R.string.keys_json_contacts_memberid)))
                                .addVerified(jsonContact.getInt(
                                        getString.apply(
                                                R.string.keys_json_contacts_verified)))
                                .build();
                        if (!temp.contains(contact)) {
                            temp.add(contact);
                        }
                    }
                } else {
                    Log.e("ERROR!", "No data array");
                }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mContactList.setValue(temp);
    }
    public void connectGet(final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contacts";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
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

}
