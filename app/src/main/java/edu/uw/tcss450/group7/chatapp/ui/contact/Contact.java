package edu.uw.tcss450.group7.chatapp.ui.contact;

import java.io.Serializable;

/**
 * Class to encapsulate a Phish.net Blog Post. Building an Object requires a publish date and title.
 *
 * Optional fields include URL, teaser, and Author.
 *
 *
 * @author Charles Bryan
 * @version 14 September 2018
 */
public class Contact implements Serializable {

    private final int mMemberId;
    private final String mUserName;
    private final String mFirstName;
    private final String mLastName;
    private final String mEmail;
    private final int mVerified;

    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */
    public static class Builder {
        private int mMemberId = -1;
        private String mUserName = "TestUserName";
        private String mFirstName = "TestFname";
        private String mLastName = "TestLname";
        private String mEmail = "TestEmail";
        private int mVerified = -1;


        /**
         * Constructs a new Builder.
         *
         * @param firstName the first name of the Contact.
         * @param lastName the last name of the Contact.
         */
        public Builder(String firstName, String lastName) {
            this.mFirstName = firstName;
            this.mLastName = lastName;
        }

        /**
         * Add an optional email for the Contact.
         * @param email an email for the Contact
         * @return the Builder of this Contact
         */
        public Builder addEmail(final String email) {
            mEmail = email;
            return this;
        }

        public Builder addUserName(final String userName) {
            mUserName = userName;
            return this;
        }
        /**
         * Add an member id to contact
         * @param id an member id for the contact
         * @return the Builder of this Contact
         */
        public Builder addMemberId(final int id) {
            mMemberId = id;
            return this;
        }

        public Builder addVerified(final int verified) {
            mVerified = verified;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }

    }

    private Contact(final Builder builder) {
        this.mMemberId = builder.mMemberId;
        this.mUserName = builder.mUserName;
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mEmail = builder.mEmail;
        this.mVerified = builder.mVerified;
    }

    public Integer getMemberId() {
        return mMemberId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }


    public String getFullName() {
        return mLastName + ", " + mFirstName;
    }

    public String getEmail() {
        return mEmail;
    }


}
