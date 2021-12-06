package edu.uw.tcss450.group7.chatapp.ui.contact;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class ContactRequest implements Serializable {

    private final int mMemberid;
    private final String mUsername;
    private final String mMessage;

    public ContactRequest (int memberid, String username, String message){

        mMemberid = memberid;
        mUsername = username;
        mMessage = message;


    }



    public String getMessage(){
        return mMessage;
    }

    public String getUsername(){

        return mUsername;

    }
    public int getMemberid(){

        return mMemberid;

    }


    @Override


    public boolean equals(@Nullable Object other){
        boolean result = false;

        if(other instanceof  ContactRequest){

            result = mMemberid == ((ContactRequest) other).mMemberid;

        }

        return result;

    }



}
