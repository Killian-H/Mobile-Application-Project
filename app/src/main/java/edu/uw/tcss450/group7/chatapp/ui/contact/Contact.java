package edu.uw.tcss450.group7.chatapp.ui.contact;

import java.io.Serializable;

/**
 * Class to create object for the Contact from database. Building an Object requires a Contact first name and last name.
 *
 * Optional fields include email, username, memberId, verified, and IsIncomingRequest.
 *
 *
 */
public class Contact implements Serializable {

    private final int mMemberId;
    private final String mUserName;
    private final String mFirstName;
    private final String mLastName;
    private final String mEmail;
    private final int mVerified;
    private final int mIsIncomingRequest;

    /**
     * Helper class for building Credentials.
     *
     */
    public static class Builder {
        private int mMemberId = -1;
        private String mUserName = "TestUserName";
        private String mFirstName = "TestFname";
        private String mLastName = "TestLname";
        private String mEmail = "TestEmail";
        private int mVerified = -1;
        private int mIsIncomingRequest = 0;


        /**
         * Constructs a new Builder.
         *
         * @param firstName the first name of the Contact.
         * @param lastName the last name of the Contact.
         */
        public Builder(String firstName, String lastName) {
            this.mFirstName = firstName;
            this.mLastName = lastName;
            this.mIsIncomingRequest = 0;
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

        /**
         * Add an optional userName for the Contact.
         * @param userName an userName for the Contact
         * @return the Builder of this Contact
         */
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

        /**
         * Add an verified to contact
         * @param verified an verified for the contact
         * @return the Builder of this Contact
         */
        public Builder addVerified(final int verified) {
            mVerified = verified;
            return this;
        }

        /**
         * Add an isRequest to contact
         * @param isRequest an isRequest for the contact
         * @return the Builder of this Contact
         */
        public Builder IsIncomingRequest(final int isRequest) {
            mIsIncomingRequest = isRequest;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }

    }

    /**
     * Constructor for contact
     * @param builder an builder for the contact
     * @return the Builder of this Contact
     */
    private Contact(final Builder builder) {
        this.mMemberId = builder.mMemberId;
        this.mUserName = builder.mUserName;
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mEmail = builder.mEmail;
        this.mVerified = builder.mVerified;
        this.mIsIncomingRequest =  builder.mIsIncomingRequest;
    }

    /**
     * Getter methods for Contact
     */
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
        return mFirstName + " " + mLastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public Boolean getVerified() {
        return mVerified==1;
    }

    public Boolean getIsIncomingRequest() {
        return mIsIncomingRequest==1;
    }


}
