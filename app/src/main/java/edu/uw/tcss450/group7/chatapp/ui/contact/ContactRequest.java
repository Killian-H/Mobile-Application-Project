package edu.uw.tcss450.group7.chatapp.ui.contact;

import androidx.annotation.Nullable;
import java.io.Serializable;

/**
 * Class to create object for the ContactRequest. Building an Object requires a publish first name and last name.
 *
 * Used for the push notification receiver.
 *
 *
 */
public class ContactRequest implements Serializable {

    //Private class fields
    private final int mMemberid;
    private final String mUsername;
    private final String mMessage;

    //Constructor
    public ContactRequest (int memberid, String username, String message){
        mMemberid = memberid;
        mUsername = username;
        mMessage = message;
    }


    //Getter methods
    public String getMessage(){ return mMessage; }

    public String getUsername(){ return mUsername; }


    @Override
    public boolean equals(@Nullable Object other){
        boolean result = false;
        if(other instanceof  ContactRequest){
            result = mMemberid == ((ContactRequest) other).mMemberid;
        }
        return result;
    }

}
