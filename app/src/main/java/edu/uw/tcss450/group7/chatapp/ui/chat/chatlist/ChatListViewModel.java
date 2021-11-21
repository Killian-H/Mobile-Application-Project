package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.ui.contact.Contact;


public class ChatListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Chat>> mChatList;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mChatList = new MutableLiveData<>();
        mChatList.setValue(new ArrayList<>());
    }

    public void addChatListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Chat>> observer) {
        mChatList.observe(owner, observer);
    }

//    private void handleError(final VolleyError error) {
//        //you should add much better error handling in a production release.
//        //i.e. YOUR PROJECT
//        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
//        throw new IllegalStateException(error.getMessage());
//    }
//    private void handleResult(final JSONObject result) {
//        ArrayList<Contact> temp= new ArrayList<>();
//        IntFunction<String> getString =
//                getApplication().getResources()::getString;
//        try {
//            JSONObject root = result;
//            if (root.has(getString.apply(R.string.keys_json_contacts_data))) {
//                JSONArray data = root.getJSONArray(
//                        getString.apply(R.string.keys_json_contacts_data));
//
//                for(int i = 0; i < data.length(); i++) {
//                    JSONObject jsonContact = data.getJSONObject(i);
//                    edu.uw.tcss450.group7.chatapp.ui.contact.Contact contact = new edu.uw.tcss450.group7.chatapp.ui.contact.Contact.Builder(
//                            jsonContact.getString(
//                                    getString.apply(
//                                            R.string.keys_json_contacts_firstname)),
//                            jsonContact.getString(
//                                    getString.apply(
//                                            R.string.keys_json_contacts_lastname)))
//                            .addEmail(jsonContact.getString(
//                                    getString.apply(
//                                            R.string.keys_json_contacts_email)))
//                            .addUserName(jsonContact.getString(
//                                    getString.apply(
//                                            R.string.keys_json_contacts_username)))
//                            .addMemberId(jsonContact.getInt(
//                                    getString.apply(
//                                            R.string.keys_json_contacts_memberid)))
//                            .addVerified(jsonContact.getInt(
//                                    getString.apply(
//                                            R.string.keys_json_contacts_verified)))
//                            .build();
//                    if (!temp.contains(contact)) {
//                        temp.add(contact);
//                    }
//                }
//            } else {
//                Log.e("ERROR!", "No data array");
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("ERROR!", e.getMessage());
//        }
//        mContactList.setValue(temp);
//        mIncomingList.setValue(temp);
//    }
    public void connectGet() {
        mChatList.setValue(
                edu.uw.tcss450.group7.chatapp.ui.Chat.ChatRoomGenerator.getChatList());
    }
}
