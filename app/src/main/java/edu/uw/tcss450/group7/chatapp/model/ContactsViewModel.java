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

/**
 * Main Contacts fragment view model to handle data between fragments and endpoints
 *
 */
public class ContactsViewModel extends AndroidViewModel {
    /**List of Contacts user have*/
    private MutableLiveData<List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> mContactList;
    /**List of Contacts requests user got*/
    private MutableLiveData<List<edu.uw.tcss450.group7.chatapp.ui.contact.Contact>> mIncomingList;
    /**List of Contacts user searched*/
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


    /**
     * Listeners for responses from the server when the user starts the registration process.
     *
     * @param owner Handles life cycles changes without the need for code inside this
     *              view model.
     * @param observer Listens for responses from the server.
     */
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

    /**
     * Handles server errors received from the web service.
     *
     * @param error The error thrown by parsing the JSON object.
     */
    private void handleError(final VolleyError error) {
        mSearchList.setValue(new ArrayList<>());
        Log.e("CONNECTION ERROR", error.toString());
    }

    /**
     * Handles the data server returned.
     *
     * @param result The data sent by server.
     */
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
                    if(jsonContact.getInt(
                            getString.apply(
                                    R.string.keys_json_contacts_verified)) == 1){
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
                }}
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

    /**
     * Method that filters Verified contacts
     */
    private List<Contact> filterVerified(List<Contact> contactList) {
        List<Contact> result = new ArrayList<Contact>();
        for(Contact contact : contactList) {
            if(contact.getVerified()) {
                result.add(contact);
            }
        }
        return result;
    }

    /**
     * Handles the Search data server returned.
     *
     * @param result The data sent by server.
     */
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


    /**
     * Method that gets the contacts from the server
     *
     * @param jwt User Jwt to send to server.
     */
    public void connectGet(final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contacts";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleResult(response);
                        getIncomingRequests(jwt);
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


    /**
     * Method that gets the incoming contact requests from the server
     *
     * @param jwt User Jwt to send to server.
     */
    public void getIncomingRequests(final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contacts/requests";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleIncomingRequestResult,
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

    /**
     * Handles the Incoming Request data server returned.
     *
     * @param result The data sent by server.
     */
    private void handleIncomingRequestResult(final JSONObject result) {
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
                            .IsIncomingRequest(1)
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
        temp.addAll(mContactList.getValue());
        mContactList.setValue(temp);
    }

    /**
     * Method that sends search request to the server
     *
     * @param jwt User Jwt to send to server.
     * @param email Email to search for.
     */
    public void connectGetSearch(final String jwt, String email) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contacts/search";
        JSONObject body = new JSONObject();
        try {
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

    /**
     * Method that sends remove contact to the server
     *
     * @param jwt User Jwt to send to server.
     * @param memberId memberId to be removed from Contacts.
     */
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

    /**
     * Method that sends contact request to the server
     *
     * @param jwt User Jwt to send to server.
     * @param memberId memberId to send a new contact request.
     */
    public void sendContactRequest(final String jwt, int memberId) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contacts/";
        JSONObject body = new JSONObject();
        try {
            body.put("memberid_b", memberId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        connectGet(jwt);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        connectGet(jwt);
                    }
                }) {
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

    /**
     * Method that sends response to the contact request from other contact
     *
     * @param jwt User Jwt to send to server.
     * @param memberId memberId to send a new contact request.
     * @param response Response value to weather accept or decline the request.
     */
    public void respondIncomingRequest(final String jwt, int memberId, Boolean response) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "contacts/verify";
        JSONObject body = new JSONObject();
        try {
            body.put("memberid", memberId);
            body.put("option", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        connectGet(jwt);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        connectGet(jwt);
                    }
                }) {
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