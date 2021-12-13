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
import java.util.Stack;
import java.util.function.IntFunction;


import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat;
import edu.uw.tcss450.group7.chatapp.ui.contact.Contact;

public class NewChatViewModel extends AndroidViewModel {

    private MutableLiveData<List<Integer>> mAddMemList;
    private MutableLiveData<Stack<Integer>> mAddMemStack;
    private int mChatId;
    private String mJwt;

    public NewChatViewModel(@NonNull Application application) {
        super(application);
        mAddMemList = new MutableLiveData<>();
        mAddMemList.setValue(new ArrayList<>());
        mAddMemStack = new MutableLiveData<>();
        mAddMemStack.setValue(new Stack<>());
    }

    public void addMemberID(int ID) {
        List<Integer> temp = mAddMemList.getValue();
        temp.add(ID);
        mAddMemList.setValue(temp);
        Log.d("In Add MemberID", "" + mAddMemList.getValue());
    }

    public void removeMemberID(int ID) {
        List<Integer> temp = mAddMemList.getValue();
        if (temp.contains((Integer) ID)) {
            temp.remove((Integer) ID);
        }

        mAddMemList.setValue(temp);
        Log.d("In remove MemberID", "" + mAddMemList.getValue());
    }

    public void clearMemberID() {
        mAddMemList.getValue().clear();
    }

    public List<Integer> getAddMemList() {
        return mAddMemList.getValue();
    }

    private void handleError(final VolleyError error) {
        mAddMemList = new MutableLiveData<>();
        Log.e("CONNECTION ERROR", error.toString());
    }


    public void connectCreateChatAndAddUsers(final String jwt, final String chatName) {
        mJwt = jwt;
        Log.d("In connectCreateChatAndAddUsers", "jwt: " + jwt);
        Log.d("In connectCreateChatAndAddUsers", "mJwt: " + mJwt);
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

    private void connectAddUsers() {
        if (!mAddMemStack.getValue().isEmpty()) {
            int userID = mAddMemStack.getValue().pop();
            String url = getApplication().getResources().getString(R.string.base_url) +
                    "chats/" + mChatId +"/";

            HashMap<String, String> params = new HashMap<>();
            Log.d("connectAddUsers", "User ID" + userID);
            params.put("memberid", "" +userID);

            Request request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    new JSONObject(params),
                    this::handleAddResult,
                    this::handleError) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    // add headers <key,value>
                    headers.put("Authorization", mJwt);
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
        } else {
            Log.d("connectAddUsers", "User stack empty");
        }
    }

    private void handleCreateResult(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;


        try {
            JSONObject root = result;
            if (root.has("success")) {
                mChatId = root.getInt("chatID");
                if(!mAddMemList.getValue().isEmpty()) {
                    List<Integer> tempL = mAddMemList.getValue();
                    Stack<Integer> tempS = new Stack<Integer>();
                    Log.d("HandleCreateResult", "tempL: " + tempL);
                    tempS.addAll(tempL);
                    mAddMemList.setValue(new ArrayList<>());
                    mAddMemStack.setValue(tempS);
                    connectAddUsers();
                } else {
                    Log.e("ERROR! - New Chat", "Empty ID list");
                }

            } else {
                Log.e("ERROR! - New Chat", "Failed to create Chat");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }

    private void handleAddResult(final JSONObject result){
        try {
            JSONObject root = result;
            if (root.has("success")) {
                if(root.getBoolean("success")) {
                    connectAddUsers();
                }
            } else {
                Log.e("ERROR! - Add to Chat", "Failed to add user");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }

}
