/*
 * TCSS 450
 *
 * View model for the list of chat rooms.
 */
package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

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
import edu.uw.tcss450.group7.chatapp.ui.contact.Contact;

/**
 * View model for the list of chat rooms in the ChatListFragment.
 */
public class ChatListViewModel extends AndroidViewModel {

    /* The list containing the chats. */
    private MutableLiveData<List<Chat>> mChatList;

    /**
     * Initializes the chat list as an array list of mutable data.
     *
     * @param application Base class for mainaining global application state.
     */
    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mChatList = new MutableLiveData<>();
        mChatList.setValue(new ArrayList<>());
    }

    /**
     *
     * @param owner
     * @param observer
     */
    public void addChatListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Chat>> observer) {
        mChatList.observe(owner, observer);
    }

    /**
     * Hnadles an error if Volley throws one.
     *
     * @param error The error thrown.
     */
    private void handleError(final VolleyError error) {
        //you should add much better error handling in a production release.
        //i.e. YOUR PROJECT
        mChatList = new MutableLiveData<>();
        Log.e("CONNECTION ERROR", error.toString());
    }

    /**
     *
     * ]=
     * @param result
     */
    private void handleResult(final JSONObject result) {
        ArrayList<Chat> temp= new ArrayList<>();
        IntFunction<String> getString = getApplication().getResources()::getString;

        try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_chat_list_data))) {
                JSONArray data = root.getJSONArray(
                        getString.apply(R.string.keys_json_chat_list_data));

                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonContact = data.getJSONObject(i);
                    edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat chat = new edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat.Builder(
                            jsonContact.getInt(
                                    getString.apply(
                                            R.string.keys_json_chat_id)),
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_chat_name)))
   //                         .addRecentMessage("mock last message")
                            .build();
                    if (!temp.contains(chat)) {
                        temp.add(chat);
                    }
                }
            } else {
                Log.e("ERROR!", "No data array");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mChatList.setValue(temp);
    }

    public void connectGet(final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "chats";
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
