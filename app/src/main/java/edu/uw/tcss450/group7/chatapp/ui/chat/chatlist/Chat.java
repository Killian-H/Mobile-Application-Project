package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

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
public class Chat implements Serializable {

    private final int mChatId;
    private final String mRecentMessage;
    private final String mChatName;

    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */
    public static class Builder {
        private int mChatId = -1;
        private String mChatName= "Test Chat";
        private String mRecentMessage= "Test Message";


        /**
         * Constructs a new Builder.
         *
         */
        public Builder(int chatId, String chatName) {
            mChatId = chatId;
            mChatName = chatName;
        }

        /**
         * Add an optional email for the Chat.
         * @param recentMessage an email for the Chat
         * @return the Builder of this Chat
         */
        public edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat.Builder addRecentMessage(final String recentMessage) {
            mRecentMessage = recentMessage;
            return this;
        }


        /**
         * Add an member id to Chat
         * @param id an member id for the Chat
         * @return the Builder of this Chat
         */
        public edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat.Builder addChatId(final int id) {
            mChatId = id;
            return this;
        }

        public Chat build() {
            return new Chat(this);
        }

    }

    private Chat(final edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat.Builder builder) {
        this.mChatId = builder.mChatId;
        this.mChatName = builder.mChatName;
        this.mRecentMessage = builder.mRecentMessage;
    }


    public Integer getChatId() {return mChatId;}

    public String getChatName() {return mChatName;}

    public String getRecentMessage() {return mRecentMessage;}



}
