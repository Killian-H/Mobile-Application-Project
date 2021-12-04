package edu.uw.tcss450.group7.chatapp.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
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
import edu.uw.tcss450.group7.chatapp.ui.contact.Contact;

public class ContactsViewModel extends AndroidViewModel {

    private MutableLiveData<List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> mContactList;
    private MutableLiveData<List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> mIncomingList;
    private MutableLiveData<List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> mSearchList;
    private MutableLiveData<List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> mVerifiedList;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mIncomingList = new MutableLiveData<>();
        mSearchList = new MutableLiveData<>();
        mVerifiedList = new MutableLiveData<>();
        mContactList.setValue(new ArrayList<>());
        mIncomingList.setValue(new ArrayList<>());
        mSearchList.setValue(new ArrayList<>());
        mVerifiedList.setValue(new ArrayList<>());
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> observer) {
        mContactList.observe(owner, observer);
    }


    public void addIncomingListObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> observer) {
        mIncomingList.observe(owner, observer);
    }

    public void addSearchListObserver(@NonNull LifecycleOwner owner,
                                      @NonNull Observer<? super List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> observer) {
        mSearchList.observe(owner, observer);
    }

    public void addVerifiedListObserver(@NonNull LifecycleOwner owner,
                                      @NonNull Observer<? super List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> observer) {
        mVerifiedList.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        mSearchList.setValue(new ArrayList<>());
        Log.e("CONNECTION ERROR", error.toString());
    }

    private void handleResult(final JSONObject result) {
        ArrayList<Contact> temp = new ArrayList<>();
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_contacts_data))) {
                JSONArray data = root.getJSONArray(
                        getString.apply(R.string.keys_json_contacts_data));

                for (int i = 0; i < data.length(); i++) {
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
        mIncomingList.setValue(temp);
        mVerifiedList.setValue(filterVerified(temp));
    }

    private List<Contact> filterVerified(List<Contact> contactList) {
        List<Contact> result = new ArrayList<Contact>();
        for(Contact contact : contactList) {
            if(contact.getVerified()) {
                result.add(contact);
            }
        }
        return result;
    }

    private void handleResultSearch(final JSONObject result) {
        ArrayList<Contact> temp = new ArrayList<>();
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_contacts_data))) {
                JSONArray data = root.getJSONArray(
                        getString.apply(R.string.keys_json_contacts_data));

                for (int i = 0; i < data.length(); i++) {
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
        mSearchList.setValue(temp);
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

    public void getIncomingRequests(final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contacts/requests";
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

    public void connectGetSearch(final String jwt, String email) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contacts/search";
        JSONObject body = new JSONObject();
        try {
//            body.put("memberid", 35);
            body.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                this::handleResultSearch,
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


    public void removeContact(final String jwt, int memberId) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contacts/" + memberId;
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        connectGet(jwt);
                    }
                },
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