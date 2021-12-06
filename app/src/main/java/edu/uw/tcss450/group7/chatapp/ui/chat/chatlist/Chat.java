package edu.uw.tcss450.group7.chatapp.ui.chat.chatlist;

import androidx.annotation.NonNull;

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

    /* The ID of the chatroom. */
    private final int mChatId;

    /* The most recent message. */
    private final String mRecentMessage;

    /* The name of the chat room. */
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

        /**
         * Builds a new Chat.
         *
         * @return The new Chatroom.
         */
        public Chat build() {
            return new Chat(this);
        }

    }

    private Chat(final edu.uw.tcss450.group7.chatapp.ui.chat.chatlist.Chat.Builder builder) {
        this.mChatId = builder.mChatId;
        this.mChatName = builder.mChatName;
        this.mRecentMessage = builder.mRecentMessage;
    }

    /**
     * Getter to return the chat id.
     *
     * @return The chat id.
     */
    public Integer getChatId() {return mChatId;}

    /**
     * Getter which returns the chat name.
     *
     * @return The chat name.
     */
    public String getChatName() {return mChatName;}

    /**
     * Getter which returns the most recent message.
     *
     * @return The most recent message.
     */
    public String getRecentMessage() {return mRecentMessage;}

    /**
     * Returns the name of the chat room.
     *
     * @return The chat name.
     */
    @NonNull
    @Override
    public String toString() {
        return getChatName();
    }
}
